package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test for API used by MOTR.
 * Test uses anonymised dataset and HgvPsvMock data.
 */
public class MotrRequestHandlerTest {

    @Before
    public void setup() {
        setSystemProperties();
    }

    @Test
    public void testMot() {
        executeCall("ABC123");
    }

    @Test
    public void testHgv() {
        executeCall("INVTHGV");
    }

    @Test
    public void testPgv() {
        executeCall("INVTPSV");
    }

    public void testPsvWithoutHistory() {
        executeCall("NOHIST1");
    }

    @Test
    public void testEmpty() {
        executeCall("AA32AAW");
    }

    private void executeCall(String registration) {
        LambdaHandler handler = new LambdaHandler();
        Context context = new TestContext();
        ApiGatewayRequestContext requestContext = new ApiGatewayRequestContext();
        requestContext.setIdentity(new ApiGatewayRequestIdentity());

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHeaders(headers);
        request.setPath("motr/v2/search/registration/" + registration);
        request.setRequestContext(requestContext);
        request.setHttpMethod("GET");

        AwsProxyResponse resp = handler.handleRequest(request, context);

        String body = resp.getBody();
        int code = resp.getStatusCode();

        System.out.println("Response code: " + code);
        System.out.println("Response body: " + body);
    }

    private void setSystemProperties() {
        setPropertyIfMissing(ConfigKeys.SearchApiConnectionTimeout, "100");
        setPropertyIfMissing(ConfigKeys.DatabaseConnection, "jdbc:mysql://127.0.0.1:3306/mot2");
        setPropertyIfMissing(ConfigKeys.DatabaseUsername, "motdbuser");
        setPropertyIfMissing(ConfigKeys.DatabasePassword, "password");
        setPropertyIfMissing(ConfigKeys.SearchApiUrl, "http://localhost:3000");
        setPropertyIfMissing(ConfigKeys.SearchApiKeyEncrypted, "key");
    }

    private void setPropertyIfMissing(String key, String value) {
        if (System.getProperty(key) == null) {
            System.setProperty(key, value);
        }
    }
}
