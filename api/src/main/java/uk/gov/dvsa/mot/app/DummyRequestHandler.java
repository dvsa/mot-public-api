package uk.gov.dvsa.mot.app ;

import com.amazonaws.services.lambda.runtime.Context ;
import com.amazonaws.services.lambda.runtime.RequestHandler ;

public class DummyRequestHandler<I, O> implements RequestHandler<I, O>
{

  @Override
  public O handleRequest( I input, Context context )
  {
    // TODO Auto-generated method stub
    return null ;
  }
}