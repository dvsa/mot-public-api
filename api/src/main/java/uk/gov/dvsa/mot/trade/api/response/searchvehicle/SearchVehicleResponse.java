package uk.gov.dvsa.mot.trade.api.response.searchvehicle;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class SearchVehicleResponse {

    private String registration;
    private String make;
    private String model;
    private String manufactureDate;
    private String vehicleType;
    private String vehicleClass;
    private String registrationDate;
    private String annualTestExpiryDate;
    private List<AnnualTestResponse> annualTests;

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
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

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<AnnualTestResponse> getAnnualTests() {
        return annualTests;
    }

    public void setAnnualTests(List<AnnualTestResponse> annualTests) {
        this.annualTests = annualTests;
    }

    public String getAnnualTestExpiryDate() {
        return annualTestExpiryDate;
    }

    public void setAnnualTestExpiryDate(String annualTestExpiryDate) {
        this.annualTestExpiryDate = annualTestExpiryDate;
    }
}
