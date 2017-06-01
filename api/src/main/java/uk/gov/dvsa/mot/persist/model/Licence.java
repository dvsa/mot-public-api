package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the licence database table.
 */
public class Licence implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int createdBy;
    private Date createdOn;
    private Date expiryDate;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private String licenceNumber;
    private Date validFrom;
    private int version;
    private LicenceCountryLookup licenceCountryLookup;
    private LicenceType licenceType;

    public Licence() {

    }

    public int getId() {

        return this.id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getCreatedBy() {

        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {

        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {

        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {

        this.createdOn = createdOn;
    }

    public Date getExpiryDate() {

        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {

        this.expiryDate = expiryDate;
    }

    public int getLastUpdatedBy() {

        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(int lastUpdatedBy) {

        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedOn() {

        return this.lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {

        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getLicenceNumber() {

        return this.licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {

        this.licenceNumber = licenceNumber;
    }

    public Date getValidFrom() {

        return this.validFrom;
    }

    public void setValidFrom(Date validFrom) {

        this.validFrom = validFrom;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public LicenceCountryLookup getLicenceCountryLookup() {

        return this.licenceCountryLookup;
    }

    public void setLicenceCountryLookup(LicenceCountryLookup licenceCountryLookup) {

        this.licenceCountryLookup = licenceCountryLookup;
    }

    public LicenceType getLicenceType() {

        return this.licenceType;
    }

    public void setLicenceType(LicenceType licenceType) {

        this.licenceType = licenceType;
    }
}