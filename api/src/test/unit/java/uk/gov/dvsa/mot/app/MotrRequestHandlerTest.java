package uk.gov.dvsa.mot.app;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.persist.model.ColourLookup;
import uk.gov.dvsa.mot.persist.model.DvlaVehicle;
import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.HgvPsvVehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasNoCause;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class MotrRequestHandlerTest {

    private static final String AWS_REQUEST_ID = "123456";
    private static final String REGISTRATION = "REG123456";

    @Mock
    private VehicleReadService vehicleReadService;

    @Mock
    private HgvVehicleProvider hgvVehicleProvider;

    private MotrRequestHandler motrRequestHandler;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        motrRequestHandler = new MotrRequestHandler(false);
        motrRequestHandler.setHgvVehicleProvider(hgvVehicleProvider);
        motrRequestHandler.setVehicleReadService(vehicleReadService);
    }

    @Test
    public void getVehicle_WithoutRegistrationParam_ShouldThrowException() throws Exception {
        catchException(motrRequestHandler).getVehicle("");

        assertThat(caughtException(), allOf(
                instanceOf(BadRequestException.class),
                hasMessageThat(containsString("Registration param is missing")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenVehicleReadServiceFindByRegistrationReturnsException_ShouldThrowException() throws Exception {
        when(vehicleReadService.findByRegistration(REGISTRATION)).thenThrow(new IndexOutOfBoundsException());

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("There was an error retrieving mot vehicle")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenVehicleReadServiceGetDvlaVehicleByRegistrationWithVinReturnsException_ShouldThrowException()
            throws Exception {
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenThrow(new IndexOutOfBoundsException());

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("There was an error retrieving dvla vehicle")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenDvlaVehicleIsNotPresent_ShouldThrowException() throws Exception {
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(InternalServerErrorException.class),
                hasMessageThat(containsString("Vehicle is not HGV/PSV vehicle")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenGetHgvPsvVehicleNotReturnVehicle_ShouldThrowException() throws Exception {
        DvlaVehicle dvlaVehicle = new DvlaVehicle();

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(null);

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No HGV/PSV vehicle retrieved"))
        ));
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsNotEmpty_AnnualTestExpiryDateShouldBeSetToTestDate() throws Exception {
        TestHistory[] testHistory = new TestHistory[1];
        TestHistory historyItem =  new TestHistory();
        historyItem.setTestDate("2018-01-01");
        testHistory[0] = historyItem;

        DvlaVehicle dvlaVehicle = createDvlaVehicle();
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("HGV");
        vehicle.setTestHistory(testHistory);

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION);

        HgvPsvVehicle expectedVehicle = createResponseVehicle("HGV", "2018-01-01");
        HgvPsvVehicle actualVehicle = (HgvPsvVehicle) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsNotEmpty_MotTestNumberShouldBeSet() throws Exception {
        TestHistory[] testHistory = new TestHistory[1];
        TestHistory historyItem =  new TestHistory();
        historyItem.setTestDate("2018-01-01");
        historyItem.setTestCertificateSerialNo("SERIAL");
        testHistory[0] = historyItem;

        DvlaVehicle dvlaVehicle = createDvlaVehicle();
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("PSV");
        vehicle.setTestHistory(testHistory);

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION);

        HgvPsvVehicle expectedVehicle = createResponseVehicle("PSV", "2018-01-01");
        expectedVehicle.setMotTestNumber("SERIAL");
        HgvPsvVehicle actualVehicle = (HgvPsvVehicle) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsEmptyAndRegistrationDateIsNull_ShouldThrowException() throws Exception {
        DvlaVehicle dvlaVehicle = createDvlaVehicle();
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("HGV");
        vehicle.setTestHistory(new TestHistory[0]);

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(BadRequestException.class),
                hasMessageThat(containsString("Registration date is null or empty"))
        ));
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsEmptyAndVehicleIsHgv_AnnualTestExpiryDateShouldBeSetProperly() throws Exception {
        DvlaVehicle dvlaVehicle = createDvlaVehicle();
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("HGV");
        vehicle.setTestHistory(new TestHistory[0]);
        vehicle.setRegistrationDate("2015-01-05");

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION);

        HgvPsvVehicle expectedVehicle = createResponseVehicle("HGV", "2016-01-31");
        HgvPsvVehicle actualVehicle = (HgvPsvVehicle) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    @Test
    public void getVehicle_WhenSingleMotVehicleIsPresent_BodyIsCorrect() throws Exception {
        List<Vehicle> motVehicles = new ArrayList<>();
        motVehicles.add(createMotVehicle("VIN123451"));
        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(motVehicles);

        Response response = motrRequestHandler.getVehicle(REGISTRATION);

        List<uk.gov.dvsa.mot.vehicle.api.Vehicle> actualVehicles = (List<uk.gov.dvsa.mot.vehicle.api.Vehicle>) response.getEntity();
        checkIfMotVehicleIsCorrect(motVehicles.get(0), actualVehicles.get(0));
    }

    @Test
    public void getVehicle_WhenTwoMotVehicleIsPresent_BodyIsCorrect() throws Exception {
        List<Vehicle> motVehicles = new ArrayList<>();
        motVehicles.add(createMotVehicle("VIN123451"));
        motVehicles.add(createMotVehicle("VIN123452"));
        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(motVehicles);

        Response response = motrRequestHandler.getVehicle(REGISTRATION);

        List<uk.gov.dvsa.mot.vehicle.api.Vehicle> actualVehicles = (List<uk.gov.dvsa.mot.vehicle.api.Vehicle>) response.getEntity();
        checkIfMotVehicleIsCorrect(motVehicles.get(0), actualVehicles.get(0));
        checkIfMotVehicleIsCorrect(motVehicles.get(1), actualVehicles.get(1));
    }

    @Test
    public void getVehicle_WhenVehicleTestHistoryIsEmptyAndVehicleIsPsv_AnnualTestExpiryDateShouldBeSetProperly() throws Exception {
        DvlaVehicle dvlaVehicle = createDvlaVehicle();
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = createVehicle("PSV");
        vehicle.setTestHistory(new TestHistory[0]);
        vehicle.setRegistrationDate("2015-02-05");

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION);

        HgvPsvVehicle expectedVehicle = createResponseVehicle("PSV", "2016-02-05");
        HgvPsvVehicle actualVehicle = (HgvPsvVehicle) response.getEntity();
        checkIfHgvPsvVehicleIsCorrect(expectedVehicle, actualVehicle);
    }

    private DvlaVehicle createDvlaVehicle() {
        DvlaVehicle dvlaVehicle = new DvlaVehicle();

        ColourLookup colourLookup = new ColourLookup();
        colourLookup.setName("BLACK");

        dvlaVehicle.setVin("VIN123456");
        dvlaVehicle.setColour1(colourLookup);
        dvlaVehicle.setDvlaVehicleId(1);
        dvlaVehicle.setRegistration(REGISTRATION);

        return dvlaVehicle;
    }

    private Vehicle createMotVehicle(String vin) {
        Vehicle motVehicle = new Vehicle();

        motVehicle.setVin(vin);
        motVehicle.setPrimaryColour("BLACK");
        motVehicle.setDvlaVehicleId(1);
        motVehicle.setRegistration(REGISTRATION);

        return motVehicle;
    }

    private uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle createVehicle(String vehicleType) {
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();

        vehicle.setVehicleIdentifier("VIN123456");
        vehicle.setMake("MAKE");
        vehicle.setModel("MODEL");
        vehicle.setVehicleType(vehicleType);
        vehicle.setYearOfManufacture(2013);

        return vehicle;
    }

    private HgvPsvVehicle createResponseVehicle(String vehicleType, String testExpiryDate) {
        HgvPsvVehicle hgvPsvVehicle = new HgvPsvVehicle();

        hgvPsvVehicle.setMake("MAKE");
        hgvPsvVehicle.setModel("MODEL");
        hgvPsvVehicle.setPrimaryColour("BLACK");
        hgvPsvVehicle.setRegistration(REGISTRATION);
        hgvPsvVehicle.setVin("VIN123456");
        hgvPsvVehicle.setVehicleType(vehicleType);
        hgvPsvVehicle.setManufactureYear("2013");
        hgvPsvVehicle.setDvlaId(1);
        hgvPsvVehicle.setMotTestExpiryDate(testExpiryDate);

        return hgvPsvVehicle;
    }

    private void checkIfHgvPsvVehicleIsCorrect(HgvPsvVehicle expectedVehicle, HgvPsvVehicle actualVehicle) {
        assertTrue(actualVehicle.getMake().equals(expectedVehicle.getMake()));
        assertTrue(actualVehicle.getManufactureYear().equals(expectedVehicle.getManufactureYear()));
        assertTrue(actualVehicle.getModel().equals(expectedVehicle.getModel()));
        assertTrue(actualVehicle.getMotTestExpiryDate().equals(expectedVehicle.getMotTestExpiryDate()));
        assertTrue(actualVehicle.getPrimaryColour().equals(expectedVehicle.getPrimaryColour()));
        assertTrue(actualVehicle.getRegistration().equals(expectedVehicle.getRegistration()));
        assertTrue(actualVehicle.getVehicleType().equals(expectedVehicle.getVehicleType()));
        assertTrue(actualVehicle.getDvlaId() == expectedVehicle.getDvlaId());

        if (expectedVehicle.getMotTestNumber() != null) {
            assertTrue(actualVehicle.getMotTestNumber().equals(expectedVehicle.getMotTestNumber()));
        }
    }

    private void checkIfMotVehicleIsCorrect(Vehicle expectedVehicle, uk.gov.dvsa.mot.vehicle.api.Vehicle actualVehicle) {
        assertTrue(actualVehicle.getPrimaryColour().equals(expectedVehicle.getPrimaryColour()));
        assertTrue(actualVehicle.getRegistration().equals(expectedVehicle.getRegistration()));
        assertTrue(actualVehicle.getVin().equals(expectedVehicle.getVin()));
        assertTrue(actualVehicle.getDvlaVehicleId() == expectedVehicle.getDvlaVehicleId());
    }
}
