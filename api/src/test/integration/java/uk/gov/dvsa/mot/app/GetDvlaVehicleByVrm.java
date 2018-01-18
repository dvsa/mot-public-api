package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.MotrResponse;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;

import java.io.IOException;


public class GetDvlaVehicleByVrm {

    private static TradeServiceRequest tradeServiceRequest = new TradeServiceRequest();

    @BeforeClass
    public static void createInput() throws IOException {

        String input = "AF16MMO";
        TradeServiceRequest.MotTestPathParams pathParams = new TradeServiceRequest().new MotTestPathParams();
        pathParams.setRegistration(input);
        tradeServiceRequest.setPathParams(pathParams);
    }

    private Context createContext() {

        TestContext ctx = new TestContext();

        ctx.setFunctionName("TradeHandler");

        return ctx;
    }

    @Test
    public void testTradeHandler() throws TradeException {

        TradeServiceRequestHandler tradeServiceRequestHandler = new TradeServiceRequestHandler();
        Context ctx = createContext();

        MotrResponse output = tradeServiceRequestHandler.getLatestMotTest(tradeServiceRequest, ctx);

        // until integration framework doing this is only option as it is reliant on PP data
        if (output != null) {
            System.out.println(output.getMotTestExpiryDate());
        }
    }
}
