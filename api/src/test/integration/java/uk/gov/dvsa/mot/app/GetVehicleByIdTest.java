package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.gov.dvsa.mot.app.VehicleServiceRequestHandler;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;

import java.io.IOException;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */

// TODO: This test needs cleanning up.
public class GetVehicleByIdTest {

    private static Integer input;

    @BeforeClass
    public static void createInput() throws IOException {

        input = 89608788;
        // input = new Vehicle() ;
        // input.setRegistration( "Y551YFP" ) ;
        // input.setMake( "Ford" ) ;
    }

    private Context createContext() {

        TestContext ctx = new TestContext();

        ctx.setFunctionName("TradeHandler");

        return ctx;
    }

    @Test
    public void testTradeHandler() {

        try {
            VehicleServiceRequestHandler vehicleServiceRequestHandler = new VehicleServiceRequestHandler();
            Context ctx = createContext();

            Vehicle output = vehicleServiceRequestHandler.getVehicleById(input, ctx);

            if (output != null) {
                System.out.println(output.toString());
            }
        } catch (TradeException e) {
            e.printStackTrace();
        }
    }
}
