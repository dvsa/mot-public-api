package uk.gov.dvsa.mot.trade.api;

public class RfrAndAdvisoryItem {

    private String text;
    private String type;
    private boolean dangerous;
    private String deficiencyCategoryCode;
    private String deficiencyCategoryDescription;

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

    public boolean isDangerous() {

        return dangerous;
    }

    public RfrAndAdvisoryItem setDangerous(boolean dangerous) {

        this.dangerous = dangerous;
        return this;
    }

    public String getDeficiencyCategoryCode() {
        return deficiencyCategoryCode;
    }

    public void setDeficiencyCategoryCode(String deficiencyCategoryCode) {
        this.deficiencyCategoryCode = deficiencyCategoryCode;
    }

    public String getDeficiencyCategoryDescription() {
        return deficiencyCategoryDescription;
    }

    public void setDeficiencyCategoryDescription(String deficiencyCategoryDescription) {
        this.deficiencyCategoryDescription = deficiencyCategoryDescription;
    }
}
