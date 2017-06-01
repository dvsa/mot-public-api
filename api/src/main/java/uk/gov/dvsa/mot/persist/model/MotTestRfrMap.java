package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The persistent class for the mot_test_current_rfr_map database table.
 */
public class MotTestRfrMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private MotTest motTestCurrent;
    private ReasonForRejection reasonForRejection;
    private ReasonForRejectionType reasonForRejectionType;
    private MotTestRfrLocationType motTestRfrLocationType;
    private MotTestRfrMapComment motTestRfrMapComment;
    private MotTestRfrMapCustomDescription motTestRfrMapCustomDescription;
    private boolean failureDangerous;
    private boolean generated;
    private boolean onOriginalTest;
    private int createdBy;
    private Timestamp createdOn;
    private int lastUpdatedBy;
    private Timestamp lastUpdatedOn;
    private int version;

    public MotTestRfrMap() {

    }

    public long getId() {

        return this.id;
    }

    public void setId(long id) {

        this.id = id;
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

    public boolean getFailureDangerous() {

        return this.failureDangerous;
    }

    public void setFailureDangerous(boolean failureDangerous) {

        this.failureDangerous = failureDangerous;
    }

    public boolean getGenerated() {

        return this.generated;
    }

    public void setGenerated(boolean generated) {

        this.generated = generated;
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

    public boolean getOnOriginalTest() {

        return this.onOriginalTest;
    }

    public void setOnOriginalTest(boolean onOriginalTest) {

        this.onOriginalTest = onOriginalTest;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public MotTest getMotTestCurrent() {

        return this.motTestCurrent;
    }

    public void setMotTestCurrent(MotTest motTestCurrent) {

        this.motTestCurrent = motTestCurrent;
    }

    public ReasonForRejectionType getReasonForRejectionType() {

        return this.reasonForRejectionType;
    }

    public void setReasonForRejectionType(ReasonForRejectionType reasonForRejectionType) {

        this.reasonForRejectionType = reasonForRejectionType;
    }

    public ReasonForRejection getReasonForRejection() {

        return this.reasonForRejection;
    }

    public void setReasonForRejection(ReasonForRejection reasonForRejection) {

        this.reasonForRejection = reasonForRejection;
    }

    public MotTestRfrLocationType getMotTestRfrLocationType() {

        return this.motTestRfrLocationType;
    }

    public void setMotTestRfrLocationType(MotTestRfrLocationType motTestRfrLocationType) {

        this.motTestRfrLocationType = motTestRfrLocationType;
    }

    public MotTestRfrMapComment getMotTestRfrMapComment() {

        return this.motTestRfrMapComment;
    }

    public void setMotTestRfrMapComment(MotTestRfrMapComment motTestRfrMapComment) {

        this.motTestRfrMapComment = motTestRfrMapComment;
    }

    public MotTestRfrMapCustomDescription getMotTestRfrMapCustomDescription() {

        return this.motTestRfrMapCustomDescription;
    }

    public void setMotTestRfrMapCustomDescription(MotTestRfrMapCustomDescription motTestRfrMapCustomDescription) {

        this.motTestRfrMapCustomDescription = motTestRfrMapCustomDescription;
    }
}