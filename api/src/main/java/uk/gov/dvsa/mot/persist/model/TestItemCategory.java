package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the test_item_category database table.
 */
public class TestItemCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int sectionTestItemCategoryId;
    private int version;
    private BusinessRule businessRule;
    private TestItemCategory parentTestItemCategory;
    private List<TiCategoryLanguageContentMap> tiCategoryLanguageContentMaps;

    public TestItemCategory() {

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

    public int getSectionTestItemCategoryId() {

        return this.sectionTestItemCategoryId;
    }

    public void setSectionTestItemCategoryId(int sectionTestItemCategoryId) {

        this.sectionTestItemCategoryId = sectionTestItemCategoryId;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public BusinessRule getBusinessRule() {

        return this.businessRule;
    }

    public void setBusinessRule(BusinessRule businessRule) {

        this.businessRule = businessRule;
    }

    public TestItemCategory getParentTestItemCategory() {

        return this.parentTestItemCategory;
    }

    public void setParentTestItemCategory(TestItemCategory testItemCategory) {

        this.parentTestItemCategory = testItemCategory;
    }

    public List<TiCategoryLanguageContentMap> getTiCategoryLanguageContentMaps() {

        return this.tiCategoryLanguageContentMaps;
    }

    public void setTiCategoryLanguageContentMaps(List<TiCategoryLanguageContentMap> tiCategoryLanguageContentMaps) {

        this.tiCategoryLanguageContentMaps = tiCategoryLanguageContentMaps;
    }

    public TiCategoryLanguageContentMap addTiCategoryLanguageContentMap(
            TiCategoryLanguageContentMap tiCategoryLanguageContentMap) {

        getTiCategoryLanguageContentMaps().add(tiCategoryLanguageContentMap);
        tiCategoryLanguageContentMap.setTestItemCategory(this);

        return tiCategoryLanguageContentMap;
    }

    public TiCategoryLanguageContentMap removeTiCategoryLanguageContentMap(
            TiCategoryLanguageContentMap tiCategoryLanguageContentMap) {

        getTiCategoryLanguageContentMaps().remove(tiCategoryLanguageContentMap);
        tiCategoryLanguageContentMap.setTestItemCategory(null);

        return tiCategoryLanguageContentMap;
    }

}