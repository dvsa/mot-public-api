package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the non_working_day_country_lookup database table.
 */
public class NonWorkingDayCountryLookup implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private CountryLookup countryLookup;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    private List<NonWorkingDayLookup> nonWorkingDayLookups;

    public NonWorkingDayCountryLookup() {

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

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public CountryLookup getCountryLookup() {

        return this.countryLookup;
    }

    public void setCountryLookup(CountryLookup countryLookup) {

        this.countryLookup = countryLookup;
    }

    public List<NonWorkingDayLookup> getNonWorkingDayLookups() {

        return this.nonWorkingDayLookups;
    }

    public void setNonWorkingDayLookups(List<NonWorkingDayLookup> nonWorkingDayLookups) {

        this.nonWorkingDayLookups = nonWorkingDayLookups;
    }

    public NonWorkingDayLookup addNonWorkingDayLookup(NonWorkingDayLookup nonWorkingDayLookup) {

        getNonWorkingDayLookups().add(nonWorkingDayLookup);
        nonWorkingDayLookup.setNonWorkingDayCountryLookup(this);

        return nonWorkingDayLookup;
    }

    public NonWorkingDayLookup removeNonWorkingDayLookup(NonWorkingDayLookup nonWorkingDayLookup) {

        getNonWorkingDayLookups().remove(nonWorkingDayLookup);
        nonWorkingDayLookup.setNonWorkingDayCountryLookup(null);

        return nonWorkingDayLookup;
    }

  /*
   * public List<Site> getSites() { return this.sites; }
   * 
   * public void setSites( List<Site> sites ) { this.sites = sites; }
   * 
   * public Site addSite( Site site ) { getSites().add( site );
   * site.setNonWorkingDayCountryLookup( this );
   * 
   * return site; }
   * 
   * public Site removeSite( Site site ) { getSites().remove( site );
   * site.setNonWorkingDayCountryLookup( null );
   * 
   * return site; }
   */
}