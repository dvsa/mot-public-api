package uk.gov.dvsa.mot.trade.api.response;

public class MotTestResponse {

    private String completedDate;
    private String testResult;
    private String expiryDate;
    private String odometerValue;
    private String odometerUnit;
    private String motTestNumber;

    public String getCompletedDate() {

        return completedDate;
    }

    public void setCompletedDate(String completedDate) {

        this.completedDate = completedDate;
    }

    public String getTestResult() {

        return testResult;
    }

    public void setTestResult(String testResult) {

        this.testResult = testResult;
    }

    public String getExpiryDate() {

        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {

        this.expiryDate = expiryDate;
    }

    public String getOdometerValue() {

        return odometerValue;
    }

    public void setOdometerValue(String odometerValue) {

        this.odometerValue = odometerValue;
    }

    public String getOdometerUnit() {

        return odometerUnit;
    }

    public void setOdometerUnit(String odometerUnit) {

        this.odometerUnit = odometerUnit;
    }

    public String getMotTestNumber() {

        return motTestNumber;
    }

    public void setMotTestNumber(String motTestNumber) {

        this.motTestNumber = motTestNumber;
    }
}
