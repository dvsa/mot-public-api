package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.dvsa.mot.persist.model.DvlaVehicle;
import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.io.IOException;
import java.util.Collections;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessage;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasNoCause;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MotrRequestHandlerTest {

    private static final String AWS_REQUEST_ID = "123456";
    private static final String REGISTRATION = "REG123456";
    private static final String VIN = "VIN123456";

    @Mock
    private VehicleReadService vehicleReadService;

    @Mock
    private HgvVehicleProvider hgvVehicleProvider;

    @Mock
    private Context context;

    @InjectMocks
    private MotrRequestHandler motrRequestHandler = new MotrRequestHandler();

    @Before
    public void setUp() throws IOException {

        when(context.getAwsRequestId()).thenReturn(AWS_REQUEST_ID);
    }

    @Ignore
    @Test
    public void getVehicle_WithoutRegistrationParam_ShouldThrowException() throws Exception {
        catchException(motrRequestHandler).getVehicle("");

        assertThat(caughtException(), allOf(
                instanceOf(BadRequestException.class),
                hasMessageThat(containsString("Registration param is missing")),
                hasNoCause()
        ));
    }

    @Ignore
    @Test
    public void getVehicle_WhenVehicleReadServiceFindByRegistrationReturnsException_ShouldThrowException() throws Exception {
        when(vehicleReadService.findByRegistration(REGISTRATION)).thenThrow(new IndexOutOfBoundsException());

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(Exception.class)
        ));
    }

    @Ignore
    @Test
    public void getVehicle_WhenVehicleReadServiceGetDvlaVehicleByRegistrationWithVinReturnsException_ShouldThrowException()
            throws Exception {
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenThrow(new IndexOutOfBoundsException());

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(Exception.class)
        ));
    }

    @Ignore
    @Test
    public void getVehicle_WhenMotVehicleIsPresent_ShouldThrowException() throws Exception {
        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(Collections.singletonList(new Vehicle()));

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(Exception.class)
        ));
    }

    @Ignore
    @Test
    public void getVehicle_WhenDvlaVehicleIsNotPresent_ShouldThrowException() throws Exception {
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(null);

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(Exception.class)
        ));
    }

    @Ignore
    @Test
    public void getVehicle_WhenGetHgvPsvVehicleNotReturnVehicle_ShouldThrowException() throws Exception {
        DvlaVehicle dvlaVehicle = new DvlaVehicle();
        dvlaVehicle.setVin(VIN);

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(VIN))).thenReturn(null);

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(Exception.class),
                hasMessage("No HGV/PSV vehicle retrieved - MotrRequestHandler - getVehicle()")
        ));
    }


    @Ignore
    @Test
    public void getVehicle_WhenVrmNotMatches_ShouldThrowException() throws Exception {
        DvlaVehicle dvlaVehicle = new DvlaVehicle();
        dvlaVehicle.setVin(VIN);
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier("000");

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(VIN))).thenReturn(vehicle);

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(Exception.class),
                hasMessageThat(containsString("VRM mismatch between user input and HGV api;"))
        ));
    }

    @Ignore
    @Test
    public void getVehicle_WhenVehicleTestHistoryIsEmptyAndRegistrationDateIsNull_ShouldThrowException() throws Exception {
        DvlaVehicle dvlaVehicle = new DvlaVehicle();
        dvlaVehicle.setVin(VIN);
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier(VIN);
        vehicle.setTestHistory(new TestHistory[0]);

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(VIN))).thenReturn(vehicle);

        catchException(motrRequestHandler).getVehicle(REGISTRATION);

        assertThat(caughtException(), allOf(
                instanceOf(Exception.class),
                hasMessageThat(containsString("Registration date is null or empty"))
        ));
    }

    @Ignore
    @Test
    public void getVehicle_WhenVehicleTestHistoryIsNotEmpty_AnnualTestExpiryDateShouldBeSetToTestDate() throws Exception {
        TestHistory[] testHistory = new TestHistory[1];
        TestHistory historyItem =  new TestHistory();
        historyItem.setTestDate("2018-01-01");
        testHistory[0] = historyItem;

        DvlaVehicle dvlaVehicle = new DvlaVehicle();
        dvlaVehicle.setVin(VIN);
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();
        vehicle.setVehicleIdentifier(VIN);
        vehicle.setTestHistory(testHistory);

        when(vehicleReadService.findByRegistration(REGISTRATION)).thenReturn(null);
        when(vehicleReadService.getDvlaVehicleByRegistrationWithVin(REGISTRATION)).thenReturn(dvlaVehicle);
        when(hgvVehicleProvider.getVehicle(eq(VIN))).thenReturn(vehicle);

        AwsProxyResponse response = motrRequestHandler.getVehicle(REGISTRATION);

        assertTrue(response.getBody().contains("2018-01-01"));
    }
}
