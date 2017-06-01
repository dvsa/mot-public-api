package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;
//import java.util.List;

/**
 * The persistent class for the transmission_type database table.
 */
public class TransmissionType implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String code;
    private String name;
    private int displayOrder;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    // private List<ModelDetail> modelDetails;

    public TransmissionType() {

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

  /*
   * public List<ModelDetail> getModelDetails() { return this.modelDetails; }
   * 
   * public void setModelDetails( List<ModelDetail> modelDetails ) {
   * this.modelDetails = modelDetails; }
   * 
   * public ModelDetail addModelDetail( ModelDetail modelDetail ) {
   * getModelDetails().add( modelDetail ); modelDetail.setTransmissionType( this
   * );
   * 
   * return modelDetail; }
   * 
   * public ModelDetail removeModelDetail( ModelDetail modelDetail ) {
   * getModelDetails().remove( modelDetail ); modelDetail.setTransmissionType(
   * null );
   * 
   * return modelDetail; }
   */
}