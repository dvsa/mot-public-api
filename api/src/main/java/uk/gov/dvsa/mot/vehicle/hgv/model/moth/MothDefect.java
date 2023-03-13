package uk.gov.dvsa.mot.vehicle.hgv.model.moth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MothDefect {

    @JsonProperty("id")
    private String id;

    @JsonProperty("isDangerous")
    private boolean isDangerous;

    @JsonProperty("type")
    private String type;

    @JsonProperty("deficiencyCategoryCode")
    private String deficiencyCategoryCode;

    @JsonProperty("text")
    private DefectText text;

    public String getId() {
        return id;
    }

    public MothDefect setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isDangerous() {
        return isDangerous;
    }

    public MothDefect setDangerous(boolean dangerous) {
        isDangerous = dangerous;
        return this;
    }

    public String getType() {
        return type;
    }

    public MothDefect setType(String type) {
        this.type = type;
        return this;
    }

    public String getDeficiencyCategoryCode() {
        return deficiencyCategoryCode;
    }

    public MothDefect setDeficiencyCategoryCode(String deficiencyCategoryCode) {
        this.deficiencyCategoryCode = deficiencyCategoryCode;
        return this;
    }

    public DefectText getText() {
        return text;
    }

    public MothDefect setText(DefectText text) {
        this.text = text;
        return this;
    }
}
