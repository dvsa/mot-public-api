package uk.gov.dvsa.mot.trade.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MotTestV2Response extends MotTestResponse {

    private String odometerResultType;
    private List<RfrAndAdvisoryV2Response> rfrAndComments;

    public String getOdometerResultType() {

        return odometerResultType;
    }

    public void setOdometerResultType(String odometerResultType) {

        this.odometerResultType = odometerResultType;
    }

    public List<RfrAndAdvisoryV2Response> getRfrAndComments() {

        return rfrAndComments;
    }

    public void setRfrAndComments(List<RfrAndAdvisoryV2Response> rfrAndComments) {

        this.rfrAndComments = rfrAndComments;
    }
}
