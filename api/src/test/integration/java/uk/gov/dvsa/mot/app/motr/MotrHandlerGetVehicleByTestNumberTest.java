package uk.gov.dvsa.mot.app.motr;

import com.amazonaws.serverless.proxy.model.AwsProxyResponse;

import org.junit.Test;

/**
 * Manual test for API used by MOTR.
 *
 * Test uses local test numbers - you have to provide your own data.
 */
public class MotrHandlerGetVehicleByTestNumberTest extends MotrHandlerAbstractTest {

    @Test
    public void testMot() {
        executeCall("496949931299");
    }

    private void executeCall(String testNumber) {
        AwsProxyResponse resp = call("motr/v2/search/mot-test/" + testNumber);

        String body = resp.getBody();
        int code = resp.getStatusCode();

        System.out.println("Response code: " + code);
        System.out.println("Response body: " + body);
    }
}
