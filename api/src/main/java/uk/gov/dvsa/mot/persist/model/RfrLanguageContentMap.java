package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.util.Date ;

/**
 * The persistent class for the rfr_language_content_map database table.
 * 
 */
public class RfrLanguageContentMap implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private String name ;
  private String testItemSelectorName ;
  private String inspectionManualDescription ;
  private String advisoryText ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private int version ;
  private LanguageType languageType ;
  private ReasonForRejection reasonForRejection ;

  public RfrLanguageContentMap()
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

  public String getAdvisoryText()
  {
    return this.advisoryText ;
  }

  public void setAdvisoryText( String advisoryText )
  {
    this.advisoryText = advisoryText ;
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

  public String getInspectionManualDescription()
  {
    return this.inspectionManualDescription ;
  }

  public void setInspectionManualDescription( String inspectionManualDescription )
  {
    this.inspectionManualDescription = inspectionManualDescription ;
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

  public String getName()
  {
    return this.name ;
  }

  public void setName( String name )
  {
    this.name = name ;
  }

  public String getTestItemSelectorName()
  {
    return this.testItemSelectorName ;
  }

  public void setTestItemSelectorName( String testItemSelectorName )
  {
    this.testItemSelectorName = testItemSelectorName ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public LanguageType getLanguageType()
  {
    return this.languageType ;
  }

  public void setLanguageType( LanguageType languageType )
  {
    this.languageType = languageType ;
  }

  public ReasonForRejection getReasonForRejection()
  {
    return this.reasonForRejection ;
  }

  public void setReasonForRejection( ReasonForRejection reasonForRejection )
  {
    this.reasonForRejection = reasonForRejection ;
  }

}