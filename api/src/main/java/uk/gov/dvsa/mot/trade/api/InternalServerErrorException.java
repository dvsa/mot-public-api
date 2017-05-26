package uk.gov.dvsa.mot.trade.api ;

public class InternalServerErrorException extends TradeException
{
  private static final long serialVersionUID = 1L ;

  public InternalServerErrorException( String message, String awsRequestId )
  {
    super( "Internal Server Error", 500, message, awsRequestId ) ;
  }

  public InternalServerErrorException( Throwable t, String awsRequestId )
  {
    super( "Internal Server Error", 500, "Internal Server Error", awsRequestId ) ;
  }
}
