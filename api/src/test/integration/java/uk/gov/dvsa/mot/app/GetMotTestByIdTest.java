package uk.gov.dvsa.mot.app ;

import java.io.IOException ;
//import java.util.List ;

import org.junit.BeforeClass ;
import org.junit.Test ;

import com.amazonaws.services.lambda.runtime.Context ;

import uk.gov.dvsa.mot.app.MotServiceRequestHandler ;
//import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem ;
import uk.gov.dvsa.mot.mottest.api.MotTest ;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetMotTestByIdTest
{

  private static Integer input ;

  @BeforeClass
  public static void createInput() throws IOException
  {
    input = 42 ;
  }

  private Context createContext()
  {
    TestContext ctx = new TestContext() ;

    ctx.setFunctionName( "TradeHandler" ) ;

    return ctx ;
  }

  @Test
  public void testTradeHandler()
  {
    MotServiceRequestHandler motServiceRequestHandler = new MotServiceRequestHandler() ;
    Context ctx = createContext() ;

    MotTest output = motServiceRequestHandler.getMotTestById( input, ctx ) ;

    if ( output != null )
    {
      System.out.println( output.toString() ) ;
    }
  }
}
