package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.util.Date ;
import java.util.List ;

/**
 * The persistent class for the jasper_template database table.
 * 
 */
public class JasperTemplate implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private JasperTemplateType jasperTemplateType ;
  private String jasperReportName ;
  private boolean isActive ;
  private Date activeFrom ;
  private Date activeTo ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private int version ;
  private List<JasperTemplateVariation> jasperTemplateVariations ;

  public JasperTemplate()
  {
  }

  public int getId()
  {
    return this.id ;
  }

  public void setId( int id )
  {
    this.id = id ;
  }

  public Date getActiveFrom()
  {
    return this.activeFrom ;
  }

  public void setActiveFrom( Date activeFrom )
  {
    this.activeFrom = activeFrom ;
  }

  public Date getActiveTo()
  {
    return this.activeTo ;
  }

  public void setActiveTo( Date activeTo )
  {
    this.activeTo = activeTo ;
  }

  public int getCreatedBy()
  {
    return this.createdBy ;
  }

  public void setCreatedBy( int createdBy )
  {
    this.createdBy = createdBy ;
  }

  public Date getCreatedOn()
  {
    return this.createdOn ;
  }

  public void setCreatedOn( Date createdOn )
  {
    this.createdOn = createdOn ;
  }

  public boolean getIsActive()
  {
    return this.isActive ;
  }

  public void setIsActive( boolean isActive )
  {
    this.isActive = isActive ;
  }

  public String getJasperReportName()
  {
    return this.jasperReportName ;
  }

  public void setJasperReportName( String jasperReportName )
  {
    this.jasperReportName = jasperReportName ;
  }

  public int getLastUpdatedBy()
  {
    return this.lastUpdatedBy ;
  }

  public void setLastUpdatedBy( int lastUpdatedBy )
  {
    this.lastUpdatedBy = lastUpdatedBy ;
  }

  public Date getLastUpdatedOn()
  {
    return this.lastUpdatedOn ;
  }

  public void setLastUpdatedOn( Date lastUpdatedOn )
  {
    this.lastUpdatedOn = lastUpdatedOn ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public JasperTemplateType getJasperTemplateType()
  {
    return this.jasperTemplateType ;
  }

  public void setJasperTemplateType( JasperTemplateType jasperTemplateType )
  {
    this.jasperTemplateType = jasperTemplateType ;
  }

  public List<JasperTemplateVariation> getJasperTemplateVariations()
  {
    return this.jasperTemplateVariations ;
  }

  public void setJasperTemplateVariations( List<JasperTemplateVariation> jasperTemplateVariations )
  {
    this.jasperTemplateVariations = jasperTemplateVariations ;
  }

  public JasperTemplateVariation addJasperTemplateVariation( JasperTemplateVariation jasperTemplateVariation )
  {
    getJasperTemplateVariations().add( jasperTemplateVariation ) ;
    jasperTemplateVariation.setJasperTemplate( this ) ;

    return jasperTemplateVariation ;
  }

  public JasperTemplateVariation removeJasperTemplateVariation( JasperTemplateVariation jasperTemplateVariation )
  {
    getJasperTemplateVariations().remove( jasperTemplateVariation ) ;
    jasperTemplateVariation.setJasperTemplate( null ) ;

    return jasperTemplateVariation ;
  }

}