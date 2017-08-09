package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the dvla_vehicle database table.
 */
public class DvlaVehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private BodyType bodyType;
    private String bodyTypeCode;
    private ColourLookup colour1;
    private ColourLookup colour2;
    private String ctRelatedMark;
    private int designedGrossWeight;
    private int dvlaVehicleId;
    private int engineCapacity;
    private String engineNumber;
    private String euClassification;
    private Date firstRegistrationDate;
    private boolean isSeriouslyDamaged;
    private boolean isVehicleNewAtFirstRegistration;
    private String makeCode;
    private String makeInFull;
    private Date manufactureDate;
    private int massInServiceWeight;
    private String modelCode;
    private DvlaModel modelDetail;
    private DvlaMake makeDetail;
    private FuelType propulsion;
    private String recentV5DocumentNumber;
    private String registration;
    private String registrationCollapsed;
    private String registrationValidationCharacter;
    private int seatingCapacity;
    private String svaEmissionStandard;
    private int unladenWeight;
    private String vin;
    private String vinCollapsed;
    private WheelplanType wheelplan;
    private Vehicle vehicle;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;

    public DvlaVehicle() {

    }

    public int getId() {

        return this.id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public BodyType getBodyType() {

        return this.bodyType;
    }

    public void setBodyType(BodyType bodyType) {

        this.bodyType = bodyType;
    }

    public ColourLookup getColour1() {

        return this.colour1;
    }

    public void setColour1(ColourLookup colour1) {

        this.colour1 = colour1;
    }

    public ColourLookup getColour2() {

        return this.colour2;
    }

    public void setColour2(ColourLookup colour2) {

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

    public String getCtRelatedMark() {

        return this.ctRelatedMark;
    }

    public void setCtRelatedMark(String ctRelatedMark) {

        this.ctRelatedMark = ctRelatedMark;
    }

    public int getDesignedGrossWeight() {

        return this.designedGrossWeight;
    }

    public void setDesignedGrossWeight(int designedGrossWeight) {

        this.designedGrossWeight = designedGrossWeight;
    }

    public int getDvlaVehicleId() {

        return this.dvlaVehicleId;
    }

    public void setDvlaVehicleId(int dvlaVehicleId) {

        this.dvlaVehicleId = dvlaVehicleId;
    }

    public int getEngineCapacity() {

        return this.engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {

        this.engineCapacity = engineCapacity;
    }

    public String getEngineNumber() {

        return this.engineNumber;
    }

    public void setEngineNumber(String engineNumber) {

        this.engineNumber = engineNumber;
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

    public boolean getIsSeriouslyDamaged() {

        return this.isSeriouslyDamaged;
    }

    public void setIsSeriouslyDamaged(boolean isSeriouslyDamaged) {

        this.isSeriouslyDamaged = isSeriouslyDamaged;
    }

    public boolean getIsVehicleNewAtFirstRegistration() {

        return this.isVehicleNewAtFirstRegistration;
    }

    public void setIsVehicleNewAtFirstRegistration(boolean isVehicleNewAtFirstRegistration) {

        this.isVehicleNewAtFirstRegistration = isVehicleNewAtFirstRegistration;
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

    public String getMakeCode() {

        return this.makeCode;
    }

    public void setMakeCode(String makeCode) {

        this.makeCode = makeCode;
    }

    public String getMakeInFull() {

        return this.makeInFull;
    }

    public void setMakeInFull(String makeInFull) {

        this.makeInFull = makeInFull;
    }

    public Date getManufactureDate() {

        return this.manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {

        this.manufactureDate = manufactureDate;
    }

    public int getMassInServiceWeight() {

        return this.massInServiceWeight;
    }

    public void setMassInServiceWeight(int massInServiceWeight) {

        this.massInServiceWeight = massInServiceWeight;
    }

    public String getModelCode() {

        return this.modelCode;
    }

    public void setModelCode(String modelCode) {

        this.modelCode = modelCode;
    }

    public void setModelDetail(DvlaModel modelDetail) {

        this.modelDetail = modelDetail;
    }

    public DvlaModel getModelDetail() {

        return modelDetail;
    }

    public void setMakeDetail(DvlaMake makeDetail) {

        this.makeDetail = makeDetail;
    }

    public DvlaMake getMakeDetail() {

        return makeDetail;
    }

    public FuelType getPropulsion() {

        return this.propulsion;
    }

    public void setPropulsion(FuelType propulsion) {

        this.propulsion = propulsion;
    }

    public String getRecentV5DocumentNumber() {

        return this.recentV5DocumentNumber;
    }

    public void setRecentV5DocumentNumber(String recentV5DocumentNumber) {

        this.recentV5DocumentNumber = recentV5DocumentNumber;
    }

    public String getRegistration() {

        return this.registration;
    }

    public void setRegistration(String registration) {

        this.registration = registration;
    }

    public String getRegistrationCollapsed() {

        return this.registrationCollapsed;
    }

    public void setRegistrationCollapsed(String registrationCollapsed) {

        this.registrationCollapsed = registrationCollapsed;
    }

    public String getRegistrationValidationCharacter() {

        return this.registrationValidationCharacter;
    }

    public void setRegistrationValidationCharacter(String registrationValidationCharacter) {

        this.registrationValidationCharacter = registrationValidationCharacter;
    }

    public int getSeatingCapacity() {

        return this.seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {

        this.seatingCapacity = seatingCapacity;
    }

    public String getSvaEmissionStandard() {

        return this.svaEmissionStandard;
    }

    public void setSvaEmissionStandard(String svaEmissionStandard) {

        this.svaEmissionStandard = svaEmissionStandard;
    }

    public int getUnladenWeight() {

        return this.unladenWeight;
    }

    public void setUnladenWeight(int unladenWeight) {

        this.unladenWeight = unladenWeight;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public String getVin() {

        return this.vin;
    }

    public void setVin(String vin) {

        this.vin = vin;
    }

    public String getVinCollapsed() {

        return this.vinCollapsed;
    }

    public void setVinCollapsed(String vinCollapsed) {

        this.vinCollapsed = vinCollapsed;
    }

    public WheelplanType getWheelplan() {

        return this.wheelplan;
    }

    public void setWheelplan(WheelplanType wheelplan) {

        this.wheelplan = wheelplan;
    }

    public Vehicle getVehicle() {

        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {

        this.vehicle = vehicle;
    }

    public String getBodyTypeCode() {
        return bodyTypeCode;
    }

    public void setBodyTypeCode(String bodyTypeCode) {
        this.bodyTypeCode = bodyTypeCode;
    }
}