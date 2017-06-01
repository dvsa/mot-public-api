package uk.gov.dvsa.mot.app;

import uk.gov.dvsa.mot.app.TradeServiceRequestHandler;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import com.amazonaws.services.lambda.runtime.Context;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

//import java.util.List ;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTradeMotHistoryByDateRange {
    private static TradeServiceRequest input = new TradeServiceRequest();

    @BeforeClass
    public static void createInput() throws IOException {

        try {
            input.setDate("20160624");
            input.setPage(0);
            input.setPages(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
