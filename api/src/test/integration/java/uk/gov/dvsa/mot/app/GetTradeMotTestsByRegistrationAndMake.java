package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import org.junit.Test;

import uk.gov.dvsa.mot.dataprovider.VehicleProvider;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.io.IOException;
import java.util.List;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTradeMotTestsByRegistrationAndMake extends IntegrationTestBase{

    @Inject
    private final VehicleProvider vehicleProvider=null;

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

    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("TradeHandler");

        return ctx;
    }

    //integration tests fail on INT environment, but are still useful locally, so I will leave them ignored
    @Test
    public void testGetTradeMotTestsReturnsResultsWhenRegistrationAndMakeProvided() throws TradeException, IOException {
        TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
        Context ctx = createContext();

        Vehicle vehicle = this.vehicleProvider.getClass4Vehicle();
        TradeServiceRequest input = createInput(vehicle.getRegistration(), vehicle.getMake());

        List<Vehicle> output = tradeServiceRequestHandler.getTradeMotTests(input, ctx);

        if (output != null) {
            System.out.println(output.toString());
        }
    }

    //integration tests fail on INT environment, but are still useful locally, so I will leave them ignored
    @Test
    public void testGetTradeMotTestsReturnsResultsWhenOnlyRegistrationIsProvided() throws TradeException, IOException {
        TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
        Context ctx = createContext();

        TradeServiceRequest input = createInput("FNZ6110", null);

        List<Vehicle> output = tradeServiceRequestHandler.getTradeMotTests(input, ctx);

        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
