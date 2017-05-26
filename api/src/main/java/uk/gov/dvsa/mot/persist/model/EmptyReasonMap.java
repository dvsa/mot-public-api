package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.sql.Timestamp ;

/**
 * The persistent class for the empty_reason_map database table.
 * 
 */
public class EmptyReasonMap implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private int createdBy ;
  private Timestamp createdOn ;
  private int lastUpdatedBy ;
  private Timestamp lastUpdatedOn ;
  private int version ;
  private EmptyVinReasonLookup emptyVinReasonLookup ;
  private EmptyVrmReasonLookup emptyVrmReasonLookup ;
  private Vehicle vehicle ;

  public EmptyReasonMap()
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

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public EmptyVinReasonLookup getEmptyVinReasonLookup()
  {
    return this.emptyVinReasonLookup ;
  }

  public void setEmptyVinReasonLookup( EmptyVinReasonLookup emptyVinReasonLookup )
  {
    this.emptyVinReasonLookup = emptyVinReasonLookup ;
  }

  public EmptyVrmReasonLookup getEmptyVrmReasonLookup()
  {
    return this.emptyVrmReasonLookup ;
  }

  public void setEmptyVrmReasonLookup( EmptyVrmReasonLookup emptyVrmReasonLookup )
  {
    this.emptyVrmReasonLookup = emptyVrmReasonLookup ;
  }

  public Vehicle getVehicle()
  {
    return this.vehicle ;
  }

  public void setVehicle( Vehicle vehicle )
  {
    this.vehicle = vehicle ;
  }

}