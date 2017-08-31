package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;

import java.io.IOException;
import java.util.List;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTradeMotTestsTest {

    private static TradeServiceRequest input = new TradeServiceRequest();

    @BeforeClass
    public static void createInput() throws IOException {
        // input.setId( 729084574L ) ;
        input.setId(900000000L);
        TradeServiceRequest.MotTestPathParams pathParams = new TradeServiceRequest().new MotTestPathParams();
        pathParams.setRegistration("REGI");
        input.setPathParams(pathParams);
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

            List<DisplayMotTestItem> output = tradeServiceRequestHandler.getTradeMotTestsLegacy(input, ctx);

            if (output != null) {
                System.out.println(output.toString());
            }
        } catch (TradeException e) {
            e.printStackTrace();
        }
    }
}
