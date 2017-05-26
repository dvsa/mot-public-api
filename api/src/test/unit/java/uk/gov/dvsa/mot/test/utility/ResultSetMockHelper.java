package uk.gov.dvsa.mot.test.utility ;

import static org.mockito.ArgumentMatchers.anyInt ;
import static org.mockito.Mockito.doAnswer ;

import java.sql.Date ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.sql.Timestamp ;
import java.util.Calendar ;

public class ResultSetMockHelper
{

  private final static String EXPECTED_STRING = "Sample String" ;
  private final static int EXPECTED_INT = 2 ;
  private final static Boolean EXPECTED_BOOLEAN = true ;
  private final static Date EXPECTED_DATE = new Date( Calendar.getInstance().getTimeInMillis() ) ;
  private final static Timestamp EXPECTED_TIMESTAMP = new Timestamp( System.currentTimeMillis() ) ;

  // Handles generating test data when reading a result set for String, int,
  // Boolean, Date, and Timestamp
  public static void Initialize( ResultSet resultSetMock ) throws SQLException
  {
    doAnswer( invocation -> EXPECTED_STRING ).when( resultSetMock ).getString( anyInt() ) ;

    doAnswer( invocation -> EXPECTED_INT ).when( resultSetMock ).getInt( anyInt() ) ;

    doAnswer( invocation -> EXPECTED_BOOLEAN ).when( resultSetMock ).getBoolean( anyInt() ) ;

    doAnswer( invocation -> EXPECTED_DATE ).when( resultSetMock ).getDate( anyInt() ) ;

    doAnswer( invocation -> EXPECTED_TIMESTAMP ).when( resultSetMock ).getTimestamp( anyInt() ) ;
  }
}
