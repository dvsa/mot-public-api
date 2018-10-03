package uk.gov.dvsa.mot.trade.read.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.dvsa.mot.motr.model.VehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.service.MotrReadServiceDatabase;
import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
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
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MotrReadServiceDatabaseTest {

    MotrReadServiceDatabase motrReadService;

    @Mock
    VehicleReadService vehicleReadServiceMock;

    @Mock
    MotTestReadService motTestReadServiceMock;

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

        Optional<VehicleWithLatestTest> vehicleWithLatestTest =
                motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertTrue("Returned vehicle is null", vehicleWithLatestTest.isPresent());
        assertEquals("Registration is incorrect", registration, vehicleWithLatestTest.get().getRegistration());
        assertEquals("Make is incorrect", make, vehicleWithLatestTest.get().getMake());
        assertEquals("Model is incorrect", model, vehicleWithLatestTest.get().getModel());
        assertEquals("Manufacturing year is incorrect", manufactureYear,
                vehicleWithLatestTest.get().getManufactureYear().getValue());
        assertEquals("Test expiry date is incorrect", expiryDateTime.toLocalDate(),
                vehicleWithLatestTest.get().getTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), vehicleWithLatestTest.get().getTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, vehicleWithLatestTest.get().getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, vehicleWithLatestTest.get().getSecondaryColour());

    }

    @Test
    public void getLatestMotTestByMotTestNumberWithSameRegistrationAndVin_ValidMotNumber_VehicleNull() {
        final long motTestNumber = 5672823;
        when(vehicleReadServiceMock.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber)).thenReturn(null);

        Optional<VehicleWithLatestTest> vehicleWithLatestTest =
                motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertFalse(vehicleWithLatestTest.isPresent());
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

        Optional<VehicleWithLatestTest> vehicleWithLatestTest =
                motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertTrue("Returned vehicle is null", vehicleWithLatestTest.isPresent());
        assertEquals("Registration is incorrect", registration, vehicleWithLatestTest.get().getRegistration());
        assertEquals("Make is incorrect", make, vehicleWithLatestTest.get().getMake());
        assertEquals("Model is incorrect", model, vehicleWithLatestTest.get().getModel());
        assertEquals("Manufacturing year is incorrect", manufactureYear,
                vehicleWithLatestTest.get().getManufactureYear().getValue());
        assertNull("Test expiry date is incorrect", vehicleWithLatestTest.get().getTestExpiryDate());
        assertNull("Test number is incorrect", vehicleWithLatestTest.get().getTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, vehicleWithLatestTest.get().getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, vehicleWithLatestTest.get().getSecondaryColour());
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

        Optional<VehicleWithLatestTest> vehicleWithLatestTest =
                motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertTrue("Returned vehicle is null", vehicleWithLatestTest.isPresent());
        assertEquals("Registration is incorrect", registration, vehicleWithLatestTest.get().getRegistration());
        assertEquals("Make is incorrect", make, vehicleWithLatestTest.get().getMake());
        assertEquals("Model is incorrect", model, vehicleWithLatestTest.get().getModel());
        assertEquals("Manufacturing year is incorrect", manufactureYear,
                vehicleWithLatestTest.get().getManufactureYear().getValue());
        assertEquals("Test expiry date is incorrect", expiryDateTime.toLocalDate(),
                vehicleWithLatestTest.get().getTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), vehicleWithLatestTest.get().getTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, vehicleWithLatestTest.get().getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, vehicleWithLatestTest.get().getSecondaryColour());
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

        when(vehicleReadServiceMock.findByDvlaVehicleId(dvlaVehicleId)).thenReturn(singletonList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        Optional<VehicleWithLatestTest> vehicleWithTest = motrReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertTrue("Returned vehicle is not preset", vehicleWithTest.isPresent());
        assertEquals("Registration is incorrect", registration, vehicleWithTest.get().getRegistration());
        assertEquals("Make is incorrect", make, vehicleWithTest.get().getMake());
        assertEquals("Model is incorrect", model, vehicleWithTest.get().getModel());
        assertEquals("Manufacturing year is incorrect", manufactureYear,
                vehicleWithTest.get().getManufactureYear().getValue());
        assertEquals("Test expiry date is incorrect", expiryDateTime.toLocalDate(),
                vehicleWithTest.get().getTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), vehicleWithTest.get().getTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, vehicleWithTest.get().getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, vehicleWithTest.get().getSecondaryColour());
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
        when(vehicleReadServiceMock.findDvlaVehicleById(dvlaVehicleId)).thenReturn(singletonList(dvlaVehicle));

        Optional<VehicleWithLatestTest> vehicleWithTest = motrReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertTrue("Returned vehicle is not preset", vehicleWithTest.isPresent());
        assertEquals("Registration is incorrect", dvlaVehicle.getRegistration(), vehicleWithTest.get().getRegistration());
        assertEquals("Make is incorrect", dvlaVehicle.getMakeDetail(), vehicleWithTest.get().getMake());
        assertEquals("Model is incorrect", dvlaVehicle.getModelDetail(), vehicleWithTest.get().getModel());
        assertEquals("Manufacturing year is incorrect", manufactureYear,
                vehicleWithTest.get().getManufactureYear().getValue());
        assertNotNull("Test expiry date is incorrect", vehicleWithTest.get().getTestExpiryDate());
        assertNull("Test number is incorrect", vehicleWithTest.get().getTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, vehicleWithTest.get().getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, vehicleWithTest.get().getSecondaryColour());
        assertEquals("DvlaId is incorrect", Integer.toString(dvlaVehicleId), vehicleWithTest.get().getDvlaVehicleId());
    }

    @Test
    public void getLatestMotTestByDvlaVehicleId_VehicleDoesNotExist_DvlaVehicleDoesNotExist() {
        final Integer dvlaVehicleId = 123;

        when(vehicleReadServiceMock.findByDvlaVehicleId(dvlaVehicleId)).thenReturn(null);
        when(vehicleReadServiceMock.findDvlaVehicleById(dvlaVehicleId)).thenReturn(null);

        Optional<VehicleWithLatestTest> vehicleWithTest = motrReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertFalse("Returned vehicle is not empty", vehicleWithTest.isPresent());
    }
}
