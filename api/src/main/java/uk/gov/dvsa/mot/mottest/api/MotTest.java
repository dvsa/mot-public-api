package uk.gov.dvsa.mot.mottest.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an MOT Test
 */
public class MotTest {
    protected Long id;
    // @XmlJavaTypeAdapter(DateFormatAdapter.class) // is this needed?
    protected Date startedDate;
    // @XmlJavaTypeAdapter(DateFormatAdapter.class) // is this needed?
    protected Date completedDate;
    // @XmlJavaTypeAdapter(DateFormatAdapter.class) // is this needed?
    protected Date issuedDate;
    // @XmlJavaTypeAdapter(DateFormatAdapter.class) // is this needed?
    protected Date expiryDate;
    protected String clientIp;
    protected String createdBy;
    // @XmlJavaTypeAdapter(DateFormatAdapter.class) // is this needed?
    protected Date createdOn;
    protected String lastUpdatedBy;
    // @XmlJavaTypeAdapter(DateFormatAdapter.class) // is this needed?
    protected Date lastUpdatedOn;
    protected Integer version;
    private String motTestType;
    private String status;
    private int vehicleId;
    private Integer vehicleVersion;
    private int personId;
    private int siteId;
    private Long number;
    private Long motTestIdOriginal;
    private Long prsMotTestId;
    private Long documentId;
    private String addressComment;
    private String complaintRef;
    private Integer emergencyReasonLogId;
    private String emergencyReason;
    private String emergencyReasonComment;
    private String reasonForCancel;
    private String reasonForCancelComment;
    private String reasonForTerminationComment;
    private boolean hasRegistration;
    private Integer vehicleWeight;
    private String weightSourceLookup;
    private Integer odometerReadingValue;
    private String odometerReadingUnit;
    private String odometerReadingType;
    private List<MotTestRfrMap> motTestRfrMaps;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getCreatedBy() {

        return createdBy;
    }

    public void setCreatedBy(String createdBy) {

        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {

        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {

        this.createdOn = createdOn;
    }

    public String getLastUpdatedBy() {

        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {

        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedOn() {

        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {

        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Integer getVersion() {

        return version;
    }

    public void setVersion(Integer version) {

        this.version = version;
    }

    public String getMotTestType() {

        return motTestType;
    }

    public void setMotTestType(String motTestType) {

        this.motTestType = motTestType;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public int getPersonId() {

        return personId;
    }

    public void setPersonId(int personId) {

        this.personId = personId;
    }

    public int getSiteId() {

        return siteId;
    }

    public void setSiteId(int siteId) {

        this.siteId = siteId;
    }

    public Long getNumber() {

        return number;
    }

    public void setNumber(Long number) {

        this.number = number;
    }

    public int getVehicleId() {

        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {

        this.vehicleId = vehicleId;
    }

    public int getVehicleVersion() {

        return vehicleVersion;
    }

    public void setVehicleVersion(int vehicleVersion) {

        this.vehicleVersion = vehicleVersion;
    }

    public Date getStartedDate() {

        return startedDate;
    }

    public void setStartedDate(Date startedDate) {

        this.startedDate = startedDate;
    }

    public Date getCompletedDate() {

        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {

        this.completedDate = completedDate;
    }

    public Date getIssuedDate() {

        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {

        this.issuedDate = issuedDate;
    }

    public Date getExpiryDate() {

        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {

        this.expiryDate = expiryDate;
    }

    public Long getMotTestIdOriginal() {

        return motTestIdOriginal;
    }

    public void setMotTestIdOriginal(Long motTestIdOriginal) {

        this.motTestIdOriginal = motTestIdOriginal;
    }

    public Long getPrsMotTestId() {

        return prsMotTestId;
    }

    public void setPrsMotTestId(Long prsMotTestId) {

        this.prsMotTestId = prsMotTestId;
    }

    public Long getDocumentId() {

        return documentId;
    }

    public void setDocumentId(Long documentId) {

        this.documentId = documentId;
    }

    public String getAddressComment() {

        return addressComment;
    }

    public void setAddressComment(String addressComment) {

        this.addressComment = addressComment;
    }

    public String getComplaintRef() {

        return complaintRef;
    }

    public void setComplaintRef(String complaintRef) {

        this.complaintRef = complaintRef;
    }

    public boolean getHasRegistration() {

        return hasRegistration;
    }

    public void setHasRegistration(boolean hasRegistration) {

        this.hasRegistration = hasRegistration;
    }

    public Integer getVehicleWeight() {

        return vehicleWeight;
    }

    public void setVehicleWeight(Integer vehicleWeight) {

        this.vehicleWeight = vehicleWeight;
    }

    public String getWeightSourceLookup() {

        return weightSourceLookup;
    }

    public void setWeightSourceLookup(String weightSourceLookup) {

        this.weightSourceLookup = weightSourceLookup;
    }

    public String getReasonForTerminationComment() {

        return reasonForTerminationComment;
    }

    public void setReasonForTerminationComment(String reasonForTerminationComment) {

        this.reasonForTerminationComment = reasonForTerminationComment;
    }

    public List<MotTestRfrMap> getMotTestRfrMaps() {

        if (motTestRfrMaps == null) {
            motTestRfrMaps = new ArrayList<>();
        }

        return motTestRfrMaps;
    }

    public void setMotTestRfrMaps(List<MotTestRfrMap> motTestRfrMaps) {

        this.motTestRfrMaps = motTestRfrMaps;
    }

    public String getClientIp() {

        return clientIp;
    }

    public void setClientIp(String clientIp) {

        this.clientIp = clientIp;
    }

    public String getReasonForCancel() {

        return reasonForCancel;
    }

    public void setReasonForCancel(String reasonForCancel) {

        this.reasonForCancel = reasonForCancel;
    }

    public String getReasonForCancelComment() {

        return reasonForCancelComment;
    }

    public void setReasonForCancelComment(String reasonForCancelComment) {

        this.reasonForCancelComment = reasonForCancelComment;
    }

    public Integer getEmergencyReasonLogId() {

        return emergencyReasonLogId;
    }

    public void setEmergencyReasonLogId(Integer emergencyReasonLogId) {

        this.emergencyReasonLogId = emergencyReasonLogId;
    }

    public String getEmergencyReason() {

        return emergencyReason;
    }

    public void setEmergencyReason(String emergencyReason) {

        this.emergencyReason = emergencyReason;
    }

    public String getEmergencyReasonComment() {

        return emergencyReasonComment;
    }

    public void setEmergencyReasonComment(String emergencyReasonComment) {

        this.emergencyReasonComment = emergencyReasonComment;
    }

    public void setEmergencyComment(String emergencyReasonComment) {

        this.emergencyReasonComment = emergencyReasonComment;
    }

    public Integer getOdometerReadingValue() {

        return odometerReadingValue;
    }

    public void setOdometerReadingValue(Integer odometerReadingValue) {

        this.odometerReadingValue = odometerReadingValue;
    }

    public String getOdometerReadingUnit() {

        return odometerReadingUnit;
    }

    public void setOdometerReadingUnit(String odometerReadingUnit) {

        this.odometerReadingUnit = odometerReadingUnit;
    }

    public String getOdometerReadingType() {

        return odometerReadingType;
    }

    public void setOdometerReadingType(String odometerReadingType) {

        this.odometerReadingType = odometerReadingType;
    }

}