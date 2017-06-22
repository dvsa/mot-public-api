package uk.gov.dvsa.mot.trade.api;

import java.util.List;

public class DisplayMotTestItem {
    private String registration;
    private String makeName;
    private String modelName;
    private String firstUsedDate;
    private String fuelType;
    private String primaryColour;
    private String completedDate;
    private String testResult;
    private String expiryDate;
    private String odometerValue;
    private String odometerUnit;
    private String motTestNumber;
    private List<RfrAndAdvisoryItem> rfrAndComments;

    public DisplayMotTestItem() {

    }

    public List<RfrAndAdvisoryItem> getRfrAndComments() {

        return rfrAndComments;
    }

    public DisplayMotTestItem setRfrAndComments(List<RfrAndAdvisoryItem> rfrAndComments) {

        this.rfrAndComments = rfrAndComments;
        return this;
    }

    public String getRegistration() {

        return registration;
    }

    public DisplayMotTestItem setRegistration(String registration) {

        this.registration = registration;
        return this;
    }

    public String getMakeName() {

        return makeName;
    }

    public DisplayMotTestItem setMakeName(String makeName) {

        this.makeName = makeName;
        return this;
    }

    public String getModelName() {

        return modelName;
    }

    public DisplayMotTestItem setModelName(String modelName) {

        this.modelName = modelName;
        return this;
    }

    public String getFirstUsedDate() {

        return firstUsedDate;
    }

    public DisplayMotTestItem setFirstUsedDate(String firstUsedDate) {

        this.firstUsedDate = firstUsedDate;
        return this;
    }

    public String getFuelType() {

        return fuelType;
    }

    public DisplayMotTestItem setFuelType(String fuelType) {

        this.fuelType = fuelType;
        return this;
    }

    public String getPrimaryColour() {

        return primaryColour;
    }

    public DisplayMotTestItem setPrimaryColour(String primaryColour) {

        this.primaryColour = primaryColour;
        return this;
    }

    public String getCompletedDate() {

        return completedDate;
    }

    public DisplayMotTestItem setCompletedDate(String completedDate) {

        this.completedDate = completedDate;
        return this;
    }

    public String getTestResult() {

        return testResult;
    }

    public DisplayMotTestItem setTestResult(String testResult) {

        this.testResult = testResult;
        return this;
    }

    public String getExpiryDate() {

        return expiryDate;
    }

    public DisplayMotTestItem setExpiryDate(String expiryDate) {

        this.expiryDate = expiryDate;
        return this;
    }

    public String getOdometerValue() {

        return odometerValue;
    }

    public DisplayMotTestItem setOdometerValue(String odometerValue) {

        this.odometerValue = odometerValue;
        return this;
    }

    public String getMotTestNumber() {

        return motTestNumber;
    }

    public DisplayMotTestItem setMotTestNumber(String motTestNumber) {

        this.motTestNumber = motTestNumber;
        return this;
    }

    public String getOdometerUnit() {

        return odometerUnit;
    }

    public DisplayMotTestItem setOdometerUnit(String odometerUnit) {

        this.odometerUnit = odometerUnit;
        return this;
    }
}
