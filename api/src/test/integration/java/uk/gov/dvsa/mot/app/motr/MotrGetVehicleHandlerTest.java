package uk.gov.dvsa.mot.app.motr;

import com.amazonaws.serverless.proxy.model.AwsProxyResponse;

import org.junit.Test;

/**
 * Manual test for API used by MOTR.
 * Test uses anonymised dataset and HgvPsvMock data.
 */
public class MotrGetVehicleHandlerTest extends MotrHandlerAbstractTest {

    @Test
    public void testMot() {
        executeCall("ABC123");
    }

    @Test
    public void testHgv() {
        executeCall("INVTHGV");
    }

    @Test
    public void testPsv() {
        executeCall("INVTPSV");
    }

    @Test
    public void testPsvWithoutHistory() {
        executeCall("NOHIST1");
    }

    @Test
    public void testEmpty() {
        executeCall("AA32AAW");
    }

    private void executeCall(String registration) {
        AwsProxyResponse resp = call("motr/v2/search/registration/" + registration);

        String body = resp.getBody();
        int code = resp.getStatusCode();

        System.out.println("Response code: " + code);
        System.out.println("Response body: " + body);
    }
}
