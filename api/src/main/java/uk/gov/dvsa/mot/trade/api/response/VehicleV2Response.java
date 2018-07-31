package uk.gov.dvsa.mot.trade.api.response;

import java.util.List;

public class VehicleV2Response extends VehicleResponse {
    private String motTestExpiryDate;
    private List<MotTestV2Response> motTests;

    public String getMotTestExpiryDate() {
        return motTestExpiryDate;
    }

    public void setMotTestExpiryDate(String motTestExpiryDate) {
        this.motTestExpiryDate = motTestExpiryDate;
    }

    public List<MotTestV2Response> getMotTests() {
        return motTests;
    }

    public void setMotTests(List<MotTestV2Response> motTests) {
        this.motTests = motTests;
    }
}
