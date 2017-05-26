package uk.gov.dvsa.mot.vehicle.api ;

public class ValidationError
{

  private final String fieldName ;

  private final String errorMessage ;

  public ValidationError( String fieldName, String errorMessage )
  {
    this.fieldName = fieldName ;
    this.errorMessage = errorMessage ;
  }

  public String getFieldName()
  {
    return fieldName ;
  }

  public String getErrorMessage()
  {
    return errorMessage ;
  }
}
