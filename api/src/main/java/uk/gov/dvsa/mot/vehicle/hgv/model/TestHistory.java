package uk.gov.dvsa.mot.vehicle.hgv.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestHistory {
    private String testType;
    private String location;
    private String testResult;
    private String testDate;
    private String testCertificateExpiryDateAtTest;
    private Integer numberOfDefectsAtTest;
    private Integer numberOfAdvisoryDefectsAtTest;
    private String vehicleIdentifierAtTest;
    private String testCertificateSerialNo;

    public String getTestType() {
        return testType;
    }

    public String getTestCertificateSerialNo() {
        return testCertificateSerialNo;
    }

    public void setTestCertificateSerialNo(String testCertificateSerialNo) {
        this.testCertificateSerialNo = testCertificateSerialNo;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestCertificateExpiryDateAtTest() {
        return testCertificateExpiryDateAtTest;
    }

    public void setTestCertificateExpiryDateAtTest(String testCertificateExpiryDateAtTest) {
        this.testCertificateExpiryDateAtTest = testCertificateExpiryDateAtTest;
    }

    public Integer getNumberOfDefectsAtTest() {
        return numberOfDefectsAtTest;
    }

    public void setNumberOfDefectsAtTest(Integer numberOfDefectsAtTest) {
        this.numberOfDefectsAtTest = numberOfDefectsAtTest;
    }

    public Integer getNumberOfAdvisoryDefectsAtTest() {
        return numberOfAdvisoryDefectsAtTest;
    }

    public void setNumberOfAdvisoryDefectsAtTest(Integer numberOfAdvisoryDefectsAtTest) {
        this.numberOfAdvisoryDefectsAtTest = numberOfAdvisoryDefectsAtTest;
    }

    public String getVehicleIdentifierAtTest() {
        return vehicleIdentifierAtTest;
    }

    public void setVehicleIdentifierAtTest(String vehicleIdentifierAtTest) {
        this.vehicleIdentifierAtTest = vehicleIdentifierAtTest;
    }
}
