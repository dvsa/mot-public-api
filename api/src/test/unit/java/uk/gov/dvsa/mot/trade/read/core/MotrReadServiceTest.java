package uk.gov.dvsa.mot.trade.read.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
import uk.gov.dvsa.mot.persist.TradeReadDao;
import uk.gov.dvsa.mot.trade.api.MotrResponse;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MotrReadServiceTest {

    MotrReadServiceDatabase motrReadService;

    @Mock
    VehicleReadService vehicleReadServiceMock;

    @Mock
    MotTestReadService motTestReadServiceMock;

    @Mock
    TradeReadDao tradeReadDaoMock;

    /**
     * Returns a copy of the provided LocalDateTime with its time fields all set
     * to 0
     *
     * @param input The source time
     * @return a copy of the source time with all time fields set to zero but the data fields unaltered
     */
    private static LocalDateTime atMidnight(LocalDateTime input) {

        return input.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * Converts a LocalDateTime to a Date in the system's default time zone.
     *
     * @param ldt LocalDateTime to convert
     * @return Date caused by interpreting the LocalDateTime in the system default time zone
     */
    private static Date localDateTimeToDate(LocalDateTime ldt) {

        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Before
    public void beforeTest() {
        // Arrange - Create class under test
        motrReadService = new MotrReadServiceDatabase(motTestReadServiceMock, vehicleReadServiceMock);
    }

    @After
    public void afterTest() {

        motrReadService = null;
    }

    private uk.gov.dvsa.mot.trade.api.DvlaVehicle createDvlaVehicle() {

        final String registration = "AA00AAA";
        final LocalDateTime manufactureDateTime = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        final Date manufactureDate = Date.from(manufactureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final String make = "MADEBY";
        final String model = "ETERNIA";
        final String primaryColour = "BLUE";
        final String secondaryColour = "WHITE";
        final Integer dvlaVehicleId = 123;

        uk.gov.dvsa.mot.trade.api.DvlaVehicle dvlaVehicle = new uk.gov.dvsa.mot.trade.api.DvlaVehicle();
        dvlaVehicle.setRegistration(registration);
        dvlaVehicle.setDvlaVehicleId(dvlaVehicleId);

        dvlaVehicle.setMakeDetail(make);
        dvlaVehicle.setModelDetail(model);
        dvlaVehicle.setColour1(primaryColour);
        dvlaVehicle.setColour2(secondaryColour);
        dvlaVehicle.setEuClassification("L6");
        dvlaVehicle.setFirstRegistrationDate(new Date());
        dvlaVehicle.setManufactureDate(manufactureDate);
        dvlaVehicle.setLastUpdatedOn(new Date());
        return dvlaVehicle;
    }

    @Test
    public void getLatestMotTestByMotTestNumberWithSameRegistrationAndVin_ValidRegistration_ReturnsVehicle_FullyPopulated() {

        final String registration = "AA00AAA";
        final LocalDateTime expiryDateTime = LocalDateTime.now().plus(Period.ofMonths(6));
        final Date expiryDate = Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final LocalDateTime manufactureDateTime = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        final int manufactureYear = manufactureDateTime.getYear();
        final Date manufactureDate = Date.from(manufactureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final long motTestNumber = 5672823;
        final String make = "MADEBY";
        final String model = "ETERNIA";
        final String primaryColour = "BLUE";
        final String secondaryColour = "WHITE";

        final Vehicle backendVehicle = new Vehicle();
        backendVehicle.setRegistration(registration);
        backendVehicle.setManufactureDate(manufactureDate);
        backendVehicle.setMake(make);
        backendVehicle.setModel(model);
        backendVehicle.setPrimaryColour(primaryColour);
        backendVehicle.setSecondaryColour(secondaryColour);

        final MotTest backendTest = new MotTest();
        backendTest.setExpiryDate(expiryDate);
        backendTest.setNumber(motTestNumber);

        when(vehicleReadServiceMock.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        MotrResponse motrResponse =
                motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertNotNull("Returned vehicle is null", motrResponse);
        assertEquals("Registration is incorrect", registration, motrResponse.getRegistration());
        assertEquals("Make is incorrect", make, motrResponse.getMake());
        assertEquals("Model is incorrect", model, motrResponse.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                motrResponse.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                motrResponse.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), motrResponse.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, motrResponse.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, motrResponse.getSecondaryColour());

    }

    @Test
    public void getLatestMotTestByMotTestNumberWithSameRegistrationAndVin_ValidMotNumber_VehicleNull() {

        final long motTestNumber = 5672823;

        when(vehicleReadServiceMock.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber)).thenReturn(null);

        MotrResponse motrResponse =
                motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertNull(motrResponse);
    }

    @Test
    public void getLatestMotTestByMotTestNumberWithSameRegistrationAndVin_VehicleWithSplitHistory_MotDoesNotExist() {

        final String registration = "AA00AAA";
        final LocalDateTime manufactureDateTime = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        final int manufactureYear = manufactureDateTime.getYear();
        final Date manufactureDate = Date.from(manufactureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final long motTestNumber = 5672823;
        final String make = "MADEBY";
        final String model = "ETERNIA";
        final String primaryColour = "BLUE";
        final String secondaryColour = "WHITE";

        final Vehicle backendVehicle = new Vehicle();
        backendVehicle.setRegistration(registration);
        backendVehicle.setManufactureDate(manufactureDate);
        backendVehicle.setMake(make);
        backendVehicle.setModel(model);
        backendVehicle.setPrimaryColour(primaryColour);
        backendVehicle.setSecondaryColour(secondaryColour);

        final Vehicle backendVehicle2 = new Vehicle();
        backendVehicle2.setRegistration(registration);
        backendVehicle2.setManufactureDate(manufactureDate);
        backendVehicle2.setMake(make);
        backendVehicle2.setModel(model);
        backendVehicle2.setPrimaryColour(primaryColour);
        backendVehicle2.setSecondaryColour(secondaryColour);

        when(vehicleReadServiceMock.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber))
                .thenReturn(Arrays.asList(backendVehicle, backendVehicle2));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(null);
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle2)).thenReturn(null);

        MotrResponse motrResponse =
                motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertNotNull("Returned vehicle is null", motrResponse);
        assertEquals("Registration is incorrect", registration, motrResponse.getRegistration());
        assertEquals("Make is incorrect", make, motrResponse.getMake());
        assertEquals("Model is incorrect", model, motrResponse.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                motrResponse.getManufactureYear());
        assertNull("Test expiry date is incorrect", motrResponse.getMotTestExpiryDate());
        assertNull("Test number is incorrect", motrResponse.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, motrResponse.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, motrResponse.getSecondaryColour());
    }


    @Test
    public void getLatestMotTestByMotTestNumberWithSameRegistrationAndVin_VehicleWithSplitHistory_MotExistsOneVehicle() {

        final String registration = "AA00AAA";
        final LocalDateTime expiryDateTime = LocalDateTime.now().plus(Period.ofMonths(6));
        final Date expiryDate = Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final LocalDateTime manufactureDateTime = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        final int manufactureYear = manufactureDateTime.getYear();
        final Date manufactureDate = Date.from(manufactureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final long motTestNumber = 5672823;
        final String make = "MADEBY";
        final String model = "ETERNIA";
        final String primaryColour = "BLUE";
        final String secondaryColour = "WHITE";

        final Vehicle backendVehicle = new Vehicle();
        backendVehicle.setRegistration(registration);
        backendVehicle.setManufactureDate(manufactureDate);
        backendVehicle.setMake(make);
        backendVehicle.setModel(model);
        backendVehicle.setPrimaryColour(primaryColour);
        backendVehicle.setSecondaryColour(secondaryColour);

        final Vehicle backendVehicle2 = new Vehicle();
        backendVehicle2.setRegistration(registration);
        backendVehicle2.setManufactureDate(manufactureDate);
        backendVehicle2.setMake(make);
        backendVehicle2.setModel(model);
        backendVehicle2.setPrimaryColour(primaryColour);
        backendVehicle2.setSecondaryColour(secondaryColour);

        final MotTest backendTest = new MotTest();
        backendTest.setExpiryDate(expiryDate);
        backendTest.setNumber(motTestNumber);

        when(vehicleReadServiceMock.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber))
                .thenReturn(Arrays.asList(backendVehicle, backendVehicle2));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(null);
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle2)).thenReturn(backendTest);

        MotrResponse motrResponse =
                motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertNotNull("Returned vehicle is null", motrResponse);
        assertEquals("Registration is incorrect", registration, motrResponse.getRegistration());
        assertEquals("Make is incorrect", make, motrResponse.getMake());
        assertEquals("Model is incorrect", model, motrResponse.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                motrResponse.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                motrResponse.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), motrResponse.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, motrResponse.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, motrResponse.getSecondaryColour());
    }

    @Test
    public void getLatestMotTestByDvlaVehicleId_ReturnsVehicle_FullyPopulated_WhenMotTestExists() {

        final Integer dvlaVehicleId = 123;
        final String registration = "AA00AAA";
        final LocalDateTime expiryDateTime = LocalDateTime.now().plus(Period.ofMonths(6));
        final Date expiryDate = Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final LocalDateTime manufactureDateTime = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        final int manufactureYear = manufactureDateTime.getYear();
        final Date manufactureDate = Date.from(manufactureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final long motTestNumber = 5672823;
        final String make = "MADEBY";
        final String model = "ETERNIA";
        final String primaryColour = "BLUE";
        final String secondaryColour = "WHITE";

        final Vehicle backendVehicle = new Vehicle();
        backendVehicle.setRegistration(registration);
        backendVehicle.setManufactureDate(manufactureDate);
        backendVehicle.setMake(make);
        backendVehicle.setModel(model);
        backendVehicle.setPrimaryColour(primaryColour);
        backendVehicle.setSecondaryColour(secondaryColour);

        final MotTest backendTest = new MotTest();
        backendTest.setExpiryDate(expiryDate);
        backendTest.setNumber(motTestNumber);

        when(vehicleReadServiceMock.findByDvlaVehicleId(dvlaVehicleId)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        MotrResponse motrResponse =
                motrReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertNotNull("Returned vehicle is null", motrResponse);
        assertEquals("Registration is incorrect", registration, motrResponse.getRegistration());
        assertEquals("Make is incorrect", make, motrResponse.getMake());
        assertEquals("Model is incorrect", model, motrResponse.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                motrResponse.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                motrResponse.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), motrResponse.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, motrResponse.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, motrResponse.getSecondaryColour());
    }

    @Test
    public void getLatestMotTestByDvlaVehicleId_VehicleDoesNotExist_DvlaVehicleDoes() {

        uk.gov.dvsa.mot.trade.api.DvlaVehicle dvlaVehicle = createDvlaVehicle();

        final LocalDateTime manufactureDateTime =
                LocalDateTime.ofInstant(dvlaVehicle.getManufactureDate().toInstant(), ZoneId.systemDefault());
        final int manufactureYear = manufactureDateTime.getYear();
        final String primaryColour = "BLUE";
        final String secondaryColour = "WHITE";

        int dvlaVehicleId = dvlaVehicle.getDvlaVehicleId();
        when(vehicleReadServiceMock.findByDvlaVehicleId(dvlaVehicleId)).thenReturn(null);

        when(vehicleReadServiceMock.findDvlaVehicleById(dvlaVehicleId)).thenReturn(Arrays.asList(dvlaVehicle));

        MotrResponse motrResponse =
                motrReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertNotNull("Returned vehicle is null", motrResponse);
        assertEquals("Registration is incorrect", dvlaVehicle.getRegistration(), motrResponse.getRegistration());
        assertEquals("Make is incorrect", dvlaVehicle.getMakeDetail(), motrResponse.getMake());
        assertEquals("Model is incorrect", dvlaVehicle.getModelDetail(), motrResponse.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                motrResponse.getManufactureYear());
        assertNotNull("Test expiry date is incorrect", motrResponse.getMotTestExpiryDate());
        assertNull("Test number is incorrect", motrResponse.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, motrResponse.getPrimaryColour());
        assertEquals("DvlaId is incorrect", Integer.toString(dvlaVehicleId), motrResponse.getDvlaId());
        assertEquals("Secondary colour is incorrect", secondaryColour, motrResponse.getSecondaryColour());
    }

    @Test
    public void getLatestMotTestByDvlaVehicleId_VehicleDoesNotExist_DvlaVehicleDoesNotExist() {

        final Integer dvlaVehicleId = 123;

        when(vehicleReadServiceMock.findByDvlaVehicleId(dvlaVehicleId)).thenReturn(null);

        when(vehicleReadServiceMock.findDvlaVehicleById(dvlaVehicleId)).thenReturn(null);

        MotrResponse motrResponse =
                motrReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertNull("Returned vehicle is not null", motrResponse);
    }
}
