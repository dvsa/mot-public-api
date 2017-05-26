package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.util.Date ;
import java.util.List ;

/**
 * The persistent class for the reason_for_rejection database table.
 * 
 */
public class ReasonForRejection implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private TestItemCategory testItemCategory ;
  private TestItemCategory sectionTestItemCategory ;
  private String testItemSelectorName ;
  private String testItemSelectorNameCy ;
  private Date dateFirstUsed ;
  private Date endDate ;
  private String manual ;
  private String inspectionManualReference ;
  private boolean isAdvisory ;
  private boolean isPrsFail ;
  private String audience ;
  private boolean canBeDangerous ;
  private boolean locationMarker ;
  private boolean minorItem ;
  private boolean note ;
  private boolean qtMarker ;
  private boolean specProc ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private int version ;
  private List<RfrLanguageContentMap> rfrLanguageContentMaps ;

  public ReasonForRejection()
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

  public String getAudience()
  {
    return this.audience ;
  }

  public void setAudience( String audience )
  {
    this.audience = audience ;
  }

  public boolean getCanBeDangerous()
  {
    return this.canBeDangerous ;
  }

  public void setCanBeDangerous( boolean canBeDangerous )
  {
    this.canBeDangerous = canBeDangerous ;
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

  public Date getDateFirstUsed()
  {
    return this.dateFirstUsed ;
  }

  public void setDateFirstUsed( Date dateFirstUsed )
  {
    this.dateFirstUsed = dateFirstUsed ;
  }

  public Date getEndDate()
  {
    return this.endDate ;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate ;
  }

  public String getInspectionManualReference()
  {
    return this.inspectionManualReference ;
  }

  public void setInspectionManualReference( String inspectionManualReference )
  {
    this.inspectionManualReference = inspectionManualReference ;
  }

  public boolean getIsAdvisory()
  {
    return this.isAdvisory ;
  }

  public void setIsAdvisory( boolean isAdvisory )
  {
    this.isAdvisory = isAdvisory ;
  }

  public boolean getIsPrsFail()
  {
    return this.isPrsFail ;
  }

  public void setIsPrsFail( boolean isPrsFail )
  {
    this.isPrsFail = isPrsFail ;
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

  public boolean getLocationMarker()
  {
    return this.locationMarker ;
  }

  public void setLocationMarker( boolean locationMarker )
  {
    this.locationMarker = locationMarker ;
  }

  public String getManual()
  {
    return this.manual ;
  }

  public void setManual( String manual )
  {
    this.manual = manual ;
  }

  public boolean getMinorItem()
  {
    return this.minorItem ;
  }

  public void setMinorItem( boolean minorItem )
  {
    this.minorItem = minorItem ;
  }

  public boolean getNote()
  {
    return this.note ;
  }

  public void setNote( boolean note )
  {
    this.note = note ;
  }

  public boolean getQtMarker()
  {
    return this.qtMarker ;
  }

  public void setQtMarker( boolean qtMarker )
  {
    this.qtMarker = qtMarker ;
  }

  public boolean getSpecProc()
  {
    return this.specProc ;
  }

  public void setSpecProc( boolean specProc )
  {
    this.specProc = specProc ;
  }

  public String getTestItemSelectorName()
  {
    return this.testItemSelectorName ;
  }

  public void setTestItemSelectorName( String testItemSelectorName )
  {
    this.testItemSelectorName = testItemSelectorName ;
  }

  public String getTestItemSelectorNameCy()
  {
    return this.testItemSelectorNameCy ;
  }

  public void setTestItemSelectorNameCy( String testItemSelectorNameCy )
  {
    this.testItemSelectorNameCy = testItemSelectorNameCy ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public TestItemCategory getSectionTestItemCategory()
  {
    return this.sectionTestItemCategory ;
  }

  public void setSectionTestItemCategory( TestItemCategory sectionTestItemCategory )
  {
    this.sectionTestItemCategory = sectionTestItemCategory ;
  }

  public TestItemCategory getTestItemCategory()
  {
    return this.testItemCategory ;
  }

  public void setTestItemCategory( TestItemCategory testItemCategory )
  {
    this.testItemCategory = testItemCategory ;
  }

  public List<RfrLanguageContentMap> getRfrLanguageContentMaps()
  {
    return rfrLanguageContentMaps ;
  }

  public void setTiRfrLanguageContentMaps( List<RfrLanguageContentMap> tiRfrLanguageContentMaps )
  {
    this.rfrLanguageContentMaps = tiRfrLanguageContentMaps ;
  }

  public void setAdvisory( boolean isAdvisory )
  {
    this.isAdvisory = isAdvisory ;
  }

  public void setPrsFail( boolean isPrsFail )
  {
    this.isPrsFail = isPrsFail ;
  }
}