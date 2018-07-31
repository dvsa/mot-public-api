package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class to debug jersey routing after proxy LambdaHandler invocation
 */
public class TradeServiceRequestHandlerTest {

    @Test
    public void testApiVersion() {
        LambdaHandler handler = new LambdaHandler();
        Context context = new TestContext();
        ApiGatewayRequestContext requestContext = new ApiGatewayRequestContext();
        requestContext.setIdentity(new ApiGatewayRequestIdentity());

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json+v4");

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("registration", "AC80UNS");

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHeaders(headers);
        request.setQueryStringParameters(queryParams);
        request.setPath("/trade/vehicles/mot-tests");
        request.setRequestContext(requestContext);
        request.setHttpMethod("GET");

        AwsProxyResponse resp = handler.handleRequest(request, context);

        String body = resp.getBody();
        int code = resp.getStatusCode();
    }
}
