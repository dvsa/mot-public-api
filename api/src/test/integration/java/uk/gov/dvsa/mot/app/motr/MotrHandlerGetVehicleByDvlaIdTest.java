package uk.gov.dvsa.mot.app.motr;

import com.amazonaws.serverless.proxy.model.AwsProxyResponse;

import org.junit.Test;

/**
 * Manual test for API used by MOTR.
 * Test uses anonymised dataset and HgvPsvMock data.
 */
public class MotrHandlerGetVehicleByDvlaIdTest extends MotrHandlerAbstractTest {

    @Test
    public void testMot() {
        executeCall("607155");
    }

    private void executeCall(String dvlaId) {
        AwsProxyResponse resp = call("motr/v2/search/dvla-id/" + dvlaId);

        String body = resp.getBody();
        int code = resp.getStatusCode();

        System.out.println("Response code: " + code);
        System.out.println("Response body: " + body);
    }
}
