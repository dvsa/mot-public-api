package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the jasper_hard_copy database table.
 */
public class JasperHardCopy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String filePath;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    private JasperDocument jasperDocument;

    public JasperHardCopy() {

    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

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

    public String getFilePath() {

        return this.filePath;
    }

    public void setFilePath(String filePath) {

        this.filePath = filePath;
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

    public JasperDocument getJasperDocument() {

        return this.jasperDocument;
    }

    public void setJasperDocument(JasperDocument jasperDocument) {

        this.jasperDocument = jasperDocument;
    }

}