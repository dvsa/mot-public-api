package uk.gov.dvsa.mot.vehicle.hgv.response;


import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;

public class ResponseTestHistory {
    TestHistory[] testHistory;

    public TestHistory[] getTestHistory() {
        return testHistory;
    }

    public void setTestHistory(TestHistory[] testHistory) {
        this.testHistory = testHistory;
    }
}
