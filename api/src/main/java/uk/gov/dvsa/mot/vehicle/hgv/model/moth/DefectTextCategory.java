package uk.gov.dvsa.mot.vehicle.hgv.model.moth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefectTextCategory {
    @JsonProperty("language")
    private String language;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public String getLanguage() {
        return language;
    }

    public DefectTextCategory setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DefectTextCategory setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public DefectTextCategory setName(String name) {
        this.name = name;
        return this;
    }
}
