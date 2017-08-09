package uk.gov.dvsa.mot.trade.read.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
import uk.gov.dvsa.mot.persist.TradeReadDao;
import uk.gov.dvsa.mot.persist.model.ColourLookup;
import uk.gov.dvsa.mot.persist.model.DvlaMake;
import uk.gov.dvsa.mot.persist.model.DvlaModel;
import uk.gov.dvsa.mot.persist.model.DvlaVehicle;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TradeReadServiceTest {

    TradeReadServiceDatabase tradeReadService;

    @Mock
    VehicleReadService vehicleReadServiceMock;

    @Mock
    MotTestReadService motTestReadServiceMock;

    @Mock
    MotTest motTestMock;

    @Mock
    Vehicle vehicleMock;

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
        tradeReadService = new TradeReadServiceDatabase(motTestReadServiceMock, vehicleReadServiceMock,
                tradeReadDaoMock);
    }

    @After
    public void afterTest() {

        tradeReadService = null;
    }

    @Test
    public void getLatestMotTestByRegistration_ValidRegistration_ReturnsVehicle_FullyPopulated() {

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

        when(vehicleReadServiceMock.findByRegistration(registration)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle = tradeReadService.getLatestMotTestByRegistration(registration);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                apiVehicle.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());

    }

    @Test
    public void getLatestMotTestByRegistration_ValidRegistration_NullTest() {

        final String registration = "AA00AAA";
        final LocalDateTime manufactureDateTime = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        final int manufactureYear = manufactureDateTime.getYear();
        final Date manufactureDate = Date.from(manufactureDateTime.atZone(ZoneId.systemDefault()).toInstant());
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

        when(vehicleReadServiceMock.findByRegistration(registration)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(null);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle = tradeReadService.getLatestMotTestByRegistration(registration);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertNull("Test expiry date is incorrect", apiVehicle.getMotTestExpiryDate());
        assertNull("Test number is incorrect", apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());

    }

    @Test
    public void getLatestMotTestByRegistration_ValidRegistration_VehicleNull() {

        final String registration = "AA00AAA";

        when(vehicleReadServiceMock.findByRegistration(registration)).thenReturn(null);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle = tradeReadService.getLatestMotTestByRegistration(registration);

        assertNull(apiVehicle);
    }

    @Test
    public void getLatestMotTestByRegistration_ValidRegistration_ReturnsVehicle_WithoutSecondaryColour() {

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

        final Vehicle backendVehicle = new Vehicle();
        backendVehicle.setRegistration(registration);
        backendVehicle.setManufactureDate(manufactureDate);
        backendVehicle.setMake(make);
        backendVehicle.setModel(model);
        backendVehicle.setPrimaryColour(primaryColour);

        final MotTest backendTest = new MotTest();
        backendTest.setExpiryDate(expiryDate);
        backendTest.setNumber(motTestNumber);

        when(vehicleReadServiceMock.findByRegistration(registration)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle = tradeReadService.getLatestMotTestByRegistration(registration);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                apiVehicle.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertNull("Secondary colour was made up", apiVehicle.getSecondaryColour());

    }

    @Test
    public void getLatestMotTestByRegistration_ValidRegistration_ReturnsVehicle_SecondaryColourNotStated() {

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

        final Vehicle backendVehicle = new Vehicle();
        backendVehicle.setRegistration(registration);
        backendVehicle.setManufactureDate(manufactureDate);
        backendVehicle.setMake(make);
        backendVehicle.setModel(model);
        backendVehicle.setPrimaryColour(primaryColour);
        backendVehicle.setSecondaryColour("Not Stated");

        final MotTest backendTest = new MotTest();
        backendTest.setExpiryDate(expiryDate);
        backendTest.setNumber(motTestNumber);

        when(vehicleReadServiceMock.findByRegistration(registration)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle = tradeReadService.getLatestMotTestByRegistration(registration);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                apiVehicle.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertNull("Secondary colour was made up", apiVehicle.getSecondaryColour());
    }

    @Test
    public void getLatestMotTestByRegistration_ValidRegistration_ReturnsVehicle_WithoutMotNumber() {

        final String registration = "AA00AAA";
        final LocalDateTime expiryDateTime = LocalDateTime.now().plus(Period.ofMonths(6));
        final Date expiryDate = Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final LocalDateTime manufactureDateTime = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        final int manufactureYear = manufactureDateTime.getYear();
        final Date manufactureDate = Date.from(manufactureDateTime.atZone(ZoneId.systemDefault()).toInstant());
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

        when(vehicleReadServiceMock.findByRegistration(registration)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle = tradeReadService.getLatestMotTestByRegistration(registration);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                apiVehicle.getMotTestExpiryDate());
        assertNull("The system made up the test number from somewhere", apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());

    }

    @Test
    public void getLatestMotTestByRegistration_ValidRegistration_ReturnsVehicle_WithoutExpiryDate() {

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

        final MotTest backendTest = new MotTest();
        backendTest.setNumber(motTestNumber);

        when(vehicleReadServiceMock.findByRegistration(registration)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle = tradeReadService.getLatestMotTestByRegistration(registration);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertNull("Test expiry date was made up out of nowhere", apiVehicle.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());

    }

    @Test
    public void getLatestMotTestByRegistration_ValidRegistration_ReturnsVehicle_WithoutManufactureDate() {

        final String registration = "AA00AAA";
        final LocalDateTime expiryDateTime = LocalDateTime.now().plus(Period.ofMonths(6));
        final Date expiryDate = Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final long motTestNumber = 5672823;
        final String make = "MADEBY";
        final String model = "ETERNIA";
        final String primaryColour = "BLUE";
        final String secondaryColour = "WHITE";

        final Vehicle backendVehicle = new Vehicle();
        backendVehicle.setRegistration(registration);
        backendVehicle.setMake(make);
        backendVehicle.setModel(model);
        backendVehicle.setPrimaryColour(primaryColour);
        backendVehicle.setSecondaryColour(secondaryColour);

        final MotTest backendTest = new MotTest();
        backendTest.setExpiryDate(expiryDate);
        backendTest.setNumber(motTestNumber);

        when(vehicleReadServiceMock.findByRegistration(registration)).thenReturn(Arrays.asList(backendVehicle));
        when(motTestReadServiceMock.getLatestMotTestPassByVehicle(backendVehicle)).thenReturn(backendTest);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle = tradeReadService.getLatestMotTestByRegistration(registration);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertNull("Manufacturing year was made up", apiVehicle.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                apiVehicle.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());

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

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle =
                tradeReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                apiVehicle.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());

    }

    @Test
    public void getLatestMotTestByMotTestNumberWithSameRegistrationAndVin_ValidMotNumber_VehicleNull() {

        final long motTestNumber = 5672823;

        when(vehicleReadServiceMock.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber)).thenReturn(null);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle =
                tradeReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertNull(apiVehicle);
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

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle =
                tradeReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertNull("Test expiry date is incorrect", apiVehicle.getMotTestExpiryDate());
        assertNull("Test number is incorrect", apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());
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

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle =
                tradeReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                apiVehicle.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());
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

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle =
                tradeReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertEquals("Test expiry date is incorrect", expiryDateTime.format(DateTimeFormatter.ISO_DATE),
                apiVehicle.getMotTestExpiryDate());
        assertEquals("Test number is incorrect", Long.toString(motTestNumber), apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());
    }

    @Test
    public void getLatestMotTestByDvlaVehicleId_VehicleDoesNotExist_DvlaVehicleDoes() {

        final Integer dvlaVehicleId = 123;
        final String registration = "AA00AAA";
        final LocalDateTime manufactureDateTime = LocalDateTime.now().minus(5, ChronoUnit.YEARS);
        final int manufactureYear = manufactureDateTime.getYear();
        final Date manufactureDate = Date.from(manufactureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final long motTestNumber = 5672823;
        final String make = "MADEBY";
        final String model = "ETERNIA";
        final String primaryColour = "BLUE";
        final String secondaryColour = "WHITE";

        DvlaVehicle dvlaVehicle = new DvlaVehicle();
        dvlaVehicle.setRegistration(registration);
        dvlaVehicle.setDvlaVehicleId(dvlaVehicleId);

        DvlaMake dvlaMake = new DvlaMake();
        dvlaMake.setName(make);

        DvlaModel dvlaModel = new DvlaModel();
        dvlaModel.setName(model);

        ColourLookup colour1 = new ColourLookup();
        colour1.setName(primaryColour);

        ColourLookup colour2 = new ColourLookup();
        colour2.setName(secondaryColour);

        dvlaVehicle.setMakeDetail(dvlaMake);
        dvlaVehicle.setModelDetail(dvlaModel);
        dvlaVehicle.setColour1(colour1);
        dvlaVehicle.setColour2(colour2);
        dvlaVehicle.setEuClassification("L6");
        dvlaVehicle.setFirstRegistrationDate(new Date());
        dvlaVehicle.setManufactureDate(manufactureDate);
        dvlaVehicle.setLastUpdatedOn(new Date());

        when(vehicleReadServiceMock.findByDvlaVehicleId(dvlaVehicleId)).thenReturn(null);

        when(vehicleReadServiceMock.findDvlaVehicleById(dvlaVehicleId)).thenReturn(Arrays.asList(dvlaVehicle));

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle =
                tradeReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertNotNull("Returned vehicle is null", apiVehicle);
        assertEquals("Registration is incorrect", registration, apiVehicle.getRegistration());
        assertEquals("Make is incorrect", make, apiVehicle.getMake());
        assertEquals("Model is incorrect", model, apiVehicle.getModel());
        assertEquals("Manufacturing year is incorrect", Integer.toString(manufactureYear),
                apiVehicle.getManufactureYear());
        assertNotNull("Test expiry date is incorrect", apiVehicle.getMotTestExpiryDate());
        assertNull("Test number is incorrect", apiVehicle.getMotTestNumber());
        assertEquals("Primary colour is incorrect", primaryColour, apiVehicle.getPrimaryColour());
        assertEquals("DvlaId is incorrect", dvlaVehicleId, apiVehicle.getDvlaId());
        assertEquals("Secondary colour is incorrect", secondaryColour, apiVehicle.getSecondaryColour());
    }

    @Test
    public void getLatestMotTestByDvlaVehicleId_VehicleDoesNotExist_DvlaVehicleDoesNotExist() {

        final Integer dvlaVehicleId = 123;

        when(vehicleReadServiceMock.findByDvlaVehicleId(dvlaVehicleId)).thenReturn(null);

        when(vehicleReadServiceMock.findDvlaVehicleById(dvlaVehicleId)).thenReturn(null);

        uk.gov.dvsa.mot.trade.api.Vehicle apiVehicle =
                tradeReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        assertNull("Returned vehicle is not null", apiVehicle);
    }

    /**
     * Check that the service asks for the right vehicle IDs for a selection of
     * pages Based on the hard-coded page size of 2000
     */
    @Test
    public void getVehiclesByPage_CalculatesPagesCorrectly() {

        for (int page = 0; page < 10; ++page) {
            int startId = page * 2000;
            int endId = startId + 2000;

            tradeReadService.getVehiclesByPage(page);
            verify(tradeReadDaoMock).getVehiclesMotTestsByRange(startId, endId);
        }
    }

    /**
     * When you pass in null date and page, it should default to today and page 1,
     * so we're expecting to see a request from midnight today to midnight today +
     * 1 minute
     */
    @Test
    public void getVehiclesByDatePage_NullParams() {

        LocalDateTime start = atMidnight(LocalDateTime.now());
        LocalDateTime end = start.plusMinutes(1); // pages are one minute in size

        Date startDate = localDateTimeToDate(start);
        Date endDate = localDateTimeToDate(end);

        tradeReadService.getVehiclesByDatePage(null, null);
        verify(tradeReadDaoMock).getVehiclesMotTestsByDateRange(startDate, endDate);
    }

    /**
     * Ask for a date two weeks ago, but with a null page so it should default to
     * asking for that date and a date one minute later
     */
    @Test
    public void getVehiclesByDatePage_NullPage() {

        LocalDateTime start = LocalDateTime.now().minusWeeks(2); // request for
        // two weeks ago
        LocalDateTime expectedStart = atMidnight(start);
        LocalDateTime expectedEnd = expectedStart.plusMinutes(1);

        tradeReadService.getVehiclesByDatePage(localDateTimeToDate(start), null);

        verify(tradeReadDaoMock).getVehiclesMotTestsByDateRange(localDateTimeToDate(expectedStart),
                localDateTimeToDate(expectedEnd));
    }

    /**
     * Ask for a date two weeks ago and page 10, which should ask for that date +
     * 10 minutes to that date + 11 minutes.
     */
    @Test
    public void getVehiclesByDatePage_DateAndPage() {

        LocalDateTime start = LocalDateTime.now().minusWeeks(2); // request for
        // two weeks ago
        LocalDateTime expectedStart = atMidnight(start).plusMinutes(9);
        LocalDateTime expectedEnd = expectedStart.plusMinutes(1);

        tradeReadService.getVehiclesByDatePage(localDateTimeToDate(start), 10);
        verify(tradeReadDaoMock).getVehiclesMotTestsByDateRange(localDateTimeToDate(expectedStart),
                localDateTimeToDate(expectedEnd));
    }

    /**
     * Ask for a page > 1440 and we should get page 1440
     */
    @Test
    public void getVehiclesByDatePage_DateAndPage_AfterMaxPage() {

        int pageToRequest = 1441;
        int pageExpected = 1440;
        getVehiclesByDatePageTestHelper(pageToRequest, pageExpected);
    }

    /**
     * Ask for a page < 1 and we should get page 1
     */
    @Test
    public void getVehiclesByDatePage_DateAndPage_LowPage() {

        int pageToRequest = 0;
        int pageExpected = 1;
        getVehiclesByDatePageTestHelper(pageToRequest, pageExpected);
    }

    @Test
    public void getMakes_PassesThrough() {

        List<String> makes = Arrays.asList("RENAULT", "FORD", "TOYOTA", "FERRARI", "TESLA");
        when(vehicleReadServiceMock.getMakes()).thenReturn(makes);

        List<String> actual = tradeReadService.getMakes();

        assertThat(actual, equalTo(makes));
    }

    @Test
    public void getVehiclesByVehicleId_GetsByGivenId() {

        final int vehicleId = 7891347;

        tradeReadService.getVehiclesByVehicleId(vehicleId);

        verify(tradeReadDaoMock).getVehiclesMotTestsByVehicleId(vehicleId);
    }

    @Test
    public void getVehiclesMotTestsByMotTestNumber_GetsByGivenNumber() {

        final int testNumber = 7891347;

        tradeReadService.getVehiclesMotTestsByMotTestNumber(testNumber);

        verify(tradeReadDaoMock).getVehiclesMotTestsByMotTestNumber(testNumber);
    }

    @Test
    public void getVehiclesByRegistrationAndMake_GetsByGivenNumber() {

        final String registration = "XX99XXX";
        final String make = "ACME";

        tradeReadService.getVehiclesByRegistrationAndMake(registration, make);

        verify(tradeReadDaoMock).getVehiclesMotTestsByRegistrationAndMake(registration, make);
    }

    /**
     * Run a common format page capping test on getVehiclesByDatePage
     */
    private void getVehiclesByDatePageTestHelper(int pageToRequest, int pageExpected) {

        LocalDateTime start = LocalDateTime.now().minusWeeks(2);
        LocalDateTime expectedStart = atMidnight(start).plusMinutes(pageExpected - 1);
        LocalDateTime expectedEnd = expectedStart.plusMinutes(1);

        tradeReadService.getVehiclesByDatePage(localDateTimeToDate(start), pageToRequest);
        verify(tradeReadDaoMock).getVehiclesMotTestsByDateRange(localDateTimeToDate(expectedStart),
                localDateTimeToDate(expectedEnd));
    }
}