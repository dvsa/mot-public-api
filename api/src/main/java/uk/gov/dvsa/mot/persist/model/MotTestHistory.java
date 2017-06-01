package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the mot_test_current database table.
 */
public class MotTestHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private MotTestStatus motTestStatus;
    private MotTestType motTestType;
    private Person person;
    private Site site;
    private Date startedDate;
    private Date completedDate;
    private Date issuedDate;
    private Date expiryDate;
    private BigDecimal number;
    private JasperDocument jasperDocument;
    private MotTestHistory motTestOriginal;
    private MotTestHistory motTestPrs;
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
    private List<MotTestHistoryRfrMap> motTestHistoryRfrMaps;
    private int createdBy;
    private Timestamp createdOn;
    private int lastUpdatedBy;
    private Timestamp lastUpdatedOn;
    private int version;

    public MotTestHistory() {

    }

    public long getId() {

        return this.id;
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

    /*
     * public IncognitoVehicle getIncognitoVehicle() { return
     * this.incognitoVehicle; }
     *
     * public void setIncognitoVehicle(IncognitoVehicle incognitoVehicle) {
     * this.incognitoVehicle = incognitoVehicle; }
     *
     * public JasperDocument getJasperDocument() { return this.jasperDocument; }
     *
     * public void setJasperDocument(JasperDocument jasperDocument) {
     * this.jasperDocument = jasperDocument; }
     *
     * public MotTestCurrent getMotTestCurrent1() { return this.motTestCurrent1; }
     *
     * public void setMotTestCurrent1(MotTestCurrent motTestCurrent1) {
     * this.motTestCurrent1 = motTestCurrent1; }
     */

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

    public List<MotTestHistoryRfrMap> getMotTestHistoryRfrMaps() {

        return this.motTestHistoryRfrMaps;
    }

    public void setMotTestHistoryRfrMaps(List<MotTestHistoryRfrMap> motTestHistoryRfrMaps) {

        this.motTestHistoryRfrMaps = motTestHistoryRfrMaps;
    }

    public MotTestHistoryRfrMap addMotTestHistoryRfrMap(MotTestHistoryRfrMap motTestHistoryRfrMap) {

        getMotTestHistoryRfrMaps().add(motTestHistoryRfrMap);
        motTestHistoryRfrMap.setMotTestHistory(this);

        return motTestHistoryRfrMap;
    }

    public MotTestHistoryRfrMap removeMotTestHistoryRfrMap(MotTestHistoryRfrMap motTestHistoryRfrMap) {

        getMotTestHistoryRfrMaps().remove(motTestHistoryRfrMap);
        motTestHistoryRfrMap.setMotTestHistory(null);

        return motTestHistoryRfrMap;
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

    public MotTestHistory getMotTestOriginal() {

        return motTestOriginal;
    }

    public void setMotTestOriginal(MotTestHistory motTestOriginal) {

        this.motTestOriginal = motTestOriginal;
    }

    public MotTestHistory getMotTestPrs() {

        return motTestPrs;
    }

    public void setMotTestPrs(MotTestHistory motTestPrs) {

        this.motTestPrs = motTestPrs;
    }

    // public MotTestVehicle getMotTestVehicle()
    // {
    // return this.motTestVehicle;
    // }

    // public void setMotTestVehicle( MotTestVehicle motTestVehicle )
    // {
    // this.motTestVehicle = motTestVehicle;
    // }
}