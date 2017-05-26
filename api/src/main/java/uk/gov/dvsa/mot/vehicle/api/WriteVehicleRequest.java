package uk.gov.dvsa.mot.vehicle.api ;

//import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlElement ;
import javax.xml.bind.annotation.XmlType ;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "create_vehicle_request", propOrder = { "vehicle", "oneTimePassword" } )

public class WriteVehicleRequest
{

  @XmlElement( name = "vehicle" )
  private Vehicle vehicle ;

  // @Valid
  @XmlElement( name = "one_time_password" )
  private String oneTimePassword ;

  public Vehicle getVehicle()
  {
    return vehicle ;
  }

  public void setVehicle( Vehicle vehicle )
  {
    this.vehicle = vehicle ;
  }

  public String getOneTimePassword()
  {
    return oneTimePassword ;
  }

  public void setOneTimePassword( String oneTimePassword )
  {
    this.oneTimePassword = oneTimePassword ;
  }
}
