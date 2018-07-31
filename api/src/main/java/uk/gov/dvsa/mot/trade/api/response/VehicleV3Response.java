package uk.gov.dvsa.mot.trade.api.response;

import java.util.List;

public class VehicleV3Response extends VehicleResponse {
    private String motTestDueDate;
    private List<MotTestV2Response> motTests;

    public String getMotTestDueDate() {
        return motTestDueDate;
    }

    public void setMotTestDueDate(String motTestDueDate) {
        this.motTestDueDate = motTestDueDate;
    }

    public List<MotTestV2Response> getMotTests() {
        return motTests;
    }

    public void setMotTests(List<MotTestV2Response> motTests) {
        this.motTests = motTests;
    }
}
