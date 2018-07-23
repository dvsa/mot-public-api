package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.VehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.mapper.VehicleResponseMapperFactory;
import uk.gov.dvsa.mot.trade.api.response.mapper.VehicleV4ResponseMapper;
import uk.gov.dvsa.mot.trade.read.core.TradeReadService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TradeServiceRequestHandlerTest {
    @Mock
    private TradeReadService tradeReadService;

    @Mock
    private VehicleResponseMapperFactory vehicleResponseMapperFactory;

    @Mock
    private VehicleV4ResponseMapper vehicleMapper;

    @Mock
    private Context lambdaContext;

    @Mock
    private ContainerRequestContext requestContext;

    private TradeServiceRequest request;

    public TradeServiceRequestHandlerTest() {

        MockitoAnnotations.initMocks(this);
    }

    /**
     * Convenience function for testing getTradeMotTests calls with less boilerplate.
     *
     * @param request The request to test against
     * @return The result of the call to getTradeMotTests
     * @throws TradeException if getTradeMotTests throws
     */
    private Response createHandlerAndGetTradeMotTests(TradeServiceRequest request) throws TradeException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);
        sut.setVehicleResponseMapperFactory(vehicleResponseMapperFactory);

        return sut.getTradeMotTests(request.getVehicleId(), request.getNumber(),
                request.getRegistration(), request.getDate(), request.getPage(), requestContext);
    }

    /**
     * Convenience function for testing getTradeMotTestsLegacy calls with less boilerplate
     *
     * @param registration is a path parameter passed to getTradeMotTestsLegacy
     * @param make is a path parameter passed to getTradeMotTestsLegacy
     * @return the return value of getTradeMotTestsLegacy
     * @throws TradeException if getTradeMotTestsLegacy throws
     */
    private Response createHandlerAndGetLegacy(String registration, String make) throws TradeException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);
        sut.setVehicleResponseMapperFactory(vehicleResponseMapperFactory);

        return sut.getTradeMotTestsLegacy(registration, make, requestContext);
    }

    @Before
    public void setup() {
        when(vehicleResponseMapperFactory.getMapper(any())).thenReturn(vehicleMapper);
        request = new TradeServiceRequest();
    }

    /**
     * The list of makes from the read service should be passed back unchanged
     */
    @Test
    public void getMakes_ReturnsGivenMakes() throws TradeException {
        // create test data
        List<String> theMakes = new ArrayList<>();
        theMakes.add("FORD");
        theMakes.add("SKODA");

        when(tradeReadService.getMakes()).thenReturn(theMakes);

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);

        List<String> makes = sut.getMakes(null, lambdaContext);

        assertEquals(theMakes, makes);
    }

    /**
     * Any exception thrown by the read service should be converted to an InternalServerErrorException
     */
    @Test(expected = InternalServerErrorException.class)
    public void getMakes_ConvertsExceptionsToInternalServerError() throws TradeException {

        when(tradeReadService.getMakes()).thenThrow(new IndexOutOfBoundsException());

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);

        sut.getMakes(null, lambdaContext);
    }

    /**
     * Really unexpected conditions should give us an BadRequestException.
     * <p>
     * Here we pass in a null request, which will result in a NullPointerException which gets caught and converted.
     */
    @Test(expected = BadRequestException.class)
    public void getTradeMotTests_NullRequest_Throws() throws TradeException {

        createHandlerAndGetTradeMotTests(new TradeServiceRequest());
    }

    /**
     * If we ask for a vehicle ID which doesn't exist we should get an InvalidResourceException.
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTests_VehicleId_VehicleDoesNotExist() throws TradeException {

        List<Vehicle> vehicles = new ArrayList<>();
        final int vehicleId = 4;
        when(tradeReadService.getVehiclesByVehicleId(vehicleId)).thenReturn(vehicles);

        request.setVehicleId(vehicleId);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * If search by vehicle ID and service returns a matching vehicle, we have it returned
     */
    @Test
    public void getTradeMotTests_VehicleId_VehicleExists() throws TradeException {

        Vehicle vehicle = new Vehicle();
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle);
        final int vehicleId = 4;
        List<VehicleResponse> mappedVehicles = Arrays.asList(new VehicleResponse());
        when(tradeReadService.getVehiclesByVehicleId(vehicleId)).thenReturn(vehicles);
        when(vehicleMapper.map(eq(vehicles))).thenReturn(mappedVehicles);

        request.setVehicleId(vehicleId);

        List<?> receivedVehicles = (List<?>) createHandlerAndGetTradeMotTests(request).getEntity();

        assertEquals(mappedVehicles, receivedVehicles);
    }

    /**
     * If we ask for a vehicle ID which has multiple vehicles we should get back all of them
     */
    @Test
    public void getTradeMotTests_VehicleId_VehiclesExist() throws TradeException {

        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);
        List<VehicleResponse> mappedVehicles = Arrays.asList(new VehicleResponse(), new VehicleResponse());
        final int vehicleId = 4;
        when(tradeReadService.getVehiclesByVehicleId(vehicleId)).thenReturn(vehicles);
        when(vehicleMapper.map(eq(vehicles))).thenReturn(mappedVehicles);

        request.setVehicleId(vehicleId);

        List<?> receivedVehicles = (List<?>) createHandlerAndGetTradeMotTests(request).getEntity();

        assertEquals(mappedVehicles, receivedVehicles);
    }

    /**
     * If the database read for a Vehicle ID query throws, we should get an internal server error
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_VehicleId_DatabaseThrows() throws TradeException {

        final int vehicleId = 4;
        when(tradeReadService.getVehiclesByVehicleId(vehicleId)).thenThrow(new IndexOutOfBoundsException());
        request.setVehicleId(vehicleId);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * If we ask for an MOT number which doesn't exist we should get an InvalidResourceException.
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTests_Number_TestDoesNotExist() throws TradeException {

        final long motNumber = 42;
        when(tradeReadService.getVehiclesMotTestsByMotTestNumber(motNumber)).thenReturn(new ArrayList<>());

        request.setNumber(motNumber);

        createHandlerAndGetTradeMotTests(request);
    }


    /**
     * If we ask for an MOT number which does exist we should get back an appropriate vehicle
     */
    @Test
    public void getTradeMotTests_Number_TestExists() throws TradeException {

        final long motNumber = 233432;
        Vehicle vehicle = new Vehicle();
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle);
        vehicle.setMotTestNumber(String.valueOf(motNumber));
        List<VehicleResponse> mappedVehicles = Arrays.asList(new VehicleResponse());
        when(tradeReadService.getVehiclesMotTestsByMotTestNumber(motNumber)).thenReturn(vehicles);
        when(vehicleMapper.map(eq(vehicles))).thenReturn(mappedVehicles);

        request.setNumber(motNumber);

        List<?> receivedVehicles = (List<?>) createHandlerAndGetTradeMotTests(request).getEntity();

        assertEquals(mappedVehicles, receivedVehicles);
    }

    /**
     * If the database read for an MOT number query throws, we should get an internal server error
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_Number_DatabaseThrows() throws TradeException {

        final long motNumber = 9871234;
        when(tradeReadService.getVehiclesMotTestsByMotTestNumber(motNumber)).thenThrow(new IndexOutOfBoundsException());
        request.setNumber(motNumber);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * If we provide only the make, this should result in a BadRequestException as Registration has to come with Make
     */
    @Test(expected = BadRequestException.class)
    public void getTradeMotTests_Make_BadRequest() throws TradeException {

        String make = "TESLA";

        request.setMake(make);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * If we ask for a registration which exist, we expect to find the vehicle
     */
    @Test
    public void getTradeMotTests_Registration_VehicleExists() throws TradeException {

        final String registration = "AA44VBG";
        final Vehicle vehicle = new Vehicle();
        vehicle.setRegistration(registration);
        List<VehicleResponse> mappedVehicles = Arrays.asList(new VehicleResponse());
        mappedVehicles.get(0).setRegistration(vehicle.getRegistration());

        when(tradeReadService.getVehiclesByRegistration(registration)).thenReturn(Arrays.asList(vehicle));
        when(vehicleMapper.map(eq(Arrays.asList(vehicle)))).thenReturn(mappedVehicles);

        request.setRegistration(registration);

        List<?> retrievedVehicles =  (List<?>) createHandlerAndGetTradeMotTests(request).getEntity();

        assertEquals("Should retrieve only one vehicle", 1, retrievedVehicles.size());
        assertEquals("Should pass through the vehicle from mapper", mappedVehicles, retrievedVehicles);
    }

    /**
     * If we ask for a registration and make which do not exist, we expect an InvalidResourceException
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTests_RegistrationMake_VehicleDoesNotExist() throws TradeException {

        final String registration = "AA44VBG";
        final String make = "ACME";

        when(tradeReadService.getVehiclesByRegistrationAndMake(registration, make)).thenReturn(Arrays.asList());

        request.setRegistration(registration);
        request.setMake(make);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * If there's an exception from the database call we expect to get an InternalServerErrorException
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_RegistrationMake_DatabaseError() throws TradeException {

        final String registration = "AA44VBG";

        when(tradeReadService.getVehiclesByRegistration(registration)).thenThrow(new ArithmeticException());

        request.setRegistration(registration);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * Unparseable dates cause internal server errors. This may change to a BadRequest in future.
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_Date_InvalidDate() throws TradeException {

        final String badDateString = "ObviouslyNotAValidDate";

        request.setDate(badDateString);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * We should get an InternalServerErrorException when we request a date/page that has nothing in it
     * <p>
     * This variant of the test does not explicitly set the page
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_Date_NothingForDate() throws TradeException {

        final String dateString = "2014-01-01";
        request.setDate(dateString);

        when(tradeReadService.getVehiclesByDatePage(any(Date.class), eq(0))).thenReturn(Arrays.asList());

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * We should get an InvalidResourceException when we request a date/page that has nothing in it
     * <p>
     * This variant of the test explicitly requests a non-default page
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTests_Date_NothingForDateAndPage() throws TradeException {

        final String dateString = "2014-01-01";
        final int page = 45;
        request.setDate(dateString);
        request.setPage(page);

        when(tradeReadService.getVehiclesByDatePage(any(Date.class), eq(page))).thenReturn(Arrays.asList());

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * When asked for a date and page which has vehicles on board, those vehicles should be returned.
     */
    @Test
    public void getTradeMotTests_Date_ReturnsVehicles() throws TradeException {

        final String dateString = "2014-01-01";
        final int page = 3;
        final Vehicle vehicle1 = new Vehicle();
        final Vehicle vehicle2 = new Vehicle();
        vehicle1.setRegistration("ONE");
        vehicle2.setRegistration("TWO");
        List<VehicleResponse> mappedVehicles = Arrays.asList(new VehicleResponse(), new VehicleResponse());
        mappedVehicles.get(0).setRegistration(vehicle1.getRegistration());
        mappedVehicles.get(1).setRegistration(vehicle2.getRegistration());

        request.setDate(dateString);
        request.setPage(page);

        when(tradeReadService.getVehiclesByDatePage(any(Date.class), eq(page))).thenReturn(Arrays.asList(vehicle1, vehicle2));
        when(vehicleMapper.map(eq(Arrays.asList(vehicle1, vehicle2)))).thenReturn(mappedVehicles);

        List<?> returnedVehicles = (List<?>) createHandlerAndGetTradeMotTests(request).getEntity();

        assertEquals(mappedVehicles, returnedVehicles);
    }

    /**
     * When the database call throws, expect an InternalServerErrorException
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_Date_DatabaseError() throws TradeException {

        final String dateString = "2014-01-01";
        final int page = 3;

        request.setDate(dateString);
        request.setPage(page);

        when(tradeReadService.getVehiclesByDatePage(any(Date.class), eq(page))).thenThrow(new ArithmeticException());

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * When asked for a page which has vehicles, the vehicles should be returned
     */
    @Test
    public void getTradeMotTests_Page_ReturnsVehicles() throws TradeException {

        final int page = 3;
        final Vehicle vehicle1 = new Vehicle();
        final Vehicle vehicle2 = new Vehicle();
        vehicle1.setRegistration("ONE");
        vehicle2.setRegistration("TWO");
        List<VehicleResponse> mappedVehicles = Arrays.asList(new VehicleResponse(), new VehicleResponse());
        mappedVehicles.get(0).setRegistration(vehicle1.getRegistration());
        mappedVehicles.get(1).setRegistration(vehicle2.getRegistration());

        request.setPage(page);

        when(tradeReadService.getVehiclesByPage(page)).thenReturn(Arrays.asList(vehicle1, vehicle2));
        when(vehicleMapper.map(eq(Arrays.asList(vehicle1, vehicle2)))).thenReturn(mappedVehicles);

        List<?> returnedVehicles = (List<?>) createHandlerAndGetTradeMotTests(request).getEntity();

        assertEquals(mappedVehicles, returnedVehicles);
    }

    /**
     * When called with appropriate header, API version is parsed correctly
     */
    @Test
    public void getTradeMotTests_ParsesApiVersionCorrectly() throws TradeException {

        final int page = 3;
        request.setPage(page);
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("Accept", Arrays.asList("application/json+v3"));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        when(requestContext.getHeaders()).thenReturn(headers);
        when(tradeReadService.getVehiclesByPage(page)).thenReturn(Arrays.asList(new Vehicle()));
        when(vehicleMapper.map(any())).thenReturn(Arrays.asList(new VehicleResponse()));

        createHandlerAndGetTradeMotTests(request).getEntity();

        verify(vehicleResponseMapperFactory).getMapper(argumentCaptor.capture());
        assertEquals("v3", argumentCaptor.getValue());
    }

    /**
     * When called with appropriate header but no version specified, endpoint works properly
     */
    @Test
    public void getTradeMotTests_WorksAsV1WhenNoApiVersionSpecified() throws TradeException {

        final int page = 3;
        request.setPage(page);
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("Accept", Arrays.asList("application/json"));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        when(requestContext.getHeaders()).thenReturn(headers);
        when(tradeReadService.getVehiclesByPage(page)).thenReturn(Arrays.asList(new Vehicle()));
        when(vehicleMapper.map(any())).thenReturn(Arrays.asList(new VehicleResponse()));

        createHandlerAndGetTradeMotTests(request).getEntity();

        verify(vehicleResponseMapperFactory).getMapper(argumentCaptor.capture());
        assertEquals(null, argumentCaptor.getValue());
    }

    /**
     * When called with appropriate header but no version specified, endpoint works properly
     */
    @Test
    public void getTradeMotTests_WorksAsV1WhenHeadersAreNotSet() throws TradeException {

        final int page = 3;
        request.setPage(page);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        when(requestContext.getHeaders()).thenReturn(null);
        when(tradeReadService.getVehiclesByPage(page)).thenReturn(Arrays.asList(new Vehicle()));
        when(vehicleMapper.map(any())).thenReturn(Arrays.asList(new VehicleResponse()));

        createHandlerAndGetTradeMotTests(request).getEntity();

        verify(vehicleResponseMapperFactory).getMapper(argumentCaptor.capture());
        assertEquals(null, argumentCaptor.getValue());
    }

    /**
     * When asked for a page which contains no vehicles, an InvalidResourceException should be thrown
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTests_Page_EmptyPage() throws TradeException {

        final int page = 3;

        request.setPage(page);

        when(tradeReadService.getVehiclesByPage(page)).thenReturn(null);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * When the database call throws, we should get an InternalServerErrorException
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_Page_DatabaseThrows() throws TradeException {

        final int page = 3;

        request.setPage(page);

        when(tradeReadService.getVehiclesByPage(page)).thenThrow(new IndexOutOfBoundsException());

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * A null request should get us an InvalidResourceException
     *
     * @throws TradeException always if the test is working correctly
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTestsLegacy_NullRequest() throws TradeException {

        createHandlerAndGetLegacy(null, null);
    }

    /**
     * A registration/make which doesn't match any vehicle should get us an InvalidResourceException
     *
     * @throws TradeException always
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTestsLegacy_NoVehicle() throws TradeException {

        final String registration = "NOTACAR";
        final String make = "NOTAMAKE";

        when(tradeReadService.getMotTestsByRegistrationAndMake(registration, make)).thenReturn(Arrays.asList());

        createHandlerAndGetLegacy(registration, make);
    }

    /**
     * When we pass in a registration and make corresponding to a vehicle we should get those tests back
     *
     * @throws TradeException never
     */
    @Test
    public void getTradeMotTestsLegacy_RegistrationMake_Valid() throws TradeException {

        final String registration = "AA88UJM";
        final String make = "FERRARI";

        DisplayMotTestItem test1 = new DisplayMotTestItem();
        test1.setMotTestNumber("1");
        test1.setRegistration(registration);
        test1.setMakeName(make);
        DisplayMotTestItem test2 = new DisplayMotTestItem();
        test1.setMotTestNumber("2");
        test1.setRegistration(registration);
        test1.setMakeName(make);
        List<DisplayMotTestItem> tests = Arrays.asList(test1, test2);

        when(tradeReadService.getMotTestsByRegistrationAndMake(registration, make)).thenReturn(tests);

        List<?> receivedTests = (List<?>) createHandlerAndGetLegacy(registration, make).getEntity();

        assertEquals(tests, receivedTests);
    }
}
