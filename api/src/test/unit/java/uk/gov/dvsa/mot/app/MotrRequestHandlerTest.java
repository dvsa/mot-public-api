package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.MotrResponse;
import uk.gov.dvsa.mot.trade.read.core.MotrReadService;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasNoCause;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class MotrRequestHandlerTest {

    private static final String REGISTRATION = "REG123456";
    private static final int DVLA_VEHICLE_ID = 213;
    private static final long MOT_TEST_NUMBER = 12345L;

    @Mock
    private MotrReadService motrReadService;

    @Mock
    private ContainerRequestContext containerRequestContext;

    @Mock
    private ApiGatewayRequestContext context;

    @Mock
    private HgvVehicleProvider hgvVehicleProvider;

    private MotrRequestHandler motrRequestHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(context.getRequestId()).thenReturn("1");
        when(containerRequestContext.getProperty(anyString())).thenReturn(context);

        motrRequestHandler = new MotrRequestHandler(false);
        motrRequestHandler.setHgvVehicleProvider(hgvVehicleProvider);
        motrRequestHandler.setMotrReadService(motrReadService);
    }

    // Tests for getVehicle method

    @Test
    public void getVehicle_WithoutRegistrationParam_ShouldThrowException() throws Exception {
        catchException(motrRequestHandler).getVehicle("", containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No MOT Test or DVLA vehicle found for registration")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenVehicleReadServiceFindByRegistrationReturnsException_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenThrow(new IndexOutOfBoundsException());

        catchException(motrRequestHandler).getVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("There was an error retrieving mot vehicle")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenVehicleReadServiceGetDvlaVehicleByRegistrationWithVinReturnsException_ShouldThrowException()
            throws Exception {
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenThrow(new IndexOutOfBoundsException());

        catchException(motrRequestHandler).getVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("There was an error retrieving dvla vehicle")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenDvlaVehicleIsNotPresent_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(null);
        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(null);

        catchException(motrRequestHandler).getVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No MOT Test or DVLA vehicle found for registration")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenGetHgvPsvVehicleNotReturnVehicle_ShouldThrowException() throws Exception {
        MotrResponse dvlaVehicle = defaultMotrResponse(REGISTRATION);

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(null);
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(null);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        MotrResponse expectedVehicle = createResponseVehicle("MOT", "2018-01-01");
        MotrResponse actualVehicle = (MotrResponse) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsNotEmpty_AnnualTestExpiryDateShouldBeSetToTestDate() throws Exception {
        TestHistory[] testHistory = new TestHistory[1];
        TestHistory historyItem =  new TestHistory();
        testHistory[0] = historyItem;

        MotrResponse dvlaVehicle = defaultMotrResponse(REGISTRATION);
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("HGV");
        vehicle.setTestHistory(testHistory);
        vehicle.setTestCertificateExpiryDate("01/01/2018");

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(null);
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        MotrResponse expectedVehicle = createResponseVehicle("HGV", "2018-01-01");
        MotrResponse actualVehicle = (MotrResponse) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsNotEmpty_MotTestNumberShouldBeSet() throws Exception {
        TestHistory[] testHistory = new TestHistory[1];
        TestHistory historyItem =  new TestHistory();
        historyItem.setTestCertificateSerialNo("SERIAL");
        testHistory[0] = historyItem;

        MotrResponse dvlaVehicle = defaultMotrResponse(REGISTRATION);
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("PSV");
        vehicle.setTestHistory(testHistory);
        vehicle.setTestCertificateExpiryDate("01/01/2018");

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(null);
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        MotrResponse expectedVehicle = createResponseVehicle("PSV", "2018-01-01");
        expectedVehicle.setMotTestNumber("SERIAL");
        MotrResponse actualVehicle = (MotrResponse) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    @Test
    public void getVehicle_whenHgvPsvExpiryDateIsUnknown_returnVehicleWithNullExpiry() throws Exception {
        MotrResponse dvlaVehicle = defaultMotrResponse(REGISTRATION);
        dvlaVehicle.setMotTestExpiryDate(null);

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("PSV");

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(null);
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        MotrResponse expectedVehicle = createResponseVehicle("PSV", null);
        MotrResponse actualVehicle = (MotrResponse) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsEmptyAndVehicleIsHgv_AnnualTestExpiryDateShouldBeSetProperly() throws Exception {
        MotrResponse dvlaVehicle = defaultMotrResponse(REGISTRATION);
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("HGV");
        vehicle.setTestHistory(new TestHistory[0]);
        vehicle.setRegistrationDate("05/01/2015");

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(null);
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        MotrResponse expectedVehicle = createResponseVehicle("HGV", "2016-01-31");
        MotrResponse actualVehicle = (MotrResponse) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    @Test
    public void getVehicle_WhenSingleMotVehicleIsPresent_BodyIsCorrect() throws Exception {
        MotrResponse motrResponse = defaultMotrResponse("VIN123451");

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(motrResponse);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        MotrResponse actualVehicles = (MotrResponse) response.getEntity();
        checkIfMotVehicleIsCorrect(motrResponse, actualVehicles);
    }

    @Test
    public void getVehicle_WhenTwoMotVehicleIsPresent_BodyIsCorrect() throws Exception {
        MotrResponse motrResponse = defaultMotrResponse("VIN12123");
        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(motrResponse);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        MotrResponse actualVehicles = (MotrResponse) response.getEntity();
        checkIfMotVehicleIsCorrect(motrResponse, actualVehicles);
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsEmptyAndVehicleIsPsv_AnnualTestExpiryDateShouldBeSetProperly() throws Exception {
        MotrResponse dvlaVehicle = defaultMotrResponse(REGISTRATION);
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("PSV");
        vehicle.setTestHistory(new TestHistory[0]);
        vehicle.setRegistrationDate("05/02/2015");

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(null);
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        MotrResponse expectedVehicle = createResponseVehicle("PSV", "2016-02-05");
        MotrResponse actualVehicle = (MotrResponse) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    // Tests for getCommercialVehicle method

    @Test
    public void getCommercialVehicle_WhenVehicleReadServiceFindByRegistrationReturnsException_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenThrow(new IndexOutOfBoundsException());

        catchException(motrRequestHandler).getCommercialVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("There was an error retrieving dvla vehicle")),
                hasNoCause()
        ));
    }

    @Test
    public void getCommercialVehicle_WhenVehicleReadServiceFindByRegistrationReturnsNull_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(null);

        catchException(motrRequestHandler).getCommercialVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No DVLA vehicle found for registration " + REGISTRATION)),
                hasNoCause()
        ));
    }

    @Test
    public void getCommercialVehicle_WhenHgvVehicleProviderThrowsException_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(defaultMotrResponse(REGISTRATION));
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenThrow(new Exception());

        catchException(motrRequestHandler).getCommercialVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InternalServerErrorException.class),
                hasMessageThat(containsString("There was an error retrieving the HGV/PSV history")),
                hasNoCause()
        ));
    }

    @Test
    public void getCommercialVehicle_WhenHgvVehicleProviderReturnsNull_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(defaultMotrResponse(REGISTRATION));
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(null);

        catchException(motrRequestHandler).getCommercialVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No HGV/PSV vehicle found for registration " + REGISTRATION)),
                hasNoCause()
        ));
    }

    @Test
    public void getCommercialVehicle_WhenHgvVehicleProviderReturnsVehicleWithoutTestHistory_ResponseShouldBeCorrect() throws Exception {
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("HGV");
        vehicle.setRegistrationDate("05/01/2015");

        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(defaultMotrResponse(REGISTRATION));
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getCommercialVehicle(REGISTRATION, containerRequestContext);

        assertEquals(response.getStatus(), 200);

        MotrResponse expectedVehicle = createResponseVehicle("HGV", "2016-01-31");
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, (MotrResponse) response.getEntity());
    }

    @Test
    public void getCommercialVehicle_WhenHgvVehicleProviderReturnsVehicleWithTestHistory_MotTestNumberShouldBeSet() throws Exception {
        TestHistory[] testHistory = new TestHistory[1];
        TestHistory historyItem =  new TestHistory();
        historyItem.setTestCertificateSerialNo("SERIAL");
        testHistory[0] = historyItem;

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("HGV");
        vehicle.setRegistrationDate("05/01/2015");
        vehicle.setTestHistory(testHistory);

        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(defaultMotrResponse(REGISTRATION));
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getCommercialVehicle(REGISTRATION, containerRequestContext);

        assertEquals(response.getStatus(), 200);

        MotrResponse expectedVehicle = createResponseVehicle("HGV", "2016-01-31");
        expectedVehicle.setMotTestNumber("SERIAL");
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, (MotrResponse) response.getEntity());
    }

    @Test
    public void getCommercialVehicle_forTrailer() throws Exception {
        String trailerRegistration = "A123456";
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("TRAILER");
        when(hgvVehicleProvider.getVehicle(eq(trailerRegistration))).thenReturn(vehicle);

        Response response = motrRequestHandler.getCommercialVehicle(trailerRegistration, containerRequestContext);

        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
    }

    // Tests for getVehicleByDvlaId method

    @Test
    public void getVehicleByDvlaId_WhenDvlaVehicleIdIsNull_ShouldThrowException() throws Exception {
        catchException(motrRequestHandler).getVehicleByDvlaId(null, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(BadRequestException.class),
                hasMessageThat(containsString("Invalid Parameters")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicleByDvlaId_WhenMotrReadServiceReturnsNull_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestByDvlaVehicleId(DVLA_VEHICLE_ID)).thenReturn(null);

        catchException(motrRequestHandler).getVehicleByDvlaId(DVLA_VEHICLE_ID, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No MOT Test or DVLA vehicle found for DVLA vehicle id " + DVLA_VEHICLE_ID)),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicleByDvlaId_WhenMotrReadServiceReturnsVehicle_ShouldReturnCorrectResponse() throws Exception {
        when(motrReadService.getLatestMotTestByDvlaVehicleId(DVLA_VEHICLE_ID)).thenReturn(defaultMotrResponse(REGISTRATION));

        Response response = motrRequestHandler.getVehicleByDvlaId(DVLA_VEHICLE_ID, containerRequestContext);

        assertEquals(response.getStatus(), 200);

        MotrResponse expectedVehicle = createResponseVehicle("MOT", "2018-01-01");
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, (MotrResponse) response.getEntity());
    }

    // Tests for getVehicleByMotTestNumber method

    @Test
    public void getVehicleByMotTestNumber_WhenMotTestNumberIsNull_ShouldThrowException() throws Exception {
        catchException(motrRequestHandler).getVehicleByMotTestNumber(null, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(BadRequestException.class),
                hasMessageThat(containsString("Invalid Parameters")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicleByMotTestNumber_WhenMotrReadServiceReturnsNull_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(MOT_TEST_NUMBER)).thenReturn(null);

        catchException(motrRequestHandler).getVehicleByMotTestNumber(MOT_TEST_NUMBER, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No MOT Tests found with number: " + Long.toString(MOT_TEST_NUMBER))),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicleByMotTestNumber_WhenMotrReadServiceReturnsVehicle_ShouldReturnCorrectResponse() throws Exception {
        when(motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(MOT_TEST_NUMBER))
                .thenReturn(defaultMotrResponse(REGISTRATION));

        Response response = motrRequestHandler.getVehicleByMotTestNumber(MOT_TEST_NUMBER, containerRequestContext);

        assertEquals(response.getStatus(), 200);

        MotrResponse expectedVehicle = createResponseVehicle("MOT", "2018-01-01");
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, (MotrResponse) response.getEntity());
    }

    private uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle createVehicle(String vehicleType) {
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();

        vehicle.setVehicleIdentifier(REGISTRATION);
        vehicle.setMake("MAKE");
        vehicle.setModel("MODEL");
        vehicle.setVehicleType(vehicleType);
        vehicle.setYearOfManufacture(2013);

        return vehicle;
    }

    private MotrResponse createResponseVehicle(String vehicleType, String testExpiryDate) {
        MotrResponse hgvPsvVehicle = new MotrResponse();

        hgvPsvVehicle.setMake("MAKE");
        hgvPsvVehicle.setModel("MODEL");
        hgvPsvVehicle.setRegistration(REGISTRATION);
        hgvPsvVehicle.setVehicleType(vehicleType);
        hgvPsvVehicle.setManufactureYear("2013");
        hgvPsvVehicle.setDvlaId("213");
        hgvPsvVehicle.setMotTestExpiryDate(testExpiryDate);

        return hgvPsvVehicle;
    }

    private void checkIfHgvPsvVehicleIsCorrect(MotrResponse expectedVehicle, MotrResponse actualVehicle) {
        assertEquals(expectedVehicle.getMake(), actualVehicle.getMake());
        assertEquals(expectedVehicle.getManufactureYear(), actualVehicle.getManufactureYear());
        assertEquals(expectedVehicle.getModel(), actualVehicle.getModel());
        assertEquals(expectedVehicle.getMotTestExpiryDate(), actualVehicle.getMotTestExpiryDate());
        assertEquals(expectedVehicle.getRegistration(), actualVehicle.getRegistration());
        assertEquals(expectedVehicle.getVehicleType(), actualVehicle.getVehicleType());
        assertEquals(expectedVehicle.getDvlaId(), actualVehicle.getDvlaId());

        if (expectedVehicle.getMotTestNumber() != null) {
            assertEquals(expectedVehicle.getMotTestNumber(), actualVehicle.getMotTestNumber());
        }
    }

    private void checkIfMotVehicleIsCorrect(MotrResponse expectedVehicle, MotrResponse actualVehicle) {
        assertEquals(actualVehicle.getRegistration(), expectedVehicle.getRegistration());
        assertSame(actualVehicle.getDvlaId(), expectedVehicle.getDvlaId());
    }

    private MotrResponse defaultMotrResponse(String registration) {
        MotrResponse motrResponse = new MotrResponse();

        motrResponse.setRegistration(registration);
        motrResponse.setMake("MAKE");
        motrResponse.setModel("MODEL");
        motrResponse.setManufactureYear("2013");
        motrResponse.setMotTestExpiryDate("2018-01-01");
        motrResponse.setDvlaId("213");

        return motrResponse;
    }
}
