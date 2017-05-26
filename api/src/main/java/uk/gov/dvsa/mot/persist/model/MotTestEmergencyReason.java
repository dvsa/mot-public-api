package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.sql.Timestamp ;

/**
 * The persistent class for the mot_test_emergency_reason database table.
 * 
 */
public class MotTestEmergencyReason implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private long id ;
  private EmergencyLog emergencyLog ;
  private Comment comment ;
  private EmergencyReasonLookup emergencyReasonLookup ;
  private int createdBy ;
  private Timestamp createdOn ;
  private int lastUpdatedBy ;
  private Timestamp lastUpdatedOn ;
  private int version ;
  private MotTest motTestCurrent ;
  private MotTestHistory motTestHistory ;

  public MotTestEmergencyReason()
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

  public EmergencyLog getEmergencyLog()
  {
    return this.emergencyLog ;
  }

  public void setEmergencyLog( EmergencyLog emergencyLog )
  {
    this.emergencyLog = emergencyLog ;
  }

  public Comment getComment()
  {
    return this.comment ;
  }

  public void setComment( Comment comment )
  {
    this.comment = comment ;
  }

  public EmergencyReasonLookup getEmergencyReasonLookup()
  {
    return this.emergencyReasonLookup ;
  }

  public void setEmergencyReasonLookup( EmergencyReasonLookup emergencyReasonLookup )
  {
    this.emergencyReasonLookup = emergencyReasonLookup ;
  }

  public MotTest getMotTestCurrent()
  {
    return this.motTestCurrent ;
  }

  public void setMotTestCurrent( MotTest motTestCurrent )
  {
    this.motTestCurrent = motTestCurrent ;
  }

  public MotTestHistory getMotTestHistory()
  {
    return this.motTestHistory ;
  }

  public void setMotTestHistory( MotTestHistory motTestHistory )
  {
    this.motTestHistory = motTestHistory ;
  }

}