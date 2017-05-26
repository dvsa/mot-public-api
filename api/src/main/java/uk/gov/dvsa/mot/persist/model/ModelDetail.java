package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.sql.Timestamp ;

/**
 * The persistent class for the model_detail database table.
 * 
 */
public class ModelDetail implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private Model model ;
  private BodyType bodyType ;
  private FuelType fuelType ;
  private TransmissionType transmissionType ;
  private int cylinderCapacity ;
  private VehicleClass vehicleClass ;
  private String euClassification ;
  private WheelplanType wheelplanType ;
  private boolean isVerified ;
  private String sha1ConcatWsChksum ;
  private int createdBy ;
  private Timestamp createdOn ;
  private int lastUpdatedBy ;
  private Timestamp lastUpdatedOn ;
  private int version ;

  public ModelDetail()
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

  public int getCylinderCapacity()
  {
    return this.cylinderCapacity ;
  }

  public void setCylinderCapacity( int cylinderCapacity )
  {
    this.cylinderCapacity = cylinderCapacity ;
  }

  public String getEuClassification()
  {
    return this.euClassification ;
  }

  public void setEuClassification( String euClassification )
  {
    this.euClassification = euClassification ;
  }

  public boolean getIsVerified()
  {
    return this.isVerified ;
  }

  public void setIsVerified( boolean isVerified )
  {
    this.isVerified = isVerified ;
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

  public WheelplanType getWheelplanType()
  {
    return this.wheelplanType ;
  }

  public void setWheelplanType( WheelplanType wheelplanType )
  {
    this.wheelplanType = wheelplanType ;
  }

  public BodyType getBodyType()
  {
    return this.bodyType ;
  }

  public void setBodyType( BodyType bodyType )
  {
    this.bodyType = bodyType ;
  }

  public FuelType getFuelType()
  {
    return this.fuelType ;
  }

  public void setFuelType( FuelType fuelType )
  {
    this.fuelType = fuelType ;
  }

  public Model getModel()
  {
    return this.model ;
  }

  public void setModel( Model model )
  {
    this.model = model ;
  }

  public TransmissionType getTransmissionType()
  {
    return this.transmissionType ;
  }

  public void setTransmissionType( TransmissionType transmissionType )
  {
    this.transmissionType = transmissionType ;
  }

  public VehicleClass getVehicleClass()
  {
    return this.vehicleClass ;
  }

  public void setVehicleClass( VehicleClass vehicleClass )
  {
    this.vehicleClass = vehicleClass ;
  }
}