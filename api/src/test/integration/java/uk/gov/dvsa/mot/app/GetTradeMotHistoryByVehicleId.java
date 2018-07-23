package uk.gov.dvsa.mot.app;

import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTradeMotHistoryByVehicleId {
    private static TradeServiceRequest input = new TradeServiceRequest();

    private ContainerRequestContext createContext() {

        RequestContext ctx = new RequestContext();
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("Accept", Arrays.asList("application/json+v3"));
        ctx.setHeaders(headers);

        ctx.setMethod("TradeHandler");

        return ctx;
    }

    @Test
    public void testTradeHandler() {

        try {
            TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
            ContainerRequestContext ctx = createContext();

            input.setVehicleId(59914010);

            List<?> output = (List<?>) tradeServiceRequestHandler.getTradeMotTests(input.getVehicleId(), input.getNumber(),
                    input.getRegistration(), input.getDate(), input.getPage(), ctx).getEntity();

            if (output != null) {
                System.out.println(output.toString());
            }
        } catch (TradeException e) {
            e.printStackTrace();
        }
    }
}
