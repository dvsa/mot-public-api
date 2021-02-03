package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.tngtech.java.junit.dataprovider.DataProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.security.ParamObfuscator;
import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.ServiceTemporarilyUnavailableException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.VehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.mapper.VehicleResponseMapperFactory;
import uk.gov.dvsa.mot.trade.api.response.mapper.VehicleV4ResponseMapper;
import uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle.SearchVehicleResponseMapperFactory;
import uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle.SearchVehicleV6ResponseMapper;
import uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle.SearchVehicleV7ResponseMapper;
import uk.gov.dvsa.mot.trade.read.core.TradeAnnualTestsReadService;
import uk.gov.dvsa.mot.trade.read.core.TradeReadService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
    private TradeAnnualTestsReadService tradeAnnualTestsReadService;

    @Mock
    private VehicleResponseMapperFactory vehicleResponseMapperFactory;

    @Mock
    private SearchVehicleResponseMapperFactory searchVehicleResponseMapperFactory;

    @Mock
    private VehicleV4ResponseMapper vehicleMapper;

    @Mock
    private SearchVehicleV6ResponseMapper cvsV6VehicleMapper;

    @Mock
    private SearchVehicleV7ResponseMapper cvsV7VehicleMapper;

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
    private Response createHandlerAndGetTradeMotTests(TradeServiceRequest request)
            throws TradeException, ParamObfuscator.ObfuscationException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);
        sut.setTradeAnnualTestReadService(tradeAnnualTestsReadService);
        sut.setVehicleResponseMapperFactory(vehicleResponseMapperFactory);

        String obfuscatedVehicleId = null;
        if (request.getVehicleId() != null) {
            System.setProperty(ConfigKeys.ObfuscationSecret, "BbV[`8d7zQnc:?}\"CSz$L0t+(3r:_uT$");
            obfuscatedVehicleId = ParamObfuscator.obfuscate(request.getVehicleId().toString());
        }

        return sut.getTradeMotTests(obfuscatedVehicleId, request.getNumber(),
                request.getRegistration(), request.getDate(), request.getPage(), requestContext);
    }

    /**
     * Convenience function for testing getTradeAnnualTests calls with less boilerplate.
     *
     * @param request The request to test against
     * @return The result of the call to getTradeAnnualTests
     * @throws TradeException if getTradeAnnualTests throws
     */
    private Response createHandlerAndGetTradeAnnualTestsRegistrationsEndpoint(TradeServiceRequest request)
            throws TradeException {

        TradeServiceRequestHandler sut = getTradeServiceRequestHandler();

        return sut.getTradeAnnualTests(request.getAnnualTestsRegistrations(), null, requestContext);
    }

    /**
     * Convenience function for testing getTradeAnnualTests calls with less boilerplate.
     *
     * @param request The request to test against
     * @return The result of the call to getTradeAnnualTests
     * @throws TradeException if getTradeAnnualTests throws
     */
    private Response createHandlerAndGetTradeAnnualTestsRegistrationsOrVinsEndpoint(TradeServiceRequest request)
            throws TradeException {

        TradeServiceRequestHandler sut = getTradeServiceRequestHandler();

        return sut.getTradeAnnualTests(null, request.getAnnualTestsRegistrations(), requestContext);
    }

    /**
     * Convenience function for testing getTradeMotTestsLegacy calls with less boilerplate
     *
     * @param registration is a path parameter passed to getTradeMotTestsLegacy
     * @param make         is a path parameter passed to getTradeMotTestsLegacy
     * @return the return value of getTradeMotTestsLegacy
     * @throws TradeException if getTradeMotTestsLegacy throws
     */
    private Response createHandlerAndGetLegacy(String registration, String make) throws TradeException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);
        sut.setTradeAnnualTestReadService(tradeAnnualTestsReadService);
        sut.setVehicleResponseMapperFactory(vehicleResponseMapperFactory);

        return sut.getTradeMotTestsLegacy(registration, make, requestContext);
    }


    /**
     * Retrieve date for query parameter that is within 5 weeks of current date
     *
     * @return string value 7 days prior to the current day
     */
    private String getUsableDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    @Before
    public void setup() {
        System.setProperty(ConfigKeys.AnnualTestsMaxQueryableVehicleIdentifiers, "10");
        when(vehicleResponseMapperFactory.getMapper(any())).thenReturn(vehicleMapper);
        when(searchVehicleResponseMapperFactory.getMapper(null)).thenReturn(cvsV6VehicleMapper);
        when(searchVehicleResponseMapperFactory.getMapper("v6")).thenReturn(cvsV6VehicleMapper);
        when(searchVehicleResponseMapperFactory.getMapper("v7")).thenReturn(cvsV7VehicleMapper);
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
        sut.setTradeAnnualTestReadService(tradeAnnualTestsReadService);

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
        sut.setTradeAnnualTestReadService(tradeAnnualTestsReadService);

        sut.getMakes(null, lambdaContext);
    }

    /**
     * Really unexpected conditions should give us an BadRequestException.
     * <p>
     * Here we pass in a null request, which will result in a NullPointerException which gets caught and converted.
     */
    @Test(expected = BadRequestException.class)
    public void getTradeMotTests_NullRequest_Throws() throws TradeException, ParamObfuscator.ObfuscationException {

        createHandlerAndGetTradeMotTests(new TradeServiceRequest());
    }

    /**
     * If we ask for a vehicle ID which doesn't exist we should get an InvalidResourceException.
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTests_VehicleId_VehicleDoesNotExist() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_VehicleId_VehicleExists() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_VehicleId_VehiclesExist() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_VehicleId_DatabaseThrows() throws TradeException, ParamObfuscator.ObfuscationException {

        final int vehicleId = 4;
        when(tradeReadService.getVehiclesByVehicleId(vehicleId)).thenThrow(new IndexOutOfBoundsException());
        request.setVehicleId(vehicleId);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * If we provide only the make, this should result in a BadRequestException as Registration has to come with Make
     */
    @Test(expected = BadRequestException.class)
    public void getTradeMotTests_Make_BadRequest() throws TradeException, ParamObfuscator.ObfuscationException {

        String make = "TESLA";

        request.setMake(make);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * If we ask for a registration which exist, we expect to find the vehicle
     */
    @Test
    public void getTradeMotTests_Registration_VehicleExists() throws TradeException, ParamObfuscator.ObfuscationException {

        final String registration = "AA44VBG";
        final Vehicle vehicle = new Vehicle();
        vehicle.setRegistration(registration);
        List<VehicleResponse> mappedVehicles = Arrays.asList(new VehicleResponse());
        mappedVehicles.get(0).setRegistration(vehicle.getRegistration());

        when(tradeReadService.getVehiclesByRegistration(registration)).thenReturn(Arrays.asList(vehicle));
        when(vehicleMapper.map(eq(Arrays.asList(vehicle)))).thenReturn(mappedVehicles);

        request.setRegistration(registration);

        List<?> retrievedVehicles = (List<?>) createHandlerAndGetTradeMotTests(request).getEntity();

        assertEquals("Should retrieve only one vehicle", 1, retrievedVehicles.size());
        assertEquals("Should pass through the vehicle from mapper", mappedVehicles, retrievedVehicles);
    }

    /**
     * If we ask for a registration and make which do not exist, we expect an InvalidResourceException
     */
    @Test(expected = InvalidResourceException.class)
    public void getTradeMotTests_RegistrationMake_VehicleDoesNotExist() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_RegistrationMake_DatabaseError() throws TradeException, ParamObfuscator.ObfuscationException {

        final String registration = "AA44VBG";

        when(tradeReadService.getVehiclesByRegistration(registration)).thenThrow(new ArithmeticException());

        request.setRegistration(registration);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * Unparseable dates cause internal server errors. This may change to a BadRequest in future.
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_Date_InvalidDate() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_Date_NothingForDate() throws TradeException, ParamObfuscator.ObfuscationException {

        final String dateString = this.getUsableDate();
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
    public void getTradeMotTests_Date_NothingForDateAndPage() throws TradeException, ParamObfuscator.ObfuscationException {

        final String dateString = this.getUsableDate();
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
    public void getTradeMotTests_Date_ReturnsVehicles() throws TradeException, ParamObfuscator.ObfuscationException {

        final String dateString = this.getUsableDate();
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
    public void getTradeMotTests_Date_DatabaseError() throws TradeException, ParamObfuscator.ObfuscationException {

        final String dateString = this.getUsableDate();
        final int page = 3;

        request.setDate(dateString);
        request.setPage(page);

        when(tradeReadService.getVehiclesByDatePage(any(Date.class), eq(page))).thenThrow(new ArithmeticException());

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * When the date is out of the accepted range and throws, expect a BadRequestException
     */
    @Test(expected = BadRequestException.class)
    public void getTradeMotTests_Date_BadRequestError() throws TradeException, ParamObfuscator.ObfuscationException {

        final String dateString = "20140101";
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
    public void getTradeMotTests_Page_ReturnsVehicles() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_ParsesApiVersionCorrectly() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_WorksAsV1WhenNoApiVersionSpecified() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_WorksAsV1WhenHeadersAreNotSet() throws TradeException, ParamObfuscator.ObfuscationException {

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
    public void getTradeMotTests_Page_EmptyPage() throws TradeException, ParamObfuscator.ObfuscationException {

        final int page = 3;

        request.setPage(page);

        when(tradeReadService.getVehiclesByPage(page)).thenReturn(null);

        createHandlerAndGetTradeMotTests(request);
    }

    /**
     * When the database call throws, we should get an InternalServerErrorException
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_Page_DatabaseThrows() throws TradeException, ParamObfuscator.ObfuscationException {

        final int page = 3;

        request.setPage(page);

        when(tradeReadService.getVehiclesByPage(page)).thenThrow(new IndexOutOfBoundsException());

        createHandlerAndGetTradeMotTests(request);
    }

    @Test(expected = BadRequestException.class)
    public void getTradeAnnualTests_Registrations_UndefinedThrowsBadRequestException() throws TradeException {

        createHandlerAndGetTradeAnnualTestsRegistrationsEndpoint(request);
    }

    @Test(expected = BadRequestException.class)
    public void getTradeAnnualTests_Registrations_EmptyThrowsBadRequestException() throws TradeException {

        final String registrations = "";

        request.setAnnualTestsRegistrations(registrations);

        createHandlerAndGetTradeAnnualTestsRegistrationsEndpoint(request);
    }

    @Test(expected = BadRequestException.class)
    public void getTradeAnnualTests_Registrations_MoreThan50DefinedThrowsBadRequestException() throws TradeException {

        final String registrations = "" +
                "REG001, REG002, REG003, REG004, REG005, REG006, REG007, REG008, REG009, REG010, " +
                "REG011, REG012, REG013, REG014, REG015, REG016, REG017, REG018, REG019, REG020, " +
                "REG021, REG022, REG023, REG024, REG025, REG026, REG027, REG028, REG029, REG030, " +
                "REG031, REG032, REG033, REG034, REG035, REG036, REG037, REG038, REG039, REG040, " +
                "REG041, REG042, REG043, REG044, REG045, REG046, REG047, REG048, REG049, REG050, " +
                "REG051";

        request.setAnnualTestsRegistrations(registrations);

        createHandlerAndGetTradeAnnualTestsRegistrationsEndpoint(request);
    }

    @Test()
    public void getTradeAnnualTests_Registrations_DefiningOneRegistrationReturnsOneVehicle() throws Exception {

        final String registrations = "REG001, REG002";

        request.setAnnualTestsRegistrations(registrations);

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG001");

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle1 = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG002");

        when(tradeAnnualTestsReadService.getAnnualTests(any())).thenReturn(Arrays.asList(vehicle, vehicle1));

        createHandlerAndGetTradeAnnualTestsRegistrationsEndpoint(request);
    }

    @Test()
    public void getTradeAnnualTests_Registrations_DefiningOneRegistrationReturnsOneVehicleV7() throws Exception {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("Accept", Collections.singletonList("application/json+v7"));

        final String registrations = "REG001, REG002";

        request.setAnnualTestsRegistrations(registrations);

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG001");

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle1 = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG002");

        when(requestContext.getHeaders()).thenReturn(headers);
        when(tradeAnnualTestsReadService.getAnnualTests(any())).thenReturn(Arrays.asList(vehicle, vehicle1));

        createHandlerAndGetTradeAnnualTestsRegistrationsOrVinsEndpoint(request);
    }

    @Test()
    public void getTradeAnnualTests_WithNoApiVersionUsesV6Mapper() throws Exception {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("Accept", Collections.singletonList("application/json"));

        final String registrations = "REG001, REG002";

        request.setAnnualTestsRegistrations(registrations);

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG001");

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle1 = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG002");

        List<uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle> vehicleList = Arrays.asList(vehicle, vehicle1);

        when(requestContext.getHeaders()).thenReturn(headers);
        when(tradeAnnualTestsReadService.getAnnualTests(any())).thenReturn(vehicleList);

        createHandlerAndGetTradeAnnualTestsRegistrationsEndpoint(request);

        verify(cvsV6VehicleMapper).map(vehicleList);
    }

    @Test()
    public void getTradeAnnualTests_ApiVersionV6VersionUsesV6Mapper() throws Exception {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("Accept", Collections.singletonList("application/json+v6"));

        final String registrations = "REG001, REG002";

        request.setAnnualTestsRegistrations(registrations);

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG001");

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle1 = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG002");

        List<uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle> vehicleList = Arrays.asList(vehicle, vehicle1);

        when(requestContext.getHeaders()).thenReturn(headers);
        when(tradeAnnualTestsReadService.getAnnualTests(any())).thenReturn(vehicleList);

        createHandlerAndGetTradeAnnualTestsRegistrationsEndpoint(request);

        verify(cvsV6VehicleMapper).map(vehicleList);
    }

    @Test()
    public void getTradeAnnualTests_ApiVersionV7VersionUsesV7Mapper() throws Exception {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("Accept", Collections.singletonList("application/json+v7"));

        final String registrations = "REG001, REG002";

        request.setAnnualTestsRegistrations(registrations);

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG001");

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle1 = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("REG002");

        List<uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle> vehicleList = Arrays.asList(vehicle, vehicle1);

        when(requestContext.getHeaders()).thenReturn(headers);
        when(tradeAnnualTestsReadService.getAnnualTests(any())).thenReturn(vehicleList);

        createHandlerAndGetTradeAnnualTestsRegistrationsOrVinsEndpoint(request);

        verify(cvsV7VehicleMapper).map(vehicleList);
    }

    @Test(expected = ServiceTemporarilyUnavailableException.class)
    public void getTradeAnnualTests_Registrations_ReturnsServiceTemporarilyUnavailableWhenEndpointIsDisabled() throws TradeException {

        System.setProperty(ConfigKeys.AnnualTestsMaxQueryableVehicleIdentifiers, "0");

        final String registrations = "REG001, REG002";

        request.setAnnualTestsRegistrations(registrations);

        createHandlerAndGetTradeAnnualTestsRegistrationsEndpoint(request);
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

    private TradeServiceRequestHandler getTradeServiceRequestHandler() {
        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);
        sut.setTradeAnnualTestReadService(tradeAnnualTestsReadService);
        sut.setHgvResponseMapperFactory(searchVehicleResponseMapperFactory);
        return sut;
    }

    @DataProvider
    public static Object[][] dataProviderForMapperVersions() {
        return new Object[][]{
                {"+v6", "v6"},
                {"+v7", "v7"},
        };
    }
}
