package uk.gov.dvsa.mot.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
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
import static org.junit.Assert.assertSame;
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

    @InjectMocks
    private MotrRequestHandler motrRequestHandler;

    @Before
    public void setUp() throws IOException {

        MockitoAnnotations.initMocks(this);
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
    @Ignore
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
        assertSame(response.readEntity(HgvPsvVehicle.class), writeVehicleResponseAsString(expectedVehicle));
    }

    @Test
    @Ignore
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
        assertTrue(response.getEntity().equals(writeVehicleResponseAsString(expectedVehicle)));
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
    @Ignore
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
        assertTrue(response.getEntity().equals(writeVehicleResponseAsString(expectedVehicle)));
    }

    @Test
    @Ignore
    public void getVehicle_WhenSingleMotVehicleIsPresent_BodyIsCorrect() throws Exception {
        List<Vehicle> motVehicles = new ArrayList<>();
        motVehicles.add(createMotVehicle("VIN123451"));
        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(motVehicles);

        Response response = motrRequestHandler.getVehicle(REGISTRATION);

        assertTrue(response.getEntity().equals(writeVehicleResponseAsString(motVehicles)));
    }

    @Test
    @Ignore
    public void getVehicle_WhenTwoMotVehicleIsPresent_BodyIsCorrect() throws Exception {
        List<Vehicle> motVehicles = new ArrayList<>();
        motVehicles.add(createMotVehicle("VIN123451"));
        motVehicles.add(createMotVehicle("VIN123452"));
        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(motVehicles);

        Response response = motrRequestHandler.getVehicle(REGISTRATION);

        assertTrue(response.getEntity().equals(writeVehicleResponseAsString(motVehicles)));
    }

    @Test
    @Ignore
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
        assertTrue(response.getEntity().equals(writeVehicleResponseAsString(expectedVehicle)));
    }

    private String writeVehicleResponseAsString(Object vehicles) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(vehicles);
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
        Vehicle dvlaVehicle = new Vehicle();

        dvlaVehicle.setVin(vin);
        dvlaVehicle.setPrimaryColour("BLACK");
        dvlaVehicle.setDvlaVehicleId(1);
        dvlaVehicle.setRegistration(REGISTRATION);

        return dvlaVehicle;
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
}
