package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.util.List;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTradeMotHistoryByVehicleId {
    private static TradeServiceRequest input = new TradeServiceRequest();

    private Context createContext() {

        TestContext ctx = new TestContext();

        ctx.setFunctionName("TradeHandler");

        return ctx;
    }

    @Test
    public void testTradeHandler() {

        try {
            TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
            Context ctx = createContext();

            List<Vehicle> output = tradeServiceRequestHandler.getTradeMotTests(input, ctx);

            if (output != null) {
                System.out.println(output.toString());
            }
        } catch (TradeException e) {
            e.printStackTrace();
        }
    }
}
