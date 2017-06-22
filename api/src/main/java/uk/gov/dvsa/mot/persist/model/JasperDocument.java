package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the jasper_document database table.
 */
public class JasperDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String documentContent;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;
    private JasperTemplate jasperTemplate;
    private List<JasperHardCopy> jasperHardCopies;

    public JasperDocument() {

    }

    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

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

    public String getDocumentContent() {

        return this.documentContent;
    }

    public void setDocumentContent(String documentContent) {

        this.documentContent = documentContent;
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

    public JasperTemplate getJasperTemplate() {

        return this.jasperTemplate;
    }

    public void setJasperTemplate(JasperTemplate jasperTemplate) {

        this.jasperTemplate = jasperTemplate;
    }

    public List<JasperHardCopy> getJasperHardCopies() {

        return this.jasperHardCopies;
    }

    public void setJasperHardCopies(List<JasperHardCopy> jasperHardCopies) {

        this.jasperHardCopies = jasperHardCopies;
    }

    public JasperHardCopy addJasperHardCopy(JasperHardCopy jasperHardCopy) {

        getJasperHardCopies().add(jasperHardCopy);
        jasperHardCopy.setJasperDocument(this);

        return jasperHardCopy;
    }

    public JasperHardCopy removeJasperHardCopy(JasperHardCopy jasperHardCopy) {

        getJasperHardCopies().remove(jasperHardCopy);
        jasperHardCopy.setJasperDocument(null);

        return jasperHardCopy;
    }

  /*
   * public List<CertificateReplacement> getCertificateReplacements() { return
   * this.certificateReplacements; }
   * 
   * public void setCertificateReplacements( List<CertificateReplacement>
   * certificateReplacements ) { this.certificateReplacements =
   * certificateReplacements; }
   * 
   * public CertificateReplacement addCertificateReplacement(
   * CertificateReplacement certificateReplacement ) {
   * getCertificateReplacements().add( certificateReplacement );
   * certificateReplacement.setJasperDocument( this );
   * 
   * return certificateReplacement; }
   * 
   * public CertificateReplacement removeCertificateReplacement(
   * CertificateReplacement certificateReplacement ) {
   * getCertificateReplacements().remove( certificateReplacement );
   * certificateReplacement.setJasperDocument( null );
   * 
   * return certificateReplacement; }
   * 
   * public JasperTemplate getJasperTemplate() { return this.jasperTemplate; }
   * 
   * public List<MotTest> getMotTests() { return this.motTests; }
   * 
   * public void setMotTests( List<MotTest> motTests ) { this.motTests =
   * motTests; }
   * 
   * public MotTest addMotTest( MotTest motTest ) { getMotTests().add( motTest
   * ); motTest.setJasperDocument( this );
   * 
   * return motTest; }
   * 
   * public MotTest removeMotTest( MotTest motTest ) { getMotTests().remove(
   * motTest ); motTest.setJasperDocument( null );
   * 
   * return motTest; }
   * 
   * public List<MotTestEvent> getMotTestEvents() { return this.motTestEvents; }
   * 
   * public void setMotTestEvents( List<MotTestEvent> motTestEvents ) {
   * this.motTestEvents = motTestEvents; }
   * 
   * public MotTestEvent addMotTestEvent( MotTestEvent motTestEvent ) {
   * getMotTestEvents().add( motTestEvent ); motTestEvent.setJasperDocument(
   * this );
   * 
   * return motTestEvent; }
   * 
   * public MotTestEvent removeMotTestEvent( MotTestEvent motTestEvent ) {
   * getMotTestEvents().remove( motTestEvent ); motTestEvent.setJasperDocument(
   * null );
   * 
   * return motTestEvent; }
   */
}