package uk.gov.dvsa.mot.app;

import org.junit.Ignore;
import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTradeMotTestsByRegistrationAndMake {

    private TradeServiceRequest createInput(String registration, String make) throws IOException {
        TradeServiceRequest input = new TradeServiceRequest();
        TradeServiceRequest.MotTestQueryParams queryParams = input.new MotTestQueryParams();

        if (registration != null) {
            queryParams.setRegistration(registration);
        }

        if (make != null) {
            queryParams.setMake(make);
        }

        input.setQueryParams(queryParams);

        return input;
    }

    private ContainerRequestContext createContext() {

        ContainerRequestContext ctx = new RequestContext();

        ctx.setMethod("TradeHandler");

        return ctx;
    }

    //integration tests fail on INT environment, but are still useful locally, so I will leave them ignored
    @Test
    @Ignore
    public void testGetTradeMotTestsReturnsResultsWhenRegistrationAndMakeProvided() throws TradeException, IOException {
        TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
        ContainerRequestContext ctx = createContext();

        TradeServiceRequest input = createInput("FNZ6110", "RENAULT");

        List<?> output = (List<?>) tradeServiceRequestHandler.getTradeMotTests(input.getVehicleId(), input.getNumber(),
                input.getRegistration(), input.getDate(), input.getPage(), ctx).getEntity();

        if (output != null) {
            System.out.println(output.toString());
        }
    }

    //integration tests fail on INT environment, but are still useful locally, so I will leave them ignored
    @Test
    @Ignore
    public void testGetTradeMotTestsReturnsResultsWhenOnlyRegistrationIsProvided() throws TradeException, IOException {
        TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
        ContainerRequestContext ctx = createContext();

        TradeServiceRequest input = createInput("FNZ6110", null);

        List<?> output = (List<?>) tradeServiceRequestHandler.getTradeMotTests(input.getVehicleId(), input.getNumber(),
                input.getRegistration(), input.getDate(), input.getPage(), ctx).getEntity();

        if (output != null) {
            System.out.println(output.toString());
        }
    }

    //integration tests fail on INT environment, but are still useful locally, so I will leave them ignored
    @Test
    @Ignore
    public void testGetTradeMotTestsReturnsDvlaVehicleWhenThereIsNoDvlaVehicle() throws TradeException, IOException {
        TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
        ContainerRequestContext ctx = createContext();

        TradeServiceRequest input = createInput("REG12X7", null);

        List<?> output = (List<?>) tradeServiceRequestHandler.getTradeMotTests(input.getVehicleId(), input.getNumber(),
                input.getRegistration(), input.getDate(), input.getPage(), ctx).getEntity();

        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
