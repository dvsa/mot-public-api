package uk.gov.dvsa.mot.motr.service;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.motr.model.DvlaVehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.model.MotVehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.model.VehicleType;
import uk.gov.dvsa.mot.motr.model.VehicleWithLatestTest;
import uk.gov.dvsa.mot.trade.api.DvlaVehicle;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasNoCause;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MotrVehicleHistoryProviderTest {

    private static final String REGISTRATION = "REG123456";
    private static final int DVLA_VEHICLE_ID = 213;
    private static final long MOT_TEST_NUMBER = 12345L;

    @Mock
    private MotrReadService motrReadService;

    @Mock
    private HgvVehicleProvider hgvVehicleProvider;

    private MotrVehicleHistoryProvider motrVehicleHistoryProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        motrVehicleHistoryProvider = new MotrVehicleHistoryProvider(motrReadService, hgvVehicleProvider);
    }

    @Test
    public void searchVehicleByRegistration_WhenMotNorDvlaVehiclePresent_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(Optional.empty());
        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(Optional.empty());

        catchException(motrVehicleHistoryProvider).searchVehicleByRegistration(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No MOT Test or DVLA vehicle found for registration")),
                hasNoCause()
        ));
    }

    @Test
    public void searchVehicleByRegistration_WhenGetHgvPsvVehicleNotReturnVehicle_ShouldThrowException() throws Exception {
        DvlaVehicle dvlaVehicleEntity = new DvlaVehicle();
        Optional<VehicleWithLatestTest> dvlaVehicle = Optional.of(new DvlaVehicleWithLatestTest(dvlaVehicleEntity, null));

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(Optional.empty());
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(null);

        catchException(motrVehicleHistoryProvider).searchVehicleByRegistration(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("Could not determine test expiry date for registration " + REGISTRATION)),
                hasNoCause()
        ));
    }

    @Test
    public void searchVehicleByRegistration_WhenMotVehicleFound_ShouldReturnVehicle() throws Exception {
        uk.gov.dvsa.mot.vehicle.api.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.api.Vehicle();
        vehicle.setRegistration(REGISTRATION);
        Optional<VehicleWithLatestTest> motVehicle = Optional.of(
                new MotVehicleWithLatestTest(vehicle, null)
        );
        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(motVehicle);
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(Optional.empty());

        VehicleWithLatestTest result = motrVehicleHistoryProvider.searchVehicleByRegistration(REGISTRATION);

        assertCorrectMotVehicle(vehicle, result);
    }

    @Test
    public void searchVehicleByRegistration_WhenGetHgvPsvVehicleFound_ShouldReturnVehicle() throws Exception {
        DvlaVehicle dvlaVehicleEntity = new DvlaVehicle();
        Optional<VehicleWithLatestTest> dvlaVehicle = Optional.of(new DvlaVehicleWithLatestTest(dvlaVehicleEntity, null));
        Vehicle hgvPsvVehicle = buildHgvPsvVehicle();

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(Optional.empty());
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(hgvPsvVehicle);

        VehicleWithLatestTest result = motrVehicleHistoryProvider.searchVehicleByRegistration(REGISTRATION);

        assertCorrectHgvPSvVehicle(hgvPsvVehicle, result);
    }

    @Test
    public void searchForCommercialVehicleByRegistration_WhenVehicleReadServiceFindByRegistrationReturnsNull_ShouldThrowException()
        throws Exception {
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(Optional.empty());

        catchException(motrVehicleHistoryProvider).searchForCommercialVehicleByRegistration(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No DVLA vehicle found for registration " + REGISTRATION)),
                hasNoCause()
        ));
    }

    @Test
    public void searchForCommercialVehicleByRegistration_WhenHgvVehicleProviderReturnsNull_ShouldThrowException()
        throws Exception {
        DvlaVehicle dvlaVehicleEntity = new DvlaVehicle();
        Optional<VehicleWithLatestTest> dvlaVehicle = Optional.of(new DvlaVehicleWithLatestTest(dvlaVehicleEntity, null));
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(null);

        catchException(motrVehicleHistoryProvider).searchForCommercialVehicleByRegistration(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No HGV/PSV vehicle found for registration " + REGISTRATION)),
                hasNoCause()
        ));
    }

    @Test
    public void searchForCommercialVehicleByRegistration_WhenGetHgvPsvVehicleFound_ShouldReturnVehicle() throws Exception {
        DvlaVehicle dvlaVehicleEntity = new DvlaVehicle();
        Optional<VehicleWithLatestTest> dvlaVehicle = Optional.of(new DvlaVehicleWithLatestTest(dvlaVehicleEntity, null));
        Vehicle hgvPsvVehicle = buildHgvPsvVehicle();

        when(motrReadService.getLatestMotTestByRegistration(REGISTRATION)).thenReturn(Optional.empty());
        when(motrReadService.getLatestMotTestForDvlaVehicleByRegistration(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(REGISTRATION))).thenReturn(hgvPsvVehicle);

        VehicleWithLatestTest result = motrVehicleHistoryProvider.searchForCommercialVehicleByRegistration(REGISTRATION);

        assertCorrectHgvPSvVehicle(hgvPsvVehicle, result);
    }

    @Test
    public void getDvlaVehicleWithTestByDvlaVehicleId_WhenMotrReadServiceReturnsNull_ShouldThrowException()
    throws Exception {
        when(motrReadService.getLatestMotTestByDvlaVehicleId(DVLA_VEHICLE_ID)).thenReturn(Optional.empty());

        catchException(motrVehicleHistoryProvider).getDvlaVehicleWithTestByDvlaVehicleId(DVLA_VEHICLE_ID);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No MOT Test or DVLA vehicle found for DVLA vehicle id " + DVLA_VEHICLE_ID)),
                hasNoCause()
        ));

    }

    @Test
    public void getDvlaVehicleWithTestByDvlaVehicleId_WhenMotrReadServiceReturnsVehicle_ShouldReturnIt()
        throws Exception {
        VehicleWithLatestTest vehicle = mock(VehicleWithLatestTest.class);
        when(motrReadService.getLatestMotTestByDvlaVehicleId(DVLA_VEHICLE_ID)).thenReturn(Optional.of(vehicle));

        VehicleWithLatestTest result = motrVehicleHistoryProvider.getDvlaVehicleWithTestByDvlaVehicleId(DVLA_VEHICLE_ID);

        assertThat(result, sameInstance(vehicle));
    }

    @Test
    public void getVehicleByMotTestNumber_WhenMotrReadServiceReturnsNull_ShouldThrowException() throws Exception {
        when(motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(MOT_TEST_NUMBER))
            .thenReturn(Optional.empty());

        catchException(motrVehicleHistoryProvider)
            .getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(MOT_TEST_NUMBER);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("No MOT Tests found with number: " + Long.toString(MOT_TEST_NUMBER))),
                hasNoCause()
        ));
    }



    private Vehicle buildHgvPsvVehicle() {
        Vehicle hgvPsvVehicle = new Vehicle();
        hgvPsvVehicle.setVehicleType("PSV");
        hgvPsvVehicle.setVehicleIdentifier(REGISTRATION);
        return hgvPsvVehicle;
    }

    private void assertCorrectHgvPSvVehicle(Vehicle hgvPsvVehicle, VehicleWithLatestTest result) {
        assertEquals(hgvPsvVehicle.getVehicleIdentifier(), result.getRegistration());
        assertEquals(hgvPsvVehicle.getVehicleType(), result.getVehicleType());
    }

    private void assertCorrectMotVehicle(uk.gov.dvsa.mot.vehicle.api.Vehicle vehicle, VehicleWithLatestTest result) {
        assertEquals(vehicle.getRegistration(), result.getRegistration());
        assertEquals(VehicleType.MOT.name(), result.getVehicleType());
    }
}