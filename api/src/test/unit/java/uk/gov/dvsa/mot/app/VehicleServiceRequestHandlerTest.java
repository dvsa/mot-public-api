package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

public class VehicleServiceRequestHandlerTest {
    @Mock
    VehicleReadService vehicleReadService;

    @Mock
    Context lambdaContext;

    public VehicleServiceRequestHandlerTest() {

        MockitoAnnotations.initMocks(this);
    }

    /**
     * When no vehicle is returned from the database, getVehicleById should return
     * null.
     */
    @Test(expected = InvalidResourceException.class)
    public void getVehicleById_VehicleDoesNotExist() throws Throwable {

        final int vehicleId = 19;

        when(vehicleReadService.getVehicleById(vehicleId)).thenReturn(null);

        when(lambdaContext.getAwsRequestId()).thenReturn("4");

        VehicleServiceRequestHandler sut = new VehicleServiceRequestHandler(false);
        sut.setVehicleReadService(vehicleReadService);

        sut.getVehicleById(vehicleId, lambdaContext);
    }

    @Test
    public void getVehicleById_VehicleExists() throws TradeException {

        final int vehicleId = 22;

        // this vehicle will be returned by the read service
        Vehicle vehicle = new Vehicle();

        // configure mocks
        when(vehicleReadService.getVehicleById(vehicleId)).thenReturn(vehicle);
        when(lambdaContext.getAwsRequestId()).thenReturn("4");

        // create test subject
        VehicleServiceRequestHandler sut = new VehicleServiceRequestHandler(false);
        sut.setVehicleReadService(vehicleReadService);

        // call method under test
        Vehicle returnedVehicle = sut.getVehicleById(vehicleId, lambdaContext);

        // check that the vehicle we received is the same vehicle we got from the
        // read service
        assertSame(vehicle, returnedVehicle);
    }

    /**
     * Any unexpected exception types should result in an
     * InternalServerErrorException
     */
    @Test(expected = InternalServerErrorException.class)
    public void getVehicleById_HandlesUnexpectedExceptions() throws TradeException {

        final int vehicleId = 22;

        // configure mocks
        when(vehicleReadService.getVehicleById(vehicleId)).thenThrow(new IndexOutOfBoundsException());

        // create test subject
        VehicleServiceRequestHandler sut = new VehicleServiceRequestHandler(false);
        sut.setVehicleReadService(vehicleReadService);

        // call method, which should throw
        sut.getVehicleById(vehicleId, lambdaContext);
    }

    /**
     * We expect the default constructor to throw when it tries to talk to a MySQL
     * server which doesn't exist
     */
    //@Test(expected = ProvisionException.class)
    public void defaultConstructor_Throws() throws Exception {

        new VehicleServiceRequestHandler();
    }
}