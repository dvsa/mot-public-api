package uk.gov.dvsa.mot.vehicle.hgv.model.moth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefectText {

    @JsonProperty("lateral")
    private String lateral;

    @JsonProperty("longitudinal")
    private String longitudinal;

    @JsonProperty("vertical")
    private String vertical;

    @JsonProperty("inspection_manual_reference")
    private String inspectionManualReference;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("category")
    private List<DefectTextCategory> category;



    @JsonProperty("description")
    private List<DefectTextDescription> description;

    public String getLateral() {
        return lateral;
    }

    public DefectText setLateral(String lateral) {
        this.lateral = lateral;
        return this;
    }

    public String getLongitudinal() {
        return longitudinal;
    }

    public DefectText setLongitudinal(String longitudinal) {
        this.longitudinal = longitudinal;
        return this;
    }

    public String getVertical() {
        return vertical;
    }

    public DefectText setVertical(String vertical) {
        this.vertical = vertical;
        return this;
    }

    public String getInspectionManualReference() {
        return inspectionManualReference;
    }

    public DefectText setInspectionManualReference(String inspectionManualReference) {
        this.inspectionManualReference = inspectionManualReference;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public DefectText setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public List<DefectTextCategory> getCategory() {
        return category;
    }

    public DefectText setCategory(List<DefectTextCategory> category) {
        this.category = category;
        return this;
    }

    public List<DefectTextDescription> getDescription() {
        return description;
    }

    public DefectText setDescription(List<DefectTextDescription> description) {
        this.description = description;
        return this;
    }
}
