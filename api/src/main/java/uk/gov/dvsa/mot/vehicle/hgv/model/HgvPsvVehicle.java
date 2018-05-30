package uk.gov.dvsa.mot.vehicle.hgv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.internal.Nullable;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HgvPsvVehicle {

    private String make;
    private String model;
    private String registration;
    private String vehicleType;
    private String manufactureYear;
    private String motTestExpiryDate;
    private String motTestNumber;
    private String dvlaId;

    @Nullable
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Nullable
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @NotNull
    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    @NotNull
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Nullable
    public String getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    @NotNull
    public String getMotTestExpiryDate() {
        return motTestExpiryDate;
    }

    public void setMotTestExpiryDate(String motTestExpiryDate) {
        this.motTestExpiryDate = motTestExpiryDate;
    }

    @Nullable
    public String getMotTestNumber() {
        return motTestNumber;
    }

    public void setMotTestNumber(String motTestNumber) {
        this.motTestNumber = motTestNumber;
    }

    @Nullable
    public String getDvlaId() {
        return dvlaId;
    }

    public void setDvlaId(String dvlaId) {
        this.dvlaId = dvlaId;
    }
}
