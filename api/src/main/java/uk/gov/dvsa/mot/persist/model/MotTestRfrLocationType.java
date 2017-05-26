package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.sql.Timestamp ;

/**
 * The persistent class for the mot_test_rfr_location_type database table.
 * 
 */
public class MotTestRfrLocationType implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private String lateral ;
  private String longitudinal ;
  private String vertical ;
  private String sha1ConcatWsChksum ;
  private int createdBy ;
  private Timestamp createdOn ;
  private int lastUpdatedBy ;
  private Timestamp lastUpdatedOn ;
  private int version ;

  public MotTestRfrLocationType()
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

  public String getLateral()
  {
    return this.lateral ;
  }

  public void setLateral( String lateral )
  {
    this.lateral = lateral ;
  }

  public String getLongitudinal()
  {
    return this.longitudinal ;
  }

  public void setLongitudinal( String longitudinal )
  {
    this.longitudinal = longitudinal ;
  }

  public String getSha1ConcatWsChksum()
  {
    return this.sha1ConcatWsChksum ;
  }

  public void setSha1ConcatWsChksum( String sha1ConcatWsChksum )
  {
    this.sha1ConcatWsChksum = sha1ConcatWsChksum ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public String getVertical()
  {
    return this.vertical ;
  }

  public void setVertical( String vertical )
  {
    this.vertical = vertical ;
  }
}