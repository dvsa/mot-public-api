package uk.gov.dvsa.mot.vehicle.hgv.model.moth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GarageDetails {
    @JsonProperty("name")
    private String name;

    @JsonProperty("address1")
    private String address1;

    @JsonProperty("address2")
    private String address2;

    @JsonProperty("town")
    private String town;

    @JsonProperty("postcode")
    private String postcode;

    public String getName() {
        return name;
    }

    public GarageDetails setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress1() {
        return address1;
    }

    public GarageDetails setAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    public String getAddress2() {
        return address2;
    }

    public GarageDetails setAddress2(String address2) {
        this.address2 = address2;
        return this;
    }

    public String getTown() {
        return town;
    }

    public GarageDetails setTown(String town) {
        this.town = town;
        return this;
    }

    public String getPostcode() {
        return postcode;
    }

    public GarageDetails setPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s%s%s",
                this.name == null ? "" : this.name,
                this.address1 == null ? "" :  ", " + this.address1,
                this.address2 == null ? "" : ", " + this.address2,
                this.town == null ? "" : ", " + this.town,
                this.postcode == null ? "" : ", " + this.postcode);
    }
}
