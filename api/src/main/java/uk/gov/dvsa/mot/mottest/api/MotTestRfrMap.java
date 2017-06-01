package uk.gov.dvsa.mot.mottest.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents an RFR on a vehicle with a location and other details.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"type", "location", "testItemCategory", "reasonForRejection",
        "inspectionManualReference", "comment", "customDescription", "failureDangerous", "generated", "onOriginalTest"})
public class MotTestRfrMap {
    @XmlElement(name = "type", required = false)
    private String type;
    @XmlElement(name = "location", required = false)
    private MotTestRfrLocation location;
    @XmlElement(name = "test_item_category", required = false)
    private String testItemCategory;
    @XmlElement(name = "reason_for_rejection", required = false)
    private String reasonForRejection;
    @XmlElement(name = "inspection_manual_reference", required = false)
    private String inspectionManualReference;
    @XmlElement(name = "comment", required = false)
    private String comment;
    @XmlElement(name = "custom_description", required = false)
    private String customDescription;
    @XmlElement(name = "failure_dangerous", required = false)
    private boolean failureDangerous;
    @XmlElement(name = "generated", required = false)
    private boolean generated;
    @XmlElement(name = "on_original_test", required = false)
    private boolean onOriginalTest;

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public MotTestRfrLocation getLocation() {

        return location;
    }

    public void setLocation(MotTestRfrLocation location) {

        this.location = location;
    }

    public String getTestItemCategory() {

        return testItemCategory;
    }

    public void setTestItemCategory(String testItemCategory) {

        this.testItemCategory = testItemCategory;
    }

    public String getReasonForRejection() {

        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {

        this.reasonForRejection = reasonForRejection;
    }

    public String getInspectionManualReference() {

        return inspectionManualReference;
    }

    public void setInspectionManualReference(String inspectionManualReference) {

        this.inspectionManualReference = inspectionManualReference;
    }

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

    public String getCustomDescription() {

        return customDescription;
    }

    public void setCustomDescription(String customDescription) {

        this.customDescription = customDescription;
    }

    public boolean isFailureDangerous() {

        return failureDangerous;
    }

    public void setFailureDangerous(boolean failureDangerous) {

        this.failureDangerous = failureDangerous;
    }

    public boolean isGenerated() {

        return generated;
    }

    public void setGenerated(boolean generated) {

        this.generated = generated;
    }

    public boolean isOnOriginalTest() {

        return onOriginalTest;
    }

    public void setOnOriginalTest(boolean onOriginalTest) {

        this.onOriginalTest = onOriginalTest;
    }
}