package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the ti_category_language_content_map database table.
 */
public class TiCategoryLanguageContentMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private int displayOrder;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    private LanguageType languageType;
    private TestItemCategory testItemCategory;

    public TiCategoryLanguageContentMap() {

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

    public String getDescription() {

        return this.description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public int getDisplayOrder() {

        return this.displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {

        this.displayOrder = displayOrder;
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

    public LanguageType getLanguageType() {

        return this.languageType;
    }

    public void setLanguageType(LanguageType languageType) {

        this.languageType = languageType;
    }

    public TestItemCategory getTestItemCategory() {

        return this.testItemCategory;
    }

    public void setTestItemCategory(TestItemCategory testItemCategory) {

        this.testItemCategory = testItemCategory;
    }

}