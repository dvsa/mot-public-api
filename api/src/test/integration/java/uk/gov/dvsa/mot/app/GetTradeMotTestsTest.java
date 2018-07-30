package uk.gov.dvsa.mot.app;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.TradeException;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTradeMotTestsTest {

    private static String make;
    private static String registration;

    @BeforeClass
    public static void createInput() throws IOException {
        registration = "REGI";
        make = "MAKE";
    }

    private ContainerRequestContext createContext() {

        ContainerRequestContext ctx = new RequestContext();

        ctx.setMethod("TradeHandler");

        return ctx;
    }

    @Test
    public void testTradeHandler() {

        try {
            TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
            ContainerRequestContext ctx = createContext();

            List<?> output = (List<?>) tradeServiceRequestHandler.getTradeMotTestsLegacy(registration,
                    make, ctx).getEntity();

            if (output != null) {
                System.out.println(output.toString());
            }
        } catch (TradeException e) {
            e.printStackTrace();
        }
    }
}
