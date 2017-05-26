package uk.gov.dvsa.mot.trade.api;

import java.util.List ;

public class MotTest
{
  protected Long id ;
  protected Integer vehicleId ;
  protected Integer vehicleVersion ;
  protected String completedDate ;
  protected String testResult ;
  protected String expiryDate ;
  protected String odometerValue ;
  protected String odometerUnit ;
  protected String motTestNumber ;
  protected List<RfrAndAdvisoryItem> rfrAndComments ;
  
  public String getCompletedDate()
  {
    return completedDate;
  }
  public void setCompletedDate( String completedDate )
  {
    this.completedDate = completedDate;
  }
  public String getTestResult()
  {
    return testResult;
  }
  public void setTestResult( String testResult )
  {
    this.testResult = testResult;
  }
  public String getExpiryDate()
  {
    return expiryDate;
  }
  public void setExpiryDate( String expiryDate )
  {
    this.expiryDate = expiryDate;
  }
  public String getOdometerValue()
  {
    return odometerValue;
  }
  public void setOdometerValue( String odometerValue )
  {
    this.odometerValue = odometerValue;
  }
  public String getOdometerUnit()
  {
    return odometerUnit;
  }
  public void setOdometerUnit( String odometerUnit )
  {
    this.odometerUnit = odometerUnit;
  }
  public String getMotTestNumber()
  {
    return motTestNumber;
  }
  public void setMotTestNumber( String motTestNumber )
  {
    this.motTestNumber = motTestNumber;
  }
  public List<RfrAndAdvisoryItem> getRfrAndComments()
  {
    return rfrAndComments;
  }
  public void setRfrAndComments( List<RfrAndAdvisoryItem> rfrAndComments )
  {
    this.rfrAndComments = rfrAndComments;
  }
  public Integer getVehicleId()
  {
    return vehicleId;
  }
  public void setVehicleId( Integer vehicleId )
  {
    this.vehicleId = vehicleId;
  }
  public Integer getVehicleVersion()
  {
    return vehicleVersion;
  }
  public void setVehicleVersion( Integer vehicleVersion )
  {
    this.vehicleVersion = vehicleVersion;
  }
  public Long getId()
  {
    return id;
  }
  public void setId( Long id )
  {
    this.id = id;
  }
}
