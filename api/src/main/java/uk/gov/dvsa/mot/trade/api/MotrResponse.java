package uk.gov.dvsa.mot.trade.api;

public class MotrResponse {

    private Integer id;
    private String dvlaId;
    private String registration;
    private String make;
    private String model;
    private String makeInFull;
    private String manufactureYear;
    private String primaryColour;
    private String secondaryColour;
    private String motTestExpiryDate;
    private String motTestNumber;
    private String vehicleType;

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getDvlaId() {

        return dvlaId;
    }

    public void setDvlaId(String dvlaId) {

        this.dvlaId = dvlaId;
    }

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

    public String getManufactureYear() {

        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {

        this.manufactureYear = manufactureYear;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
