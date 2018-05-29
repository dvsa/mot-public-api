package uk.gov.dvsa.mot.vehicle.hgv.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HgvPsvVehicle {

    private String make;
    private String model;
    private String primaryColour;
    private String secondaryColour;
    private String registration;
    private String vin;
    private String vehicleType;
    private String manufactureYear;
    private String motTestExpiryDate;
    private String motTestNumber;
    private String dvlaId;

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

    public String getPrimaryColour() {
        return primaryColour;
    }

    public void setPrimaryColour(String primaryColour) {
        this.primaryColour = primaryColour;
    }

    public String getSecondaryColour() {
        return secondaryColour;
    }

    public void setSecondaryColour(String secondaryColour) {
        this.secondaryColour = secondaryColour;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getMotTestExpiryDate() {
        return motTestExpiryDate;
    }

    public void setMotTestExpiryDate(String motTestExpiryDate) {
        this.motTestExpiryDate = motTestExpiryDate;
    }

    public String getMotTestNumber() {
        return motTestNumber;
    }

    public void setMotTestNumber(String motTestNumber) {
        this.motTestNumber = motTestNumber;
    }

    public String getDvlaId() {
        return dvlaId;
    }

    public void setDvlaId(String dvlaId) {
        this.dvlaId = dvlaId;
    }
}
