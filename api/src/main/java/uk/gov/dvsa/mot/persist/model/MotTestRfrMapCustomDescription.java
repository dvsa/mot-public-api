package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.sql.Timestamp ;

/**
 * The persistent class for the mot_test_rfr_map_custom_description database
 * table.
 * 
 */
public class MotTestRfrMapCustomDescription implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private long id ;
  private String customDescription ;
  private int createdBy ;
  private Timestamp createdOn ;
  private int lastUpdatedBy ;
  private Timestamp lastUpdatedOn ;
  private int version ;
  private MotTestRfrMap motTestCurrentRfrMap ;
  private MotTestHistoryRfrMap motTestHistoryRfrMap ;

  public MotTestRfrMapCustomDescription()
  {
  }

  public long getId()
  {
    return this.id ;
  }

  public void setId( long id )
  {
    this.id = id ;
  }

  public int getCreatedBy()
  {
    return this.createdBy ;
  }

  public void setCreatedBy( int createdBy )
  {
    this.createdBy = createdBy ;
  }

  public Timestamp getCreatedOn()
  {
    return this.createdOn ;
  }

  public void setCreatedOn( Timestamp createdOn )
  {
    this.createdOn = createdOn ;
  }

  public String getCustomDescription()
  {
    return this.customDescription ;
  }

  public void setCustomDescription( String customDescription )
  {
    this.customDescription = customDescription ;
  }

  public int getLastUpdatedBy()
  {
    return this.lastUpdatedBy ;
  }

  public void setLastUpdatedBy( int lastUpdatedBy )
  {
    this.lastUpdatedBy = lastUpdatedBy ;
  }

  public Timestamp getLastUpdatedOn()
  {
    return this.lastUpdatedOn ;
  }

  public void setLastUpdatedOn( Timestamp lastUpdatedOn )
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

  public MotTestRfrMap getMotTestCurrentRfrMap()
  {
    return this.motTestCurrentRfrMap ;
  }

  public void setMotTestCurrentRfrMap( MotTestRfrMap motTestCurrentRfrMap )
  {
    this.motTestCurrentRfrMap = motTestCurrentRfrMap ;
  }

  public MotTestHistoryRfrMap getMotTestHistoryRfrMap()
  {
    return this.motTestHistoryRfrMap ;
  }

  public void setMotTestHistoryRfrMap( MotTestHistoryRfrMap motTestHistoryRfrMap )
  {
    this.motTestHistoryRfrMap = motTestHistoryRfrMap ;
  }

}