package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the jasper_template_type database table.
 */
public class JasperTemplateType implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    private List<JasperTemplate> jasperTemplates;

    public JasperTemplateType() {

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

    public List<JasperTemplate> getJasperTemplates() {

        return this.jasperTemplates;
    }

    public void setJasperTemplates(List<JasperTemplate> jasperTemplates) {

        this.jasperTemplates = jasperTemplates;
    }

    public JasperTemplate addJasperTemplate(JasperTemplate jasperTemplate) {

        getJasperTemplates().add(jasperTemplate);
        jasperTemplate.setJasperTemplateType(this);

        return jasperTemplate;
    }

    public JasperTemplate removeJasperTemplate(JasperTemplate jasperTemplate) {

        getJasperTemplates().remove(jasperTemplate);
        jasperTemplate.setJasperTemplateType(null);

        return jasperTemplate;
    }

}