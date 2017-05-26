package uk.gov.dvsa.mot.trade.api ;

import java.util.List ;

public class DisplayMotTestItem
{
  private String registration ;
  private String make_name ;
  private String model_name ;
  private String first_used_date ;
  private String fuel_type ;
  private String primary_colour ;
  private String completed_date ;
  private String test_result ;
  private String expiry_date ;
  private String odometer_value ;
  private String odometer_unit ;
  private String mot_test_number ;
  private List<RfrAndAdvisoryItem> rfrAndComments ;

  public DisplayMotTestItem()
  {
  }

  public List<RfrAndAdvisoryItem> getRfrAndComments()
  {
    return rfrAndComments ;
  }

  public DisplayMotTestItem setRfrAndComments( List<RfrAndAdvisoryItem> rfrAndComments )
  {
    this.rfrAndComments = rfrAndComments ;
    return this ;
  }

  public String getRegistration()
  {
    return registration ;
  }

  public DisplayMotTestItem setRegistration( String registration )
  {
    this.registration = registration ;
    return this ;
  }

  public String getMake_name()
  {
    return make_name ;
  }

  public DisplayMotTestItem setMake_name( String make_name )
  {
    this.make_name = make_name ;
    return this ;
  }

  public String getModel_name()
  {
    return model_name ;
  }

  public DisplayMotTestItem setModel_name( String model_name )
  {
    this.model_name = model_name ;
    return this ;
  }

  public String getFirst_used_date()
  {
    return first_used_date ;
  }

  public DisplayMotTestItem setFirst_used_date( String first_used_date )
  {
    this.first_used_date = first_used_date ;
    return this ;
  }

  public String getFuel_type()
  {
    return fuel_type ;
  }

  public DisplayMotTestItem setFuel_type( String fuel_type )
  {
    this.fuel_type = fuel_type ;
    return this ;
  }

  public String getPrimary_colour()
  {
    return primary_colour ;
  }

  public DisplayMotTestItem setPrimary_colour( String primary_colour )
  {
    this.primary_colour = primary_colour ;
    return this ;
  }

  public String getCompleted_date()
  {
    return completed_date ;
  }

  public DisplayMotTestItem setCompleted_date( String completed_date )
  {
    this.completed_date = completed_date ;
    return this ;
  }

  public String getTest_result()
  {
    return test_result ;
  }

  public DisplayMotTestItem setTest_result( String test_result )
  {
    this.test_result = test_result ;
    return this ;
  }

  public String getExpiry_date()
  {
    return expiry_date ;
  }

  public DisplayMotTestItem setExpiry_date( String expiry_date )
  {
    this.expiry_date = expiry_date ;
    return this ;
  }

  public String getOdometer_value()
  {
    return odometer_value ;
  }

  public DisplayMotTestItem setOdometer_value( String odometer_value )
  {
    this.odometer_value = odometer_value ;
    return this ;
  }

  public String getMot_test_number()
  {
    return mot_test_number ;
  }

  public DisplayMotTestItem setMot_test_number( String mot_test_number )
  {
    this.mot_test_number = mot_test_number ;
    return this ;
  }

  public String getOdometer_unit()
  {
    return odometer_unit ;
  }

  public DisplayMotTestItem setOdometer_unit( String odometer_unit )
  {
    this.odometer_unit = odometer_unit ;
    return this ;
  }
}
