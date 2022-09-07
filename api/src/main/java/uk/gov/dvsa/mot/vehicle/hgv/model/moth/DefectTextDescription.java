package uk.gov.dvsa.mot.vehicle.hgv.model.moth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefectTextDescription {
    @JsonProperty("language")
    private String language;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public String getLanguage() {
        return language;
    }

    public DefectTextDescription setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DefectTextDescription setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public DefectTextDescription setName(String name) {
        this.name = name;
        return this;
    }
}
