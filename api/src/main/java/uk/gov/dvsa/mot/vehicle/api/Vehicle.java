package uk.gov.dvsa.mot.vehicle.api ;

import java.util.Date ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlElement ;
import javax.xml.bind.annotation.XmlType ;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter ;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "vehicle", propOrder = { "id",
    // dvsa create fields
    "registration", "vin", "make", "model", "vehicleClass", "transmissionType", "cylinderCapacity", "primaryColour",
    "secondaryColour", "fuelType", "firstUsedDate", "emptyVrmReason", "emptyVinReason", "countryOfRegistration",

    // additional dvla create fields
    "dvlaVehicleId", "bodyType", "weight", "weightSource", "firstRegistrationDate", "manufactureDate",
    "isNewAtFirstReg",

    // fields not in either create
    "year", "engineNumber", "chassisNumber", "isDamaged", "isDestroyed", "isIncognito", "wheelplan", "euClassification",
    "createdBy", "createdOn", "lastUpdatedBy", "lastUpdatedOn", "version" } )
public class Vehicle
{
  @XmlElement( name = "id" )
  protected int id ;
  @XmlElement( name = "registration" )
  protected String registration ;
  @XmlElement( name = "vin" )
  protected String vin ;
  @XmlElement( name = "make" )
  protected String make ;
  @XmlElement( name = "model" )
  protected String model ;
  @XmlElement( name = "vehicle_class" )
  protected String vehicleClass ;
  @XmlElement( name = "transmission_type" )
  protected String transmissionType ;
  @XmlElement( name = "fuel_type" )
  protected String fuelType ;
  @XmlElement( name = "cylinder_capacity" )
  protected Integer cylinderCapacity ;
  @XmlElement( name = "primary_colour" )
  protected String primaryColour ;
  @XmlElement( name = "secondary_colour" )
  protected String secondaryColour ;
  @XmlElement( name = "first_used_date" )
  @XmlJavaTypeAdapter( DateFormatAdapter.class )
  protected Date firstUsedDate ;
  @XmlElement( name = "empty_vrm_reason" )
  protected String emptyVrmReason ;
  @XmlElement( name = "empty_vin_reason" )
  protected String emptyVinReason ;
  @XmlElement( name = "country_of_registration" )
  protected String countryOfRegistration ;

  @XmlElement( name = "year" )
  @XmlJavaTypeAdapter( DateFormatAdapter.class )
  protected Date year ;
  @XmlElement( name = "manufacture_date" )
  @XmlJavaTypeAdapter( DateFormatAdapter.class )
  protected Date manufactureDate ;
  @XmlElement( name = "first_registration_date" )
  @XmlJavaTypeAdapter( DateFormatAdapter.class )
  protected Date firstRegistrationDate ;
  protected int weight ;
  @XmlElement( name = "weight_source" )
  protected String weightSource ;
  @XmlElement( name = "engine_number" )
  protected String engineNumber ;
  @XmlElement( name = "chassis_number" )
  protected String chassisNumber ;
  @XmlElement( name = "dvla_vehicle_id" )
  protected int dvlaVehicleId ;
  @XmlElement( name = "is_new_at_first_reg" )
  protected boolean isNewAtFirstReg ;
  @XmlElement( name = "is_damaged" )
  protected boolean isDamaged ;
  @XmlElement( name = "is_destroyed" )
  protected boolean isDestroyed ;
  @XmlElement( name = "is_incognito" )
  protected boolean isIncognito ;
  @XmlElement( name = "body_type" )
  protected String bodyType ;
  @XmlElement( required = true )
  protected String wheelplan ;
  @XmlElement( name = "eu_classification" )
  protected String euClassification ;
  @XmlElement( name = "created_by" )
  protected String createdBy ;
  @XmlElement( name = "created_on" )
  @XmlJavaTypeAdapter( DateFormatAdapter.class )
  protected Date createdOn ;
  @XmlElement( name = "last_updated_by" )
  protected String lastUpdatedBy ;
  @XmlElement( name = "last_updated_on" )
  @XmlJavaTypeAdapter( DateFormatAdapter.class )
  protected Date lastUpdatedOn ;
  @XmlElement( name = "version" )
  protected int version ;

  public String getTransmissionType()
  {
    return transmissionType ;
  }

  public void setTransmissionType( String transmissionType )
  {
    this.transmissionType = transmissionType ;
  }

  public String getEmptyVrmReason()
  {
    return emptyVrmReason ;
  }

  public void setEmptyVrmReason( String emptyVrmReason )
  {
    this.emptyVrmReason = emptyVrmReason ;
  }

  public String getEmptyVinReason()
  {
    return emptyVinReason ;
  }

  public void setEmptyVinReason( String emptyVinReason )
  {
    this.emptyVinReason = emptyVinReason ;
  }

  public boolean isNewAtFirstReg()
  {
    return isNewAtFirstReg ;
  }

