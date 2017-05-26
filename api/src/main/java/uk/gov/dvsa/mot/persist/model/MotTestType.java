package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.util.Date ;

/**
 * The persistent class for the mot_test_type database table.
 * 
 */
public class MotTestType implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private String code ;
  private String description ;
  private int displayOrder ;
  private boolean isDemo ;
  private boolean isReinspection ;
  private boolean isSlotConsuming ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private int version ;

  public MotTestType()
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

  public String getDescription()
  {
    return this.description ;
  }

  public void setDescription( String description )
  {
    this.description = description ;
  }

  public int getDisplayOrder()
  {
    return this.displayOrder ;
  }

  public void setDisplayOrder( int displayOrder )
  {
    this.displayOrder = displayOrder ;
  }

  public boolean getIsDemo()
  {
    return this.isDemo ;
  }

  public void setIsDemo( boolean isDemo )
  {
    this.isDemo = isDemo ;
  }

  public boolean getIsReinspection()
  {
    return this.isReinspection ;
  }

  public void setIsReinspection( boolean isReinspection )
  {
    this.isReinspection = isReinspection ;
  }

  public boolean getIsSlotConsuming()
  {
    return this.isSlotConsuming ;
  }

  public void setIsSlotConsuming( boolean isSlotConsuming )
  {
    this.isSlotConsuming = isSlotConsuming ;
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
}