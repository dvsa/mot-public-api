package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.util.Date ;

/**
 * The persistent class for the mot_test_reason_for_cancel_lookup database
 * table.
 * 
 */
public class MotTestReasonForCancelLookup implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private String code ;
  private String reason ;
  private String reasonCy ;
  private boolean isAbandoned ;
  private boolean isDisplayable ;
  private boolean isSystemGenerated ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private int version ;

  public MotTestReasonForCancelLookup()
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

  public String getCode()
  {
    return this.code ;
  }

  public void setCode( String code )
  {
    this.code = code ;
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

  public boolean getIsAbandoned()
  {
    return this.isAbandoned ;
  }

  public void setIsAbandoned( boolean isAbandoned )
  {
    this.isAbandoned = isAbandoned ;
  }

  public boolean getIsDisplayable()
  {
    return this.isDisplayable ;
  }

  public void setIsDisplayable( boolean isDisplayable )
  {
    this.isDisplayable = isDisplayable ;
  }

  public boolean getIsSystemGenerated()
  {
    return this.isSystemGenerated ;
  }

  public void setIsSystemGenerated( boolean isSystemGenerated )
  {
    this.isSystemGenerated = isSystemGenerated ;
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

  public String getReason()
  {
    return this.reason ;
  }

  public void setReason( String reason )
  {
    this.reason = reason ;
  }

  public String getReasonCy()
  {
    return this.reasonCy ;
  }

  public void setReasonCy( String reasonCy )
  {
    this.reasonCy = reasonCy ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }
}