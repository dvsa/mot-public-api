package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the non_working_day_lookup database table.
 */
public class NonWorkingDayLookup implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private NonWorkingDayCountryLookup nonWorkingDayCountryLookup;
    private Date day;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;

    public NonWorkingDayLookup() {

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

    public Date getDay() {

        return this.day;
    }

    public void setDay(Date day) {

        this.day = day;
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

    public NonWorkingDayCountryLookup getNonWorkingDayCountryLookup() {

        return this.nonWorkingDayCountryLookup;
    }

    public void setNonWorkingDayCountryLookup(NonWorkingDayCountryLookup nonWorkingDayCountryLookup) {

        this.nonWorkingDayCountryLookup = nonWorkingDayCountryLookup;
    }

}