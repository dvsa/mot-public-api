package uk.gov.dvsa.mot.vehicle.hgv.response;


import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;

import java.util.List;

public class ResponseTestHistory {

    List<TestHistory> testHistory;

    public List<TestHistory> getTestHistory() {
        return testHistory;
    }

    public void setTestHistory(List<TestHistory> testHistory) {
        this.testHistory = testHistory;
    }
}
