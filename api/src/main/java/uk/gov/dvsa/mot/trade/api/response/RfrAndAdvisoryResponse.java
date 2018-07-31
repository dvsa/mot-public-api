package uk.gov.dvsa.mot.trade.api.response;

public class RfrAndAdvisoryResponse {

    private String text;
    private String type;

    public String getText() {

        return text;
    }

    public RfrAndAdvisoryResponse setText(String text) {

        this.text = text;
        return this;
    }

    public String getType() {

        return type;
    }

    public RfrAndAdvisoryResponse setType(String type) {

        this.type = type;
        return this;
    }
}
