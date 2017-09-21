package uk.gov.dvsa.mot.trade.api;

import java.util.Date;

/**
 * The JSON API Model for a DvlaVehicle
 */
public class DvlaVehicle {

    private long id;
    private String bodyTypeCode;
    private String colour1;
    private String colour2;
    private long dvlaVehicleId;
    private String euClassification;
    private Date firstRegistrationDate;
    private Date manufactureDate;
    private String modelDetail;
    private String makeDetail;
    private String registration;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    private String fuelType;

    public long getId() {

        return this.id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public String getColour1() {

        return this.colour1;
    }

    public void setColour1(String colour1) {

        this.colour1 = colour1;
    }

    public String getColour2() {

        return this.colour2;
    }

    public void setColour2(String colour2) {

        this.colour2 = colour2;
    }

    public int getCreatedBy() {

        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {

        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {

        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {

        this.createdOn = createdOn;
    }

    public long getDvlaVehicleId() {

        return this.dvlaVehicleId;
    }

    public void setDvlaVehicleId(long dvlaVehicleId) {

        this.dvlaVehicleId = dvlaVehicleId;
    }

    public String getEuClassification() {

        return this.euClassification;
    }

    public void setEuClassification(String euClassification) {

        this.euClassification = euClassification;
    }

    public Date getFirstRegistrationDate() {

        return this.firstRegistrationDate;
    }

    public void setFirstRegistrationDate(Date firstRegistrationDate) {

        this.firstRegistrationDate = firstRegistrationDate;
    }

    public int getLastUpdatedBy() {

        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(int lastUpdatedBy) {

        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedOn() {

        return this.lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {

        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Date getManufactureDate() {

        return this.manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {

        this.manufactureDate = manufactureDate;
    }

    public void setModelDetail(String modelDetail) {

        this.modelDetail = modelDetail;
    }

    public String getModelDetail() {

        return modelDetail;
    }

    public void setMakeDetail(String makeDetail) {

        this.makeDetail = makeDetail;
    }

    public String getMakeDetail() {

        return makeDetail;
    }

    public String getRegistration() {

        return this.registration;
    }

    public void setRegistration(String registration) {

        this.registration = registration;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public String getBodyTypeCode() {
        return bodyTypeCode;
    }

    public void setBodyTypeCode(String bodyTypeCode) {
        this.bodyTypeCode = bodyTypeCode;
    }

    public String getFuelType() {

        return fuelType;
    }

    public void setFuelType(String fuelType) {

        this.fuelType = fuelType;
    }
}