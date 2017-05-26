package uk.gov.dvsa.mot.trade.api ;

public class InternalException extends RuntimeException
{
  private static final long serialVersionUID = 1L ;
  
  public InternalException( Throwable e )
  {
    super( e ) ;
  }
  
  public InternalException( String message )
  {
    super( message ) ;
  }
}
