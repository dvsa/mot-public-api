package uk.gov.dvsa.mot.trade.read.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
import uk.gov.dvsa.mot.persist.TradeReadDao;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
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

    /**
     * Check that the service asks for the right vehicle IDs for a selection of
     * pages Based on the hard-coded page size of 500
     */
    @Test
    public void getVehiclesByPage_CalculatesPagesCorrectly() {

        for (int page = 0; page < 10; ++page) {
            int startId = page * 500;
            int endId = startId + 500;

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
    public void getVehiclesByRegistrationAndMake_GetsByGivenNumber() {

        final String registration = "XX99XXX";
        final String make = "ACME";

        tradeReadService.getVehiclesByRegistrationAndMake(registration, make);

        verify(tradeReadDaoMock).getVehiclesMotTestsByRegistrationAndMake(registration, make);
    }

    @Test
    public void getVehiclesByRegistration_GetsVehicleFromDvsaTable() {

        final String registration = "XX99XXX";
        final uk.gov.dvsa.mot.trade.api.Vehicle vehicle = new uk.gov.dvsa.mot.trade.api.Vehicle();
        vehicle.setRegistration(registration);

        final List<uk.gov.dvsa.mot.trade.api.Vehicle> expectedVehicles = Arrays.asList(vehicle);

        when(tradeReadDaoMock.getVehiclesMotTestsByRegistration(registration)).thenReturn(expectedVehicles);

        List<uk.gov.dvsa.mot.trade.api.Vehicle> returnedVehicles = tradeReadService.getVehiclesByRegistration(registration);

        verify(tradeReadDaoMock).getVehiclesMotTestsByRegistration(registration);
        assertEquals("It returns one vehicle",returnedVehicles.size(), expectedVehicles.size());
        assertEquals("It contains vehicle with specified registration",
                returnedVehicles.get(0).getRegistration(), expectedVehicles.get(0).getRegistration());
    }

    @Test
    public void getVehiclesByRegistration_GetsVehicleFromDvlaTable() {

        final String registration = "XX99XXX";

        uk.gov.dvsa.mot.trade.api.Vehicle vehicle = new uk.gov.dvsa.mot.trade.api.Vehicle();
        vehicle.setRegistration(registration);

        uk.gov.dvsa.mot.trade.api.DvlaVehicle dvlaVehicle = new uk.gov.dvsa.mot.trade.api.DvlaVehicle();
        dvlaVehicle.setRegistration(registration);

        final List<uk.gov.dvsa.mot.trade.api.Vehicle> expectedVehicles = Arrays.asList(vehicle);

        when(tradeReadDaoMock.getVehiclesMotTestsByRegistration(registration)).thenReturn(Collections.emptyList());
        when(vehicleReadServiceMock.getDvlaVehicleByRegistration(registration)).thenReturn(dvlaVehicle);

        List<uk.gov.dvsa.mot.trade.api.Vehicle> returnedVehicles = tradeReadService.getVehiclesByRegistration(registration);

        verify(tradeReadDaoMock).getVehiclesMotTestsByRegistration(registration);
        verify(vehicleReadServiceMock).getDvlaVehicleByRegistration(registration);
        assertEquals("It returns one vehicle",returnedVehicles.size(), expectedVehicles.size());
        assertEquals("It contains vehicle with specified registration",
                returnedVehicles.get(0).getRegistration(), expectedVehicles.get(0).getRegistration());
    }

    @Test
    public void getVehiclesByRegistration_NoVehicleFoundInBothTables() {

        final String registration = "XX99XXX";

        final List<uk.gov.dvsa.mot.trade.api.Vehicle> expectedVehicles = Collections.emptyList();

        when(tradeReadDaoMock.getVehiclesMotTestsByRegistration(registration)).thenReturn(Collections.emptyList());
        when(vehicleReadServiceMock.getDvlaVehicleByRegistration(registration)).thenReturn(null);

        List<uk.gov.dvsa.mot.trade.api.Vehicle> returnedVehicles = tradeReadService.getVehiclesByRegistration(registration);

        assertEquals("It returns no vehicle",returnedVehicles.size(), expectedVehicles.size());
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