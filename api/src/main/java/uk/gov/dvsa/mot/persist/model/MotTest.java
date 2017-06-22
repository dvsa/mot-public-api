package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the mot_test_current database table.
 */
public class MotTest implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private MotTestStatus motTestStatus;
    private MotTestType motTestType;
    private Person person;
    private Site site;
    private Date startedDate;
    private Date completedDate;
    private Date submittedDate;
    private Date issuedDate;
    private Date expiryDate;
    private BigDecimal number;
    private JasperDocument jasperDocument;
    private Organisation organisation;
    private MotTest motTestOriginal;
    private MotTest motTestPrs;
    private int vehicleId;
    private int vehicleVersion;
    private int vehicleWeight;
    private WeightSourceLookup weightSourceLookup;
    private String odometerReadingType;
    private String odometerReadingUnit;
    private int odometerReadingValue;
    private boolean hasRegistration;
    private String clientIp;
    private MotTestAddressComment motTestAddressComment;
    private MotTestCancelled motTestCancelled;
    private MotTestComplaintRef motTestComplaintRef;
    private MotTestEmergencyReason motTestEmergencyReason;
    private List<MotTestRfrMap> motTestCurrentRfrMaps;
    private int createdBy;
    private Timestamp createdOn;
    private int lastUpdatedBy;
    private Timestamp lastUpdatedOn;
    private int version;

    public MotTest() {

    }

    public long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getOdometerReadingType() {

        return odometerReadingType;
    }

    public void setOdometerReadingType(String odometerReadingType) {

        this.odometerReadingType = odometerReadingType;
    }

    public String getOdometerReadingUnit() {

        return odometerReadingUnit;
    }

    public void setOdometerReadingUnit(String odometerReadingUnit) {

        this.odometerReadingUnit = odometerReadingUnit;
    }

    public int getOdometerReadingValue() {

        return odometerReadingValue;
    }

    public void setOdometerReadingValue(int odometerReadingValue) {

        this.odometerReadingValue = odometerReadingValue;
    }

    public String getClientIp() {

        return this.clientIp;
    }

    public void setClientIp(String clientIp) {

        this.clientIp = clientIp;
    }

    public Date getCompletedDate() {

        return this.completedDate;
    }

    public void setCompletedDate(Date completedDate) {

        this.completedDate = completedDate;
    }

    public Date getSubmittedDate() {

        return this.submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {

        this.submittedDate = submittedDate;
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

    public Date getExpiryDate() {

        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {

        this.expiryDate = expiryDate;
    }

    public boolean getHasRegistration() {

        return this.hasRegistration;
    }

    public void setHasRegistration(boolean hasRegistration) {

        this.hasRegistration = hasRegistration;
    }

    public Date getIssuedDate() {

        return this.issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {

        this.issuedDate = issuedDate;
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

    public BigDecimal getNumber() {

        return this.number;
    }

    public void setNumber(BigDecimal number) {

        this.number = number;
    }

    public Date getStartedDate() {

        return this.startedDate;
    }

    public void setStartedDate(Date startedDate) {

        this.startedDate = startedDate;
    }

    public int getVehicleVersion() {

        return this.vehicleVersion;
    }

    public void setVehicleVersion(int vehicleVersion) {

        this.vehicleVersion = vehicleVersion;
    }

    public int getVehicleWeight() {

        return this.vehicleWeight;
    }

    public void setVehicleWeight(int vehicleWeight) {

        this.vehicleWeight = vehicleWeight;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public MotTestAddressComment getMotTestAddressComment() {

        return this.motTestAddressComment;
    }

    public void setMotTestAddressComment(MotTestAddressComment motTestAddressComment) {

        this.motTestAddressComment = motTestAddressComment;
    }

    public MotTestCancelled getMotTestCancelled() {

        return this.motTestCancelled;
    }

    public void setMotTestCancelled(MotTestCancelled motTestCancelled) {

        this.motTestCancelled = motTestCancelled;
    }

    public MotTestComplaintRef getMotTestComplaintRef() {

        return this.motTestComplaintRef;
    }

    public void setMotTestComplaintRef(MotTestComplaintRef motTestComplaintRef) {

        this.motTestComplaintRef = motTestComplaintRef;
    }

    public MotTestStatus getMotTestStatus() {

        return this.motTestStatus;
    }

    public void setMotTestStatus(MotTestStatus motTestStatus) {

        this.motTestStatus = motTestStatus;
    }

    public MotTestType getMotTestType() {

        return this.motTestType;
    }

    public void setMotTestType(MotTestType motTestType) {

        this.motTestType = motTestType;
    }

    public Person getPerson() {

        return this.person;
    }

    public void setPerson(Person person) {

        this.person = person;
    }

    public Site getSite() {

        return this.site;
    }

    public void setSite(Site site) {

        this.site = site;
    }

    public int getVehicleId() {

        return this.vehicleId;
    }

    public void setVehicleId(int vehicleId) {

        this.vehicleId = vehicleId;
    }

    public WeightSourceLookup getWeightSourceLookup() {

        return this.weightSourceLookup;
    }

    public void setWeightSourceLookup(WeightSourceLookup weightSourceLookup) {

        this.weightSourceLookup = weightSourceLookup;
    }

    public List<MotTestRfrMap> getMotTestCurrentRfrMaps() {

        return this.motTestCurrentRfrMaps;
    }

    public void setMotTestCurrentRfrMaps(List<MotTestRfrMap> motTestCurrentRfrMaps) {

        this.motTestCurrentRfrMaps = motTestCurrentRfrMaps;
    }

    public MotTestRfrMap addMotTestCurrentRfrMap(MotTestRfrMap motTestCurrentRfrMap) {

        getMotTestCurrentRfrMaps().add(motTestCurrentRfrMap);
        motTestCurrentRfrMap.setMotTestCurrent(this);

        return motTestCurrentRfrMap;
    }

    public MotTestRfrMap removeMotTestCurrentRfrMap(MotTestRfrMap motTestCurrentRfrMap) {

        getMotTestCurrentRfrMaps().remove(motTestCurrentRfrMap);
        motTestCurrentRfrMap.setMotTestCurrent(null);

        return motTestCurrentRfrMap;
    }

    public MotTestEmergencyReason getMotTestEmergencyReason() {

        return this.motTestEmergencyReason;
    }

    public void setMotTestEmergencyReason(MotTestEmergencyReason motTestEmergencyReason) {

        this.motTestEmergencyReason = motTestEmergencyReason;
    }

    public JasperDocument getDocument() {

        return jasperDocument;
    }

    public void setDocument(JasperDocument document) {

        this.jasperDocument = document;
    }

    public Organisation getOrganisation() {

        return organisation;
    }

    public void setOrganisation(Organisation organisation) {

        this.organisation = organisation;
    }

    public MotTest getMotTestOriginal() {

        return motTestOriginal;
    }

    public void setMotTestOriginal(MotTest motTestOriginal) {

        this.motTestOriginal = motTestOriginal;
    }

    public MotTest getMotTestPrs() {

        return motTestPrs;
    }

    public void setMotTestPrs(MotTest motTestPrs) {

        this.motTestPrs = motTestPrs;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((clientIp == null) ? 0 : clientIp.hashCode());
        result = prime * result + ((completedDate == null) ? 0 : completedDate.hashCode());
        result = prime * result + createdBy;
        result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
        result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
        result = prime * result + (hasRegistration ? 1231 : 1237);
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((issuedDate == null) ? 0 : issuedDate.hashCode());
        result = prime * result + ((jasperDocument == null) ? 0 : jasperDocument.hashCode());
        result = prime * result + lastUpdatedBy;
        result = prime * result + ((lastUpdatedOn == null) ? 0 : lastUpdatedOn.hashCode());
        result = prime * result + ((motTestAddressComment == null) ? 0 : motTestAddressComment.hashCode());
        result = prime * result + ((motTestCancelled == null) ? 0 : motTestCancelled.hashCode());
        result = prime * result + ((motTestComplaintRef == null) ? 0 : motTestComplaintRef.hashCode());
        result = prime * result + ((motTestCurrentRfrMaps == null) ? 0 : motTestCurrentRfrMaps.hashCode());
        result = prime * result + ((motTestEmergencyReason == null) ? 0 : motTestEmergencyReason.hashCode());
        result = prime * result + ((motTestOriginal == null) ? 0 : motTestOriginal.hashCode());
        result = prime * result + ((motTestPrs == null) ? 0 : motTestPrs.hashCode());
        result = prime * result + ((motTestStatus == null) ? 0 : motTestStatus.hashCode());
        result = prime * result + ((motTestType == null) ? 0 : motTestType.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((odometerReadingType == null) ? 0 : odometerReadingType.hashCode());
        result = prime * result + ((odometerReadingUnit == null) ? 0 : odometerReadingUnit.hashCode());
        result = prime * result + odometerReadingValue;
        result = prime * result + ((organisation == null) ? 0 : organisation.hashCode());
        result = prime * result + ((person == null) ? 0 : person.hashCode());
        result = prime * result + ((site == null) ? 0 : site.hashCode());
        result = prime * result + ((startedDate == null) ? 0 : startedDate.hashCode());
        result = prime * result + ((submittedDate == null) ? 0 : submittedDate.hashCode());
        result = prime * result + vehicleId;
        result = prime * result + vehicleVersion;
        result = prime * result + vehicleWeight;
        result = prime * result + version;
        result = prime * result + ((weightSourceLookup == null) ? 0 : weightSourceLookup.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MotTest other = (MotTest) obj;
        if (clientIp == null) {
            if (other.clientIp != null) {
                return false;
            }
        } else if (!clientIp.equals(other.clientIp)) {
            return false;
        }
        if (completedDate == null) {
            if (other.completedDate != null) {
                return false;
            }
        } else if (!completedDate.equals(other.completedDate)) {
            return false;
        }
        if (createdBy != other.createdBy) {
            return false;
        }
        if (createdOn == null) {
            if (other.createdOn != null) {
                return false;
            }
        } else if (!createdOn.equals(other.createdOn)) {
            return false;
        }
        if (expiryDate == null) {
            if (other.expiryDate != null) {
                return false;
            }
        } else if (!expiryDate.equals(other.expiryDate)) {
            return false;
        }
        if (hasRegistration != other.hasRegistration) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (issuedDate == null) {
            if (other.issuedDate != null) {
                return false;
            }
        } else if (!issuedDate.equals(other.issuedDate)) {
            return false;
        }
        if (jasperDocument == null) {
            if (other.jasperDocument != null) {
                return false;
            }
        } else if (!jasperDocument.equals(other.jasperDocument)) {
            return false;
        }
        if (lastUpdatedBy != other.lastUpdatedBy) {
            return false;
        }
        if (lastUpdatedOn == null) {
            if (other.lastUpdatedOn != null) {
                return false;
            }
        } else if (!lastUpdatedOn.equals(other.lastUpdatedOn)) {
            return false;
        }
        if (motTestAddressComment == null) {
            if (other.motTestAddressComment != null) {
                return false;
            }
        } else if (!motTestAddressComment.equals(other.motTestAddressComment)) {
            return false;
        }
        if (motTestCancelled == null) {
            if (other.motTestCancelled != null) {
                return false;
            }
        } else if (!motTestCancelled.equals(other.motTestCancelled)) {
            return false;
        }
        if (motTestComplaintRef == null) {
            if (other.motTestComplaintRef != null) {
                return false;
            }
        } else if (!motTestComplaintRef.equals(other.motTestComplaintRef)) {
            return false;
        }
        if (motTestCurrentRfrMaps == null) {
            if (other.motTestCurrentRfrMaps != null) {
                return false;
            }
        } else if (!motTestCurrentRfrMaps.equals(other.motTestCurrentRfrMaps)) {
            return false;
        }
        if (motTestEmergencyReason == null) {
            if (other.motTestEmergencyReason != null) {
                return false;
            }
        } else if (!motTestEmergencyReason.equals(other.motTestEmergencyReason)) {
            return false;
        }
        if (motTestOriginal == null) {
            if (other.motTestOriginal != null) {
                return false;
            }
        } else if (!motTestOriginal.equals(other.motTestOriginal)) {
            return false;
        }
        if (motTestPrs == null) {
            if (other.motTestPrs != null) {
                return false;
            }
        } else if (!motTestPrs.equals(other.motTestPrs)) {
            return false;
        }
        if (motTestStatus == null) {
            if (other.motTestStatus != null) {
                return false;
            }
        } else if (!motTestStatus.equals(other.motTestStatus)) {
            return false;
        }
        if (motTestType == null) {
            if (other.motTestType != null) {
                return false;
            }
        } else if (!motTestType.equals(other.motTestType)) {
            return false;
        }
        if (number == null) {
            if (other.number != null) {
                return false;
            }
        } else if (!number.equals(other.number)) {
            return false;
        }
        if (odometerReadingType == null) {
            if (other.odometerReadingType != null) {
                return false;
            }
        } else if (!odometerReadingType.equals(other.odometerReadingType)) {
            return false;
        }
        if (odometerReadingUnit == null) {
            if (other.odometerReadingUnit != null) {
                return false;
            }
        } else if (!odometerReadingUnit.equals(other.odometerReadingUnit)) {
            return false;
        }
        if (odometerReadingValue != other.odometerReadingValue) {
            return false;
        }
        if (organisation == null) {
            if (other.organisation != null) {
                return false;
            }
        } else if (!organisation.equals(other.organisation)) {
            return false;
        }
        if (person == null) {
            if (other.person != null) {
                return false;
            }
        } else if (!person.equals(other.person)) {
            return false;
        }
        if (site == null) {
            if (other.site != null) {
                return false;
            }
        } else if (!site.equals(other.site)) {
            return false;
        }
        if (startedDate == null) {
            if (other.startedDate != null) {
                return false;
            }
        } else if (!startedDate.equals(other.startedDate)) {
            return false;
        }
        if (submittedDate == null) {
            if (other.submittedDate != null) {
                return false;
            }
        } else if (!submittedDate.equals(other.submittedDate)) {
            return false;
        }
        if (vehicleId != other.vehicleId) {
            return false;
        }
        if (vehicleVersion != other.vehicleVersion) {
            return false;
        }
        if (vehicleWeight != other.vehicleWeight) {
            return false;
        }
        if (version != other.version) {
            return false;
        }
        if (weightSourceLookup == null) {
            if (other.weightSourceLookup != null) {
                return false;
            }
        } else if (!weightSourceLookup.equals(other.weightSourceLookup)) {
            return false;
        }
        return true;
    }
}