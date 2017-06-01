package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the country_lookup database table.
 */
public class CountryLookup implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String code;
    private String name;
    private int displayOrder;
    private String isoCode;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    private List<CountryOfRegistrationLookup> countryOfRegistrationLookups;
    private List<NonWorkingDayCountryLookup> nonWorkingDayCountryLookups;

    public CountryLookup() {

    }

    public int getId() {

        return this.id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getCode() {

        return this.code;
    }

    public void setCode(String code) {

        this.code = code;
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

    public int getDisplayOrder() {

        return this.displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {

        this.displayOrder = displayOrder;
    }

    public String getIsoCode() {

        return this.isoCode;
    }

    public void setIsoCode(String isoCode) {

        this.isoCode = isoCode;
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

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public List<CountryOfRegistrationLookup> getCountryOfRegistrationLookups() {

        return this.countryOfRegistrationLookups;
    }

    public void setCountryOfRegistrationLookups(List<CountryOfRegistrationLookup> countryOfRegistrationLookups) {

        this.countryOfRegistrationLookups = countryOfRegistrationLookups;
    }

    public CountryOfRegistrationLookup addCountryOfRegistrationLookup(
            CountryOfRegistrationLookup countryOfRegistrationLookup) {

        getCountryOfRegistrationLookups().add(countryOfRegistrationLookup);
        countryOfRegistrationLookup.setCountryLookup(this);

        return countryOfRegistrationLookup;
    }

    public CountryOfRegistrationLookup removeCountryOfRegistrationLookup(
            CountryOfRegistrationLookup countryOfRegistrationLookup) {

        getCountryOfRegistrationLookups().remove(countryOfRegistrationLookup);
        countryOfRegistrationLookup.setCountryLookup(null);

        return countryOfRegistrationLookup;
    }

    public List<NonWorkingDayCountryLookup> getNonWorkingDayCountryLookups() {

        return this.nonWorkingDayCountryLookups;
    }

    public void setNonWorkingDayCountryLookups(List<NonWorkingDayCountryLookup> nonWorkingDayCountryLookups) {

        this.nonWorkingDayCountryLookups = nonWorkingDayCountryLookups;
    }

    public NonWorkingDayCountryLookup addNonWorkingDayCountryLookup(
            NonWorkingDayCountryLookup nonWorkingDayCountryLookup) {

        getNonWorkingDayCountryLookups().add(nonWorkingDayCountryLookup);
        nonWorkingDayCountryLookup.setCountryLookup(this);

        return nonWorkingDayCountryLookup;
    }

    public NonWorkingDayCountryLookup removeNonWorkingDayCountryLookup(
            NonWorkingDayCountryLookup nonWorkingDayCountryLookup) {

        getNonWorkingDayCountryLookups().remove(nonWorkingDayCountryLookup);
        nonWorkingDayCountryLookup.setCountryLookup(null);

        return nonWorkingDayCountryLookup;
    }

  /*
   * public List<Qualification> getQualifications() { return
   * this.qualifications; }
   * 
   * public void setQualifications( List<Qualification> qualifications ) {
   * this.qualifications = qualifications; }
   * 
   * public Qualification addQualification( Qualification qualification ) {
   * getQualifications().add( qualification ); qualification.setCountryLookup(
   * this );
   * 
   * return qualification; }
   * 
   * public Qualification removeQualification( Qualification qualification ) {
   * getQualifications().remove( qualification );
   * qualification.setCountryLookup( null );
   * 
   * return qualification; }
   */
}