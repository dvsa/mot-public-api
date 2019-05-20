package uk.gov.dvsa.mot.vehicle.hgv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Vehicle {
    @JsonIgnore
    private List<TestHistory> testHistory;
    private String vehicleClass;
    private String vehicleType;
    private String chassisType;
    private String manufactureDate;
    private Integer yearOfManufacture;
    private String testCertificateExpiryDate;
    private String outOfTestMarker;
    private String vehicleIdentifier;
    private String previousRegmark;
    private String registrationDate;
    private Integer numberOfAxles;
    private String make;
    private String model;

    public void setTestHistory(List<TestHistory> testHistory) {
        this.testHistory = testHistory;
    }

    public List<TestHistory> getTestHistory() {
        return testHistory;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getChassisType() {
        return chassisType;
    }

    public void setChassisType(String chassisType) {
        this.chassisType = chassisType;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Integer getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(Integer yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getTestCertificateExpiryDate() {
        return testCertificateExpiryDate;
    }

    public void setTestCertificateExpiryDate(String testCertificateExpiryDate) {
        this.testCertificateExpiryDate = testCertificateExpiryDate;
    }

    public String getOutOfTestMarker() {
        return outOfTestMarker;
    }

    public void setOutOfTestMarker(String outOfTestMarker) {
        this.outOfTestMarker = outOfTestMarker;
    }

    public String getVehicleIdentifier() {
        return vehicleIdentifier;
    }

    public void setVehicleIdentifier(String vehicleIdentifier) {
        this.vehicleIdentifier = vehicleIdentifier;
    }

    public String getPreviousRegmark() {
        return previousRegmark;
    }

    public void setPreviousRegmark(String previousRegmark) {
        this.previousRegmark = previousRegmark;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Integer getNumberOfAxles() {
        return numberOfAxles;
    }

    public void setNumberOfAxles(Integer numberOfAxles) {
        this.numberOfAxles = numberOfAxles;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
