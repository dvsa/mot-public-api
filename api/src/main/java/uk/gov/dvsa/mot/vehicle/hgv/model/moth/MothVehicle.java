package uk.gov.dvsa.mot.vehicle.hgv.model.moth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import uk.gov.dvsa.mot.vehicle.hgv.model.moth.serialization.ZonedDateTimeDeserialiser;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MothVehicle implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("registration")
    private String registration;

    @JsonProperty("vin")
    private String vin;

    @JsonProperty("makeName")
    private String makeName;

    @JsonProperty("smmtMakeName")
    private String smmtMakeName;

    @JsonProperty("modelName")
    private String modelName;

    @JsonDeserialize(using = ZonedDateTimeDeserialiser.class)
    @JsonProperty("manufacturedDate")
    private ZonedDateTime manufacturedDate;

    @JsonDeserialize(using = ZonedDateTimeDeserialiser.class)
    @JsonProperty("firstUsedDate")
    private ZonedDateTime firstUsedDate;

    @JsonProperty("fuelType")
    private String fuelType;

    @JsonProperty("primaryColour")
    private String primaryColour;

    @JsonProperty("secondaryColour")
    private String secondaryColour;

    @JsonProperty("vehicleClassCode")
    private String vehicleClassCode;

    @JsonProperty("vehicleClassGroupCode")
    private String vehicleClassGroupCode;

    @JsonProperty("euClassification")
    private String euClassification;

    @JsonProperty("bodyTypeCode")
    private String bodyTypeCode;

    @JsonDeserialize(using = ZonedDateTimeDeserialiser.class)
    @JsonProperty("firstMotDueDate")
    private ZonedDateTime firstMotDueDate;

    @JsonProperty("isV5cInvalid")
    private boolean isV5cInvalid;

    @JsonProperty("latestVehicleDataOrigin")
    private String latestVehicleDataOrigin;

    @JsonProperty("vehicleDataOrigin")
    private List<String> vehicleDataOrigin;

    @JsonProperty("vehicleType")
    private String vehicleType;

    @JsonProperty("motTestHistory")
    private List<MothTestHistory> motTestHistory;

    public String getId() {
        return id;
    }

    public MothVehicle setId(String id) {
        this.id = id;
        return this;
    }

    public String getRegistration() {
        return registration;
    }

    public MothVehicle setRegistration(String registration) {
        this.registration = registration;
        return this;
    }

    public String getVin() {
        return vin;
    }

    public MothVehicle setVin(String vin) {
        this.vin = vin;
        return this;
    }

    public String getMakeName() {
        return makeName;
    }

    public MothVehicle setMakeName(String makeName) {
        this.makeName = makeName;
        return this;
    }

    public String getSmmtMakeName() {
        return smmtMakeName;
    }

    public MothVehicle setSmmtMakeName(String smmtMakeName) {
        this.smmtMakeName = smmtMakeName;
        return this;
    }

    public String getModelName() {
        return modelName;
    }

    public MothVehicle setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public ZonedDateTime getManufacturedDate() {
        return manufacturedDate;
    }

    public MothVehicle setManufacturedDate(ZonedDateTime manufacturedDate) {
        this.manufacturedDate = manufacturedDate;
        return this;
    }

    public ZonedDateTime getFirstUsedDate() {
        return firstUsedDate;
    }

    public MothVehicle setFirstUsedDate(ZonedDateTime firstUsedDate) {
        this.firstUsedDate = firstUsedDate;
        return this;
    }

    public String getFuelType() {
        return fuelType;
    }

    public MothVehicle setFuelType(String fuelType) {
        this.fuelType = fuelType;
        return this;
    }

    public String getPrimaryColour() {
        return primaryColour;
    }

    public MothVehicle setPrimaryColour(String primaryColour) {
        this.primaryColour = primaryColour;
        return this;
    }

    public String getSecondaryColour() {
        return secondaryColour;
    }

    public MothVehicle setSecondaryColour(String secondaryColour) {
        this.secondaryColour = secondaryColour;
        return this;
    }

    public String getVehicleClassCode() {
        return vehicleClassCode;
    }

    public MothVehicle setVehicleClassCode(String vehicleClassCode) {
        this.vehicleClassCode = vehicleClassCode;
        return this;
    }

    public String getVehicleClassGroupCode() {
        return vehicleClassGroupCode;
    }

    public MothVehicle setVehicleClassGroupCode(String vehicleClassGroupCode) {
        this.vehicleClassGroupCode = vehicleClassGroupCode;
        return this;
    }

    public String getEuClassification() {
        return euClassification;
    }

    public MothVehicle setEuClassification(String euClassification) {
        this.euClassification = euClassification;
        return this;
    }

    public String getBodyTypeCode() {
        return bodyTypeCode;
    }

    public MothVehicle setBodyTypeCode(String bodyTypeCode) {
        this.bodyTypeCode = bodyTypeCode;
        return this;
    }

    public ZonedDateTime getFirstMotDueDate() {
        return firstMotDueDate;
    }

    public MothVehicle setFirstMotDueDate(ZonedDateTime firstMotDueDate) {
        this.firstMotDueDate = firstMotDueDate;
        return this;
    }

    public boolean getIsV5cInvalid() {
        return isV5cInvalid;
    }

    public MothVehicle setIsV5cInvalid(boolean v5cInvalid) {
        isV5cInvalid = v5cInvalid;
        return this;
    }

    public String getLatestVehicleDataOrigin() {
        return latestVehicleDataOrigin;
    }

    public MothVehicle setLatestVehicleDataOrigin(String latestVehicleDataOrigin) {
        this.latestVehicleDataOrigin = latestVehicleDataOrigin;
        return this;
    }

    public List<String> getVehicleDataOrigin() {
        return vehicleDataOrigin;
    }

    public MothVehicle setVehicleDataOrigin(List<String> vehicleDataOrigin) {
        this.vehicleDataOrigin = vehicleDataOrigin;
        return this;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public MothVehicle setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public List<MothTestHistory> getMotTestHistory() {
        return motTestHistory;
    }

    public MothVehicle setMotTestHistory(List<MothTestHistory> motTestHistory) {
        this.motTestHistory = motTestHistory;
        return this;
    }

    @Override
    public String toString() {
        return "MothVehicle{" +
                "id='" + id + '\'' +
                ", registration='" + registration + '\'' +
                ", vin='" + vin + '\'' +
                ", makeName='" + makeName + '\'' +
                ", smmtMakeName='" + smmtMakeName + '\'' +
                ", modelName='" + modelName + '\'' +
                ", manufacturedDate='" + manufacturedDate + '\'' +
                ", firstUsedDate='" + firstUsedDate + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", primaryColour='" + primaryColour + '\'' +
                ", secondaryColour='" + secondaryColour + '\'' +
                ", vehicleClassCode='" + vehicleClassCode + '\'' +
                ", vehicleClassGroupCode='" + vehicleClassGroupCode + '\'' +
                ", euClassification='" + euClassification + '\'' +
                ", bodyTypeCode='" + bodyTypeCode + '\'' +
                ", firstMotDueDate='" + firstMotDueDate + '\'' +
                ", isV5cInvalid=" + isV5cInvalid +
                ", latestVehicleDataOrigin='" + latestVehicleDataOrigin + '\'' +
                ", vehicleDataOrigin=" + vehicleDataOrigin +
                ", vehicleType='" + vehicleType + '\'' +
                ", motTestHistory=" + motTestHistory +
                '}';
    }
}
