package uk.gov.dvsa.mot.trade.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleResponse {

    private String registration;
    private String make;
    private String model;
    private String makeInFull;
    private String firstUsedDate;
    private String manufactureYear;
    private String fuelType;
    private String primaryColour;
    private String secondaryColour;
    private String dvlaId;

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

    public String getMakeInFull() {

        return makeInFull;
    }

    public void setMakeInFull(String makeInFull) {

        this.makeInFull = makeInFull;
    }

    public String getFirstUsedDate() {

        return firstUsedDate;
    }

    public void setFirstUsedDate(String firstUsedDate) {

        this.firstUsedDate = firstUsedDate;
    }

    public String getFuelType() {

        return fuelType;
    }

    public void setFuelType(String fuelType) {

        this.fuelType = fuelType;
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

    public String getManufactureYear() {

        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {

        this.manufactureYear = manufactureYear;
    }

    public String getDvlaId() {

        return dvlaId;
    }

    public void setDvlaId(String dvlaId) {

        this.dvlaId = dvlaId;
    }
}
