package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the vehicle database table.
 */
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int dvlaVehicleId;
    private String registration;
    private String registrationCollapsed;
    private String vin;
    private String vinCollapsed;
    private EmptyReasonMap emptyReasonMap;
    private ModelDetail modelDetail;
    private Date year;
    private Date manufactureDate;
    private Date firstRegistrationDate;
    private Date firstUsedDate;
    private ColourLookup primaryColour;
    private ColourLookup secondaryColour;
    private int weight;
    private WeightSourceLookup weightSourceLookup;
    private CountryOfRegistrationLookup countryOfRegistrationLookup;
    private String engineNumber;
    private String chassisNumber;
    private boolean isNewAtFirstReg;
    private boolean isDamaged;
    private boolean isDestroyed;
    private boolean isIncognito;
    private int createdBy;
    private Timestamp createdOn;
    private int lastUpdatedBy;
    private Timestamp lastUpdatedOn;
    private int version;

    public Vehicle() {

    }

    public int getId() {

        return this.id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getChassisNumber() {

        return this.chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {

        this.chassisNumber = chassisNumber;
    }

    public int getCreatedBy() {

        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {

        this.createdBy = createdBy;
    }

    public Timestamp getCreatedOn() {

        return this.createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {

        this.createdOn = createdOn;
    }

    public int getDvlaVehicleId() {

        return this.dvlaVehicleId;
    }

    public void setDvlaVehicleId(int dvlaVehicleId) {

        this.dvlaVehicleId = dvlaVehicleId;
    }

    public String getEngineNumber() {

        return this.engineNumber;
    }

    public void setEngineNumber(String engineNumber) {

        this.engineNumber = engineNumber;
    }

    public Date getFirstRegistrationDate() {

        return this.firstRegistrationDate;
    }

    public void setFirstRegistrationDate(Date firstRegistrationDate) {

        this.firstRegistrationDate = firstRegistrationDate;
    }

    public Date getFirstUsedDate() {

        return this.firstUsedDate;
    }

    public void setFirstUsedDate(Date firstUsedDate) {

        this.firstUsedDate = firstUsedDate;
    }

    public boolean getIsDamaged() {

        return this.isDamaged;
    }

    public void setIsDamaged(boolean isDamaged) {

        this.isDamaged = isDamaged;
    }

    public boolean getIsDestroyed() {

        return this.isDestroyed;
    }

    public void setIsDestroyed(boolean isDestroyed) {

        this.isDestroyed = isDestroyed;
    }

    public boolean getIsIncognito() {

        return this.isIncognito;
    }

    public void setIsIncognito(boolean isIncognito) {

        this.isIncognito = isIncognito;
    }

    public boolean getIsNewAtFirstReg() {

        return this.isNewAtFirstReg;
    }

    public void setIsNewAtFirstReg(boolean isNewAtFirstReg) {

        this.isNewAtFirstReg = isNewAtFirstReg;
    }

    public int getLastUpdatedBy() {

        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(int lastUpdatedBy) {

        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Timestamp getLastUpdatedOn() {

        return this.lastUpdatedOn;
    }

    public void setLastUpdatedOn(Timestamp lastUpdatedOn) {

        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Date getManufactureDate() {

        return this.manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {

        this.manufactureDate = manufactureDate;
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

    public int getWeight() {

        return this.weight;
    }

    public void setWeight(int weight) {

        this.weight = weight;
    }

    public Date getYear() {

        return this.year;
    }

    public void setYear(Date year) {

        this.year = year;
    }

    public ColourLookup getPrimaryColour() {

        return this.primaryColour;
    }

    public void setPrimaryColour(ColourLookup colourLookup1) {

        this.primaryColour = colourLookup1;
    }

    public CountryOfRegistrationLookup getCountryOfRegistrationLookup() {

        return this.countryOfRegistrationLookup;
    }

    public void setCountryOfRegistrationLookup(CountryOfRegistrationLookup countryOfRegistrationLookup) {

        this.countryOfRegistrationLookup = countryOfRegistrationLookup;
    }

    public ModelDetail getModelDetail() {

        return this.modelDetail;
    }

    public void setModelDetail(ModelDetail modelDetail) {

        this.modelDetail = modelDetail;
    }

    public ColourLookup getSecondaryColour() {

        return this.secondaryColour;
    }

    public void setSecondaryColour(ColourLookup colourLookup2) {

        this.secondaryColour = colourLookup2;
    }

    public WeightSourceLookup getWeightSourceLookup() {

        return this.weightSourceLookup;
    }

    public void setWeightSourceLookup(WeightSourceLookup weightSourceLookup) {

        this.weightSourceLookup = weightSourceLookup;
    }

    public EmptyReasonMap getEmptyReasonMap() {

        return emptyReasonMap;
    }

    public void setEmptyReasonMap(EmptyReasonMap emtpyReasonMap) {

        this.emptyReasonMap = emtpyReasonMap;
    }
}