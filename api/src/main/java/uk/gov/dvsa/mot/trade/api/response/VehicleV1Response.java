package uk.gov.dvsa.mot.trade.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleV1Response extends VehicleResponse {
    private String motTestExpiryDate;
    private List<MotTestV1Response> motTests;

    public String getMotTestExpiryDate() {
        return motTestExpiryDate;
    }

    public void setMotTestExpiryDate(String motTestExpiryDate) {
        this.motTestExpiryDate = motTestExpiryDate;
    }


    public List<MotTestV1Response> getMotTests() {
        return motTests;
    }

    public void setMotTests(List<MotTestV1Response> motTests) {
        this.motTests = motTests;
    }
}
