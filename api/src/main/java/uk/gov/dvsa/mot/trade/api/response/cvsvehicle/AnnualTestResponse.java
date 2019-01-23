package uk.gov.dvsa.mot.trade.api.response.cvsvehicle;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AnnualTestResponse {

    private String testDate;
    private String testType;
    private String testResult;
    private String testCertificateNumber;
    private String expiryDate;
    private String numberOfDefectsAtTest;
    private String numberOfAdvisoryDefectsAtTest;
    private List<DefectResponse> defects;

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestCertificateNumber() {
        return testCertificateNumber;
    }

    public void setTestCertificateNumber(String testCertificateNumber) {
        this.testCertificateNumber = testCertificateNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getNumberOfDefectsAtTest() {
        return numberOfDefectsAtTest;
    }

    public void setNumberOfDefectsAtTest(String numberOfDefectsAtTest) {
        this.numberOfDefectsAtTest = numberOfDefectsAtTest;
    }

    public String getNumberOfAdvisoryDefectsAtTest() {
        return numberOfAdvisoryDefectsAtTest;
    }

    public void setNumberOfAdvisoryDefectsAtTest(String numberOfAdvisoryDefectsAtTest) {
        this.numberOfAdvisoryDefectsAtTest = numberOfAdvisoryDefectsAtTest;
    }

    public List<DefectResponse> getDefects() {
        return defects;
    }

    public void setDefects(List<DefectResponse> defects) {
        this.defects = defects;
    }
}
