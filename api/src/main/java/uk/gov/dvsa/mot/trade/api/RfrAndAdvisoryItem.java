package uk.gov.dvsa.mot.trade.api;

public class RfrAndAdvisoryItem {

    private String text;
    private String type;

    public RfrAndAdvisoryItem() {

    }

    public String getText() {

        return text;
    }

    public RfrAndAdvisoryItem setText(String text) {

        this.text = text;
        return this;
    }

    public String getType() {

        return type;
    }

    public RfrAndAdvisoryItem setType(String type) {

        this.type = type;
        return this;
    }
}
