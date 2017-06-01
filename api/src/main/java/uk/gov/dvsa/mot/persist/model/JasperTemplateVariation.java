package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the jasper_template_variation database table.
 */
public class JasperTemplateVariation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String jasperReportName;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    private JasperTemplate jasperTemplate;

    public JasperTemplateVariation() {

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

    public String getJasperReportName() {

        return this.jasperReportName;
    }

    public void setJasperReportName(String jasperReportName) {

        this.jasperReportName = jasperReportName;
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

    public JasperTemplate getJasperTemplate() {

        return this.jasperTemplate;
    }

    public void setJasperTemplate(JasperTemplate jasperTemplate) {

        this.jasperTemplate = jasperTemplate;
    }

}