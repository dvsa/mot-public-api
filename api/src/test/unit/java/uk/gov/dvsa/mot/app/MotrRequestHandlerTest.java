package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;

import org.apache.http.HttpStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.motr.model.VehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.service.MotrReadService;
import uk.gov.dvsa.mot.motr.service.MotrVehicleHistoryProvider;
import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.MotrResponse;

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
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MotrRequestHandlerTest {

    private static final String REGISTRATION = "REG123456";
    private static final int DVLA_VEHICLE_ID = 213;
    private static final long MOT_TEST_NUMBER = 12345L;

    @Mock
    private ContainerRequestContext containerRequestContext;

    @Mock
    private ApiGatewayRequestContext context;

    @Mock
    private MotrVehicleHistoryProvider motrVehicleHistoryProvider;

    private MotrRequestHandler motrRequestHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(context.getRequestId()).thenReturn("1");
        when(containerRequestContext.getProperty(anyString())).thenReturn(context);

        motrRequestHandler = new MotrRequestHandler(false);
        motrRequestHandler.setMotrVehicleProvider(motrVehicleHistoryProvider);
    }

    // Tests for getVehicle method
    @Test
    public void getVehicle_WithoutRegistrationParam_ShouldThrowException() throws Exception {
        catchException(motrRequestHandler).getVehicle("", containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("Invalid Parameters - missing registration")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_WhenVehicleProviderReturnsException_ShouldThrowException() throws Exception {
        when(motrVehicleHistoryProvider.searchVehicleByRegistration(REGISTRATION))
                .thenThrow(new InvalidResourceException("There was an error retrieving mot vehicle"));

        catchException(motrRequestHandler).getVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("There was an error retrieving mot vehicle")),
                hasNoCause()
        ));
    }

    @Test
    public void getVehicle_SearchVehicleByRegistrationReturnsVehicle_ShouldReturnSuccessResponse() throws Exception {
        VehicleWithLatestTest vehicle = createVehicleWithLatestTest();
        when(motrVehicleHistoryProvider.searchVehicleByRegistration(eq(REGISTRATION))).thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicle(REGISTRATION, containerRequestContext);

        assertEquals(HttpStatus.SC_OK, response.getStatus());
        checkIfVehicleIsCorrect(vehicle, (MotrResponse) response.getEntity());
    }

    // Tests for getCommercialVehicle method
    @Test
    public void getCommercialVehicle_WhenVehicleReadServiceFindByRegistrationReturnsException_ShouldThrowException() throws Exception {
        when(motrVehicleHistoryProvider.searchForCommercialVehicleByRegistration(REGISTRATION))
                .thenThrow(new InvalidResourceException("There was an error retrieving dvla vehicle"));

        catchException(motrRequestHandler).getCommercialVehicle(REGISTRATION, containerRequestContext);

        assertThat(caughtException(), allOf(
                instanceOf(InvalidResourceException.class),
                hasMessageThat(containsString("There was an error retrieving dvla vehicle")),
                hasNoCause()
        ));
    }

    @Test
    public void getCommercialVehicle_WhenProviderReturnsVehicle_ResponseShouldBeCorrect() throws Exception {
        VehicleWithLatestTest vehicle = createVehicleWithLatestTest();
        when(motrVehicleHistoryProvider.searchForCommercialVehicleByRegistration(REGISTRATION))
                .thenReturn(vehicle);

        Response response = motrRequestHandler.getCommercialVehicle(REGISTRATION, containerRequestContext);

        assertEquals(HttpStatus.SC_OK, response.getStatus());

        assertEquals(HttpStatus.SC_OK, response.getStatus());
        checkIfVehicleIsCorrect(vehicle, (MotrResponse) response.getEntity());
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
    public void getVehicleByDvlaId_WhenMotrReadServiceReturnsVehicle_ShouldReturnCorrectResponse() throws Exception {
        VehicleWithLatestTest vehicle = mock(VehicleWithLatestTest.class);
        when(motrVehicleHistoryProvider.getDvlaVehicleWithTestByDvlaVehicleId(DVLA_VEHICLE_ID))
            .thenReturn(vehicle);

        Response response = motrRequestHandler.getVehicleByDvlaId(DVLA_VEHICLE_ID, containerRequestContext);

        assertEquals(response.getStatus(), HttpStatus.SC_OK);
        checkIfVehicleIsCorrect(vehicle, (MotrResponse) response.getEntity());
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
    public void getVehicleByMotTestNumber_WhenMotrReadServiceReturnsVehicle_ShouldReturnCorrectResponse() throws Exception {
        VehicleWithLatestTest vehicleWithLatestTest = createVehicleWithLatestTest();
        when(motrVehicleHistoryProvider.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(MOT_TEST_NUMBER))
                .thenReturn(vehicleWithLatestTest);

        Response response = motrRequestHandler.getVehicleByMotTestNumber(MOT_TEST_NUMBER, containerRequestContext);

        assertEquals(response.getStatus(), HttpStatus.SC_OK);

        checkIfVehicleIsCorrect(vehicleWithLatestTest, (MotrResponse) response.getEntity());
    }

    private VehicleWithLatestTest createVehicleWithLatestTest() {
        VehicleWithLatestTest vehicle = mock(VehicleWithLatestTest.class);
        when(vehicle.getRegistration()).thenReturn(REGISTRATION);
        when(vehicle.getDvlaVehicleId()).thenReturn("123");
        return vehicle;
    }

    private void checkIfVehicleIsCorrect(VehicleWithLatestTest expectedVehicle, MotrResponse actualVehicle) {
        assertEquals(actualVehicle.getRegistration(), expectedVehicle.getRegistration());
        assertSame(actualVehicle.getDvlaId(), expectedVehicle.getDvlaVehicleId());
    }
}
