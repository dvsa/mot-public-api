package uk.gov.dvsa.mot.vehicle.hgv.model.moth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import uk.gov.dvsa.mot.vehicle.hgv.model.moth.serialization.ZonedDateTimeDeserialiser;

import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MothTestHistory {

    @JsonProperty("id")
    private long id;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("motTestNumber")
    private String motTestNumber;

    @JsonProperty("type")
    private String type;

    @JsonDeserialize(using = ZonedDateTimeDeserialiser.class)
    @JsonProperty("startedDate")
    private ZonedDateTime startedDate;

    @JsonDeserialize(using = ZonedDateTimeDeserialiser.class)
    @JsonProperty("completedDate")
    private ZonedDateTime completedDate;

    @JsonDeserialize(using = ZonedDateTimeDeserialiser.class)
    @JsonProperty("expiryDate")
    private ZonedDateTime expiryDate;

    @JsonProperty("odometerValue")
    private long odometerValue;

    @JsonProperty("odometerUnit")
    private String odometerUnit;

    @JsonProperty("odometerResultType")
    private String odometerResultType;

    @JsonProperty("testResult")
    private String testResult;

    @JsonProperty("garageDetails")
    private GarageDetails garageDetails;

    @JsonProperty("defects")
    private List<MothDefect> defects;

    public long getId() {
        return id;
    }

    public MothTestHistory setId(long id) {
        this.id = id;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public MothTestHistory setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getMotTestNumber() {
        return motTestNumber;
    }

    public MothTestHistory setMotTestNumber(String motTestNumber) {
        this.motTestNumber = motTestNumber;
        return this;
    }

    public String getType() {
        return type;
    }

    public MothTestHistory setType(String type) {
        this.type = type;
        return this;
    }

    public ZonedDateTime getStartedDate() {
        return startedDate;
    }

    public MothTestHistory setStartedDate(ZonedDateTime startedDate) {
        this.startedDate = startedDate;
        return this;
    }

    public ZonedDateTime getCompletedDate() {
        return completedDate;
    }

    public MothTestHistory setCompletedDate(ZonedDateTime completedDate) {
        this.completedDate = completedDate;
        return this;
    }

    public ZonedDateTime getExpiryDate() {
        return expiryDate;
    }

    public MothTestHistory setExpiryDate(ZonedDateTime expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public long getOdometerValue() {
        return odometerValue;
    }

    public MothTestHistory setOdometerValue(long odometerValue) {
        this.odometerValue = odometerValue;
        return this;
    }

    public String getOdometerUnit() {
        return odometerUnit;
    }

    public MothTestHistory setOdometerUnit(String odometerUnit) {
        this.odometerUnit = odometerUnit;
        return this;
    }

    public String getOdometerResultType() {
        return odometerResultType;
    }

    public MothTestHistory setOdometerResultType(String odometerResultType) {
        this.odometerResultType = odometerResultType;
        return this;
    }

    public String getTestResult() {
        return testResult;
    }

    public MothTestHistory setTestResult(String testResult) {
        this.testResult = testResult;
        return this;
    }

    public GarageDetails getGarageDetails() {
        return garageDetails;
    }

    public MothTestHistory setGarageDetails(GarageDetails garageDetails) {
        this.garageDetails = garageDetails;
        return this;
    }

    public List<MothDefect> getDefects() {
        return defects;
    }

    public MothTestHistory setDefects(List<MothDefect> defects) {
        this.defects = defects;
        return this;
    }
}
