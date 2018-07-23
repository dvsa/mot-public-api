package uk.gov.dvsa.mot.trade.api.response;

import java.util.List;

public class MotTestV1Response extends MotTestResponse {

    private List<RfrAndAdvisoryResponse> rfrAndComments;

    public List<RfrAndAdvisoryResponse> getRfrAndComments() {

        return rfrAndComments;
    }

    public void setRfrAndComments(List<RfrAndAdvisoryResponse> rfrAndComments) {

        this.rfrAndComments = rfrAndComments;
    }
}