  public void setNewAtFirstReg( boolean isNewAtFirstReg )
  {
    this.isNewAtFirstReg = isNewAtFirstReg ;
  }

  public boolean isDamaged()
  {
    return isDamaged ;
  }

  public void setDamaged( boolean isDamaged )
  {
    this.isDamaged = isDamaged ;
  }

  public boolean isDestroyed()
  {
    return isDestroyed ;
  }

  public void setDestroyed( boolean isDestroyed )
  {
    this.isDestroyed = isDestroyed ;
  }

  public boolean isIncognito()
  {
    return isIncognito ;
  }

  public void setIncognito( boolean isIncognito )
  {
    this.isIncognito = isIncognito ;
  }

  /**
   * Gets the value of the id property.
   * 
   */
  public int getId()
  {
    return id ;
  }

  /**
   * Sets the value of the id property.
   * 
   */
  public void setId( int value )
  {
    this.id = value ;
  }

  /**
   * Gets the value of the registration property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getRegistration()
  {
    return registration ;
  }

  /**
   * Sets the value of the registration property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setRegistration( String value )
  {
    this.registration = value ;
  }

  /**
   * Gets the value of the vin property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getVin()
  {
    return vin ;
  }

  /**
   * Sets the value of the vin property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setVin( String value )
  {
    this.vin = value ;
  }

  /**
   * Gets the value of the year property.
   * 
   * @return possible object is {@link Date }
   * 
   */
  public Date getYear()
  {
    return year ;
  }

  /**
   * Sets the value of the year property.
   * 
   * @param value
   *          allowed object is {@link Date }
   * 
   */
  public void setYear( Date value )
  {
    this.year = value ;
  }

  /**
   * Gets the value of the manufactureDate property.
   * 
   * @return possible object is {@link Date }
   * 
   */
  public Date getManufactureDate()
  {
    return manufactureDate ;
  }

  /**
   * Sets the value of the manufactureDate property.
   * 
   * @param value
   *          allowed object is {@link Date }
   * 
   */
  public void setManufactureDate( Date value )
  {
    this.manufactureDate = value ;
  }

  /**
   * Gets the value of the firstRegistrationDate property.
   * 
   * @return possible object is {@link Date }
   * 
   */
  public Date getFirstRegistrationDate()
  {
    return firstRegistrationDate ;
  }

  /**
   * Sets the value of the firstRegistrationDate property.
   * 
   * @param value
   *          allowed object is {@link Date }
   * 
   */
  public void setFirstRegistrationDate( Date value )
  {
    this.firstRegistrationDate = value ;
  }

  /**
   * Gets the value of the firstUsedDate property.
   * 
   * @return possible object is {@link Date }
   * 
   */
  public Date getFirstUsedDate()
  {
    return firstUsedDate ;
  }

  /**
   * Sets the value of the firstUsedDate property.
   * 
   * @param value
   *          allowed object is {@link Date }
   * 
   */
  public void setFirstUsedDate( Date value )
  {
    this.firstUsedDate = value ;
  }

  /**
   * Gets the value of the primaryColour property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getPrimaryColour()
  {
    return primaryColour ;
  }

  /**
   * Sets the value of the primaryColour property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setPrimaryColour( String value )
  {
    this.primaryColour = value ;
  }

  /**
   * Gets the value of the secondaryColour property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getSecondaryColour()
  {
    return secondaryColour ;
  }

  /**
   * Sets the value of the secondaryColour property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setSecondaryColour( String value )
  {
    this.secondaryColour = value ;
  }

  /**
   * Gets the value of the weight property.
   * 
   */
  public int getWeight()
  {
    return weight ;
  }

  /**
   * Sets the value of the weight property.
   * 
   */
  public void setWeight( int value )
  {
    this.weight = value ;
  }

  /**
   * Gets the value of the weightSource property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getWeightSource()
  {
    return weightSource ;
  }

  /**
   * Sets the value of the weightSource property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setWeightSource( String value )
  {
    this.weightSource = value ;
  }

  /**
   * Gets the value of the countryOfRegistration property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCountryOfRegistration()
  {
    return countryOfRegistration ;
  }

  /**
   * Sets the value of the countryOfRegistration property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCountryOfRegistration( String value )
  {
    this.countryOfRegistration = value ;
  }

  /**
   * Gets the value of the engineNumber property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getEngineNumber()
  {
    return engineNumber ;
  }

  /**
   * Sets the value of the engineNumber property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setEngineNumber( String value )
  {
    this.engineNumber = value ;
  }

  /**
   * Gets the value of the chassisNumber property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getChassisNumber()
  {
    return chassisNumber ;
  }

  /**
   * Sets the value of the chassisNumber property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setChassisNumber( String value )
  {
    this.chassisNumber = value ;
  }

  /**
   * Gets the value of the dvlaVehicleId property.
   * 
   */
  public int getDvlaVehicleId()
  {
    return dvlaVehicleId ;
  }

