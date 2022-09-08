package uk.gov.dvsa.mot.app.motr;

import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Before;

import uk.gov.dvsa.mot.app.ConfigKeys;
import uk.gov.dvsa.mot.app.LambdaHandler;
import uk.gov.dvsa.mot.app.TestContext;

import java.util.HashMap;
import java.util.Map;

public abstract class MotrHandlerAbstractTest {

    @Before
    public void setup() {
        setSystemProperties();
    }

    protected AwsProxyResponse call(String path) {
        LambdaHandler handler = new LambdaHandler();
        Context context = new TestContext();
        ApiGatewayRequestContext requestContext = new ApiGatewayRequestContext();
        requestContext.setIdentity(new ApiGatewayRequestIdentity());

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHeaders(headers);
        request.setPath(path);
        request.setRequestContext(requestContext);
        request.setHttpMethod("GET");

        return handler.handleRequest(request, context);
    }

    private void setSystemProperties() {
        setPropertyIfMissing(ConfigKeys.MothApiConnectionTimeout, "100");
        setPropertyIfMissing(ConfigKeys.DatabaseConnection, "jdbc:mysql://127.0.0.1:3306/mot2");
        setPropertyIfMissing(ConfigKeys.DatabaseUsername, "motdbuser");
        setPropertyIfMissing(ConfigKeys.DatabasePassword, "password");
        setPropertyIfMissing(ConfigKeys.MothApiUrl, "http://localhost:3000");
        setPropertyIfMissing(ConfigKeys.MothApiKeyEncrypted, "key");
    }

    private void setPropertyIfMissing(String key, String value) {
        if (System.getProperty(key) == null) {
            System.setProperty(key, value);
        }
    }
}
