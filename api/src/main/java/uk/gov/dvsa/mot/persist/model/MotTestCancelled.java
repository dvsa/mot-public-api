package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The persistent class for the mot_test_cancelled database table.
 */
public class MotTestCancelled implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private Comment comment;
    private MotTestReasonForCancelLookup motTestReasonForCancelLookup;
    private int createdBy;
    private Timestamp createdOn;
    private int lastUpdatedBy;
    private Timestamp lastUpdatedOn;
    private int version;
    private MotTest motTestCurrent;
    private MotTestHistory motTestHistory;

    public MotTestCancelled() {

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

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public Comment getComment() {

        return this.comment;
    }

    public void setComment(Comment comment) {

        this.comment = comment;
    }

    public MotTest getMotTestCurrent() {

        return this.motTestCurrent;
    }

    public void setMotTestCurrent(MotTest motTestCurrent) {

        this.motTestCurrent = motTestCurrent;
    }

    public MotTestHistory getMotTestHistory() {

        return this.motTestHistory;
    }

    public void setMotTestHistory(MotTestHistory motTestHistory) {

        this.motTestHistory = motTestHistory;
    }

    public MotTestReasonForCancelLookup getMotTestReasonForCancelLookup() {

        return this.motTestReasonForCancelLookup;
    }

    public void setMotTestReasonForCancelLookup(MotTestReasonForCancelLookup motTestReasonForCancelLookup) {

        this.motTestReasonForCancelLookup = motTestReasonForCancelLookup;
    }

}