  /**
   * Sets the value of the dvlaVehicleId property.
   * 
   */
  public void setDvlaVehicleId( int value )
  {
    this.dvlaVehicleId = value ;
  }

  /**
   * Gets the value of the isNewAtFirstReg property.
   * 
   */
  public boolean isIsNewAtFirstReg()
  {
    return isNewAtFirstReg ;
  }

  /**
   * Sets the value of the isNewAtFirstReg property.
   * 
   */
  public void setIsNewAtFirstReg( boolean value )
  {
    this.isNewAtFirstReg = value ;
  }

  /**
   * Gets the value of the isDamaged property.
   * 
   */
  public boolean isIsDamaged()
  {
    return isDamaged ;
  }

  /**
   * Sets the value of the isDamaged property.
   * 
   */
  public void setIsDamaged( boolean value )
  {
    this.isDamaged = value ;
  }

  /**
   * Gets the value of the isDestroyed property.
   * 
   */
  public boolean isIsDestroyed()
  {
    return isDestroyed ;
  }

  /**
   * Sets the value of the isDestroyed property.
   * 
   */
  public void setIsDestroyed( boolean value )
  {
    this.isDestroyed = value ;
  }

  /**
   * Gets the value of the isIncognito property.
   * 
   */
  public boolean isIsIncognito()
  {
    return isIncognito ;
  }

  /**
   * Sets the value of the isIncognito property.
   * 
   */
  public void setIsIncognito( boolean value )
  {
    this.isIncognito = value ;
  }

  /**
   * Gets the value of the make property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getMake()
  {
    return make ;
  }

  /**
   * Sets the value of the make property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setMake( String value )
  {
    this.make = value ;
  }

  /**
   * Gets the value of the model property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getModel()
  {
    return model ;
  }

  /**
   * Sets the value of the model property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setModel( String value )
  {
    this.model = value ;
  }

  /**
   * Gets the value of the vehicleClass property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getVehicleClass()
  {
    return vehicleClass ;
  }

  /**
   * Sets the value of the vehicleClass property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setVehicleClass( String value )
  {
    this.vehicleClass = value ;
  }

  /**
   * Gets the value of the bodyType property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getBodyType()
  {
    return bodyType ;
  }

  /**
   * Sets the value of the bodyType property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setBodyType( String value )
  {
    this.bodyType = value ;
  }

  /**
   * Gets the value of the fuelType property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getFuelType()
  {
    return fuelType ;
  }

  /**
   * Sets the value of the fuelType property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setFuelType( String value )
  {
    this.fuelType = value ;
  }

  /**
   * Gets the value of the wheelplan property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getWheelplan()
  {
    return wheelplan ;
  }

  /**
   * Sets the value of the wheelplan property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setWheelplan( String value )
  {
    this.wheelplan = value ;
  }

  /**
   * Gets the value of the cylinderCapacity property.
   * 
   */
  public Integer getCylinderCapacity()
  {
    return cylinderCapacity ;
  }

  /**
   * Sets the value of the cylinderCapacity property.
   * 
   */
  public void setCylinderCapacity( Integer value )
  {
    this.cylinderCapacity = value ;
  }

  /**
   * Gets the value of the euClassification property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getEuClassification()
  {
    return euClassification ;
  }

  /**
   * Sets the value of the euClassification property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setEuClassification( String value )
  {
    this.euClassification = value ;
  }

  /**
   * Gets the value of the createdBy property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCreatedBy()
  {
    return createdBy ;
  }

  /**
   * Sets the value of the createdBy property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setCreatedBy( String value )
  {
    this.createdBy = value ;
  }

  /**
   * Gets the value of the createdOn property.
   * 
   * @return possible object is {@link Date }
   * 
   */
  public Date getCreatedOn()
  {
    return createdOn ;
  }

  /**
   * Sets the value of the createdOn property.
   * 
   * @param value
   *          allowed object is {@link Date }
   * 
   */
  public void setCreatedOn( Date value )
  {
    this.createdOn = value ;
  }

  /**
   * Gets the value of the lastModifiedBy property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getLastUpdatedBy()
  {
    return lastUpdatedBy ;
  }

  /**
   * Sets the value of the lastModifiedBy property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setLastUpdatedBy( String value )
  {
    this.lastUpdatedBy = value ;
  }

  /**
   * Gets the value of the lastModifiedOn property.
   * 
   * @return possible object is {@link Date }
   * 
   */
  public Date getLastUpdatedOn()
  {
    return lastUpdatedOn ;
  }

  /**
   * Sets the value of the lastModifiedOn property.
   * 
   * @param value
   *          allowed object is {@link Date }
   * 
   */
  public void setLastUpdatedOn( Date value )
  {
    this.lastUpdatedOn = value ;
  }

  /**
   * Gets the value of the version property.
   * 
   */
  public int getVersion()
  {
    return version ;
  }

  /**
   * Sets the value of the version property.
   * 
   */
  public void setVersion( int value )
  {
    this.version = value ;
  }

}