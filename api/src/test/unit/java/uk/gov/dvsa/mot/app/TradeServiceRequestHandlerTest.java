package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.read.core.TradeReadService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class TradeServiceRequestHandlerTest {
    @Mock
    private TradeReadService tradeReadService;

    @Mock
    private Context lambdaContext;

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
    private List<Vehicle> createHandlerAndGetTradeMotTests(TradeServiceRequest request) throws TradeException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);

        return sut.getTradeMotTests(request, lambdaContext);
    }

    /**
     * Convenience function for testing getLatestMotTest calls with less boilerplate
     *
     * @param request the request to pass to getLatestMotTest
     * @return The return value of getLatestMotTest
     * @throws TradeException if getLatestMotTest throws
     */
    private Vehicle createHandlerAndGetLatestMotTest(TradeServiceRequest request) throws TradeException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);

        return sut.getLatestMotTest(request, lambdaContext);
    }

    /**
     * Convenience function for testing getLatestMotTestByMotTestNumber calls with less boilerplate
     *
     * @param request the request to pass to getLatestMotTestByMotTestNumber
     * @return The return value of getLatestMotTestByMotTestNumber
     * @throws TradeException if getLatestMotTestByMotTestNumber throws
     */
    private Vehicle createHandlerAndGetLatestMotTestByMotTestNumber(TradeServiceRequest request) throws TradeException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);

        return sut.getLatestMotTestByMotTestNumber(request, lambdaContext);
    }

    /**
     * Convenience function for testing getLatestMotTestByDvlaVehicleId calls with less boilerplate
     *
     * @param request the request to pass to getLatestMotTestByDvlaVehicleId
     * @return The return value of getLatestMotTestByDvlaVehicleId
     * @throws TradeException if getLatestMotTestByDvlaVehicleId throws
     */
    private Vehicle createHandlerAndGetLatestMotTestByDvlaVehicleId(TradeServiceRequest request) throws TradeException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);

        return sut.getLatestMotTestByDvlaVehicleId(request, lambdaContext);
    }

    /**
     * Convenience function for testing getTradeMotTestsLegacy calls with less boilerplate
     *
     * @param request the request to pass to getTradeMotTestsLegacy
     * @return the return value of getTradeMotTestsLegacy
     * @throws TradeException if getTradeMotTestsLegacy throws
     */
    private List<DisplayMotTestItem> createHandlerAndGetLegacy(TradeServiceRequest request) throws TradeException {

        TradeServiceRequestHandler sut = new TradeServiceRequestHandler(false);
        sut.setTradeReadService(tradeReadService);

        return sut.getTradeMotTestsLegacy(request, lambdaContext);
    }

    @Before
    public void setup() {

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
     * Really unexpected conditions should give us an InternalServerErrorException.
     * <p>
     * Here we pass in a null request, which will result in a NullPointerException which gets caught and converted.
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTests_NullRequest_Throws() throws TradeException {

        createHandlerAndGetTradeMotTests(null);
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
     * If we ask for a vehicle ID which does exist we should get back the vehicle with that ID
     */
    @Test
    public void getTradeMotTests_VehicleId_VehicleExists() throws TradeException {

        Vehicle vehicle = new Vehicle();
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle);
        final int vehicleId = 4;
        when(tradeReadService.getVehiclesByVehicleId(vehicleId)).thenReturn(vehicles);

        request.setVehicleId(vehicleId);

        List<Vehicle> receivedVehicles = createHandlerAndGetTradeMotTests(request);

        assertEquals(vehicles, receivedVehicles);
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
        final int vehicleId = 4;
        when(tradeReadService.getVehiclesByVehicleId(vehicleId)).thenReturn(vehicles);

        request.setVehicleId(vehicleId);

        List<Vehicle> receivedVehicles = createHandlerAndGetTradeMotTests(request);

        assertEquals(vehicles, receivedVehicles);
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

        Vehicle vehicle = new Vehicle();
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle);
        final long motNumber = 233432;
        when(tradeReadService.getVehiclesMotTestsByMotTestNumber(motNumber)).thenReturn(vehicles);

        request.setNumber(motNumber);

        List<Vehicle> receivedVehicles = createHandlerAndGetTradeMotTests(request);

        assertEquals(vehicles, receivedVehicles);
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

        when(tradeReadService.getVehiclesByRegistration(registration)).thenReturn(Arrays.asList(vehicle));

        request.setRegistration(registration);

        List<Vehicle> retrievedVehicles = createHandlerAndGetTradeMotTests(request);

        assertEquals("Should retrieve only one vehicle", 1, retrievedVehicles.size());
        assertEquals("Should pass through the vehicle from the DB layer", vehicle, retrievedVehicles.get(0));
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
     * We should get an InvalidResourceException when we request a date/page that has nothing in it
     * <p>
     * This variant of the test does not explicitly set the page
     */
    @Test(expected = InvalidResourceException.class)
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

        request.setDate(dateString);
        request.setPage(page);

        when(tradeReadService.getVehiclesByDatePage(any(Date.class), eq(page))).thenReturn(Arrays.asList(vehicle1, vehicle2));

        List<Vehicle> returnedVehicles = createHandlerAndGetTradeMotTests(request);

        assertEquals(Arrays.asList(vehicle1, vehicle2), returnedVehicles);
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

        request.setPage(page);

        when(tradeReadService.getVehiclesByPage(page)).thenReturn(Arrays.asList(vehicle1, vehicle2));

        List<Vehicle> returnedVehicles = createHandlerAndGetTradeMotTests(request);

        assertEquals(Arrays.asList(vehicle1, vehicle2), returnedVehicles);
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
     * If the registration in the request is null, we expect a BadRequestException
     */
    @Test(expected = BadRequestException.class)
    public void getLatestMotTest_NullRegistration() throws TradeException {

        request.setRegistration(null);

        createHandlerAndGetLatestMotTest(request);
    }

    /**
     * If the request is null we expect an InternalServerErrorException
     */
    @Test(expected = InternalServerErrorException.class)
    public void getLatestMotTest_NullRequest() throws TradeException {

        createHandlerAndGetLatestMotTest(null);
    }

    /**
     * When you ask for a registration which doesn't have any MOTs you should get an InvalidResourceException
     */
    @Test(expected = InvalidResourceException.class)
    public void getLatestMotTest_InvalidRegistration() throws TradeException {

        final String registration = "NOTACAR";
        request.getPathParams().setRegistration(registration);

        when(tradeReadService.getLatestMotTestByRegistration(registration)).thenReturn(null);

        createHandlerAndGetLatestMotTest(request);
    }

    /**
     * If asked for a vehicle which does exist we expect to receive that vehicle.
     */
    @Test
    public void getLatestMotTest_ValidRegistration() throws TradeException {

        final String registration = "XX89UIP";
        request.getPathParams().setRegistration(registration);

        final Vehicle vehicle = new Vehicle();
        vehicle.setRegistration(registration);

        when(tradeReadService.getLatestMotTestByRegistration(registration)).thenReturn(vehicle);

        Vehicle receivedVehicle = createHandlerAndGetLatestMotTest(request);

        assertEquals(vehicle, receivedVehicle);
    }

    /**
     * If we don't provide an MOT number we should get an BadRequestException.
     */
    @Test(expected = BadRequestException.class)
    public void getLatestMotTestByMotTestNumber_IncorrectParamsProvided() throws TradeException {

        createHandlerAndGetLatestMotTestByMotTestNumber(request);
    }

    /**
     * When you ask for a vehicle which doesn't have any MOTs you should get an InvalidResourceException
     */
    @Test(expected = InvalidResourceException.class)
    public void getLatestMotTestByMotTestNumber_VehicleHasNoMot() throws TradeException {

        final long motNumber = 42;
        request.getPathParams().setNumber(motNumber);

        when(tradeReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motNumber)).thenReturn(null);

        createHandlerAndGetLatestMotTestByMotTestNumber(request);
    }

    /**
     * If asked for a vehicle which does exist and has a valid MOT we expect to receive that vehicle.
     */
    @Test
    public void getLatestMotTestByMotTestNumber_ValidRegistrationAndMot() throws TradeException {

        final long motNumber = 42;
        request.getPathParams().setNumber(motNumber);

        final String registration = "XX89UIP";
        request.getPathParams().setRegistration(registration);

        final Vehicle vehicle = new Vehicle();
        vehicle.setRegistration(registration);

        when(tradeReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motNumber)).thenReturn(vehicle);

        Vehicle receivedVehicle = createHandlerAndGetLatestMotTestByMotTestNumber(request);

        assertEquals(vehicle, receivedVehicle);
    }

    /**
     * If we don't provide an MOT number we should get an BadRequestException.
     */
    @Test(expected = BadRequestException.class)
    public void getLatestMotTestByDvlaVehicleId_IncorrectParamsProvided() throws TradeException {

        createHandlerAndGetLatestMotTestByDvlaVehicleId(request);
    }

    /**
     * When you ask for a DVLA vehicle which doesn't exist you should get an InvalidResourceException
     */
    @Test(expected = InvalidResourceException.class)
    public void getLatestMotTestByDvlaVehicleId_VehicleDoesNotExist() throws TradeException {

        final long dvlaVehicleId = 42;
        request.getPathParams().setDvlaId(dvlaVehicleId);

        when(tradeReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId)).thenReturn(null);

        createHandlerAndGetLatestMotTestByDvlaVehicleId(request);
    }

    /**
     * If asked for a DVLA vehicle which does exist and does not have a valid MOT we expect to
     * receive that vehicle's DVLA ID details and calculated expiry date
     */
    @Test
    public void getLatestMotTestByDvlaVehicleId_ValidDvlaVehicleAndNoMot() throws TradeException {

        final long dvlaVehicleId = 42;
        request.getPathParams().setDvlaId(dvlaVehicleId);

        final String registration = "XX89UIP";
        final Vehicle vehicle = new Vehicle();
        vehicle.setRegistration(registration);
        vehicle.setDvlaId(Long.toString(dvlaVehicleId));

        when(tradeReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId)).thenReturn(vehicle);

        Vehicle receivedVehicle = createHandlerAndGetLatestMotTestByDvlaVehicleId(request);

        assertEquals(vehicle, receivedVehicle);
    }

    /**
     * If asked for a DVLA vehicle which does exist and has a valid MOT we expect to receive that vehicle's MOT details
     * and no dvlaID
     */
    @Test
    public void getLatestMotTestByMotTestNumber_ValidDvlaVehicleWithPassedMot() throws TradeException {

        final long dvlaVehicleId = 42;
        request.getPathParams().setDvlaId(dvlaVehicleId);

        final String registration = "XX89UIP";
        final String motNumber = "42";
        final Vehicle vehicle = new Vehicle();
        vehicle.setRegistration(registration);
        vehicle.setMotTestNumber(motNumber);

        when(tradeReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId)).thenReturn(vehicle);

        Vehicle receivedVehicle = createHandlerAndGetLatestMotTestByDvlaVehicleId(request);

        assertEquals(vehicle, receivedVehicle);
        assertNull(receivedVehicle.getDvlaId());
    }

    /**
     * A null request should get us an InternalServerErrorException
     *
     * @throws TradeException always if the test is working correctly
     */
    @Test(expected = InternalServerErrorException.class)
    public void getTradeMotTestsLegacy_NullRequest() throws TradeException {

        createHandlerAndGetLegacy(null);
    }

    /**
     * A null registration should get us a BadRequestException
     *
     * @throws TradeException always
     */
    @Test(expected = BadRequestException.class)
    public void getTradeMotTestsLegacy_NullRegistration() throws TradeException {

        request.getPathParams().setRegistration(null);
        createHandlerAndGetLegacy(request);
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
        request.getPathParams().setRegistration(registration);
        request.getPathParams().setMake(make);

        when(tradeReadService.getMotTestsByRegistrationAndMake(registration, make)).thenReturn(Arrays.asList());

        createHandlerAndGetLegacy(request);
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

        request.getPathParams().setRegistration(registration);
        request.getPathParams().setMake(make);

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

        List<DisplayMotTestItem> receivedTests = createHandlerAndGetLegacy(request);

        assertEquals(tests, receivedTests);
    }
}