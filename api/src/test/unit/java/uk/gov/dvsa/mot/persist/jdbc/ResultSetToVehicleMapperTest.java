package uk.gov.dvsa.mot.persist.jdbc ;

import static org.hamcrest.CoreMatchers.equalTo ;
import static org.hamcrest.CoreMatchers.notNullValue ;
import static org.junit.Assert.assertThat ;
import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.when ;
import static uk.gov.dvsa.mot.test.utility.Matchers.hasSize ;

import java.math.BigDecimal ;
import java.sql.Date ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.sql.Timestamp ;
import java.time.LocalDateTime ;
import java.time.ZoneId ;
import java.time.format.DateTimeFormatter ;
import java.util.List ;

import org.junit.Test ;

import uk.gov.dvsa.mot.trade.api.InternalException ;
import uk.gov.dvsa.mot.trade.api.MotTest ;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem ;
import uk.gov.dvsa.mot.trade.api.Vehicle ;

public class ResultSetToVehicleMapperTest
{
  /**
   * Checks that a vehicle is mapped out of a result set correctly.
   */
  @Test
  public void MapsVehicle() throws SQLException
  {
    ResultSet rs = CreateMockResultSetForFullPath() ;

    List<Vehicle> actual = ResultSetToVehicleMapper.mapResultSetToVehicle( rs ) ;

    assertThat( actual, notNullValue() ) ;
    assertThat( actual, hasSize( 2 ) ) ;

    // both vehicles are the same
    for ( Vehicle v : actual )
    {
      AssertVehicleIsCorrect( v ) ;
    }
  }

  @Test
  public void MapsVehicle_Minimal() throws SQLException
  {
    ResultSet rs = CreateMockResultSetForMinimalPath() ;

    List<Vehicle> actual = ResultSetToVehicleMapper.mapResultSetToVehicle( rs ) ;

    assertThat( actual, notNullValue() ) ;
    assertThat( actual, hasSize( 2 ) ) ;

    // both vehicles are the same
    for ( Vehicle v : actual )
    {
      AssertMinimalVehicleIsCorrect( v ) ;
    }
  }

  @Test( expected = InternalException.class )
  public void MapsVehicle_Throws() throws SQLException
  {
    ResultSet rs = mock( ResultSet.class ) ;
    when( rs.next() ).thenThrow( new SQLException( "" ) ) ;

    ResultSetToVehicleMapper.mapResultSetToVehicle( rs ) ;
  }

  private void AssertVehicleIsCorrect( Vehicle vehicle )
  {
    AssertMinimalVehicleIsCorrect( vehicle ) ;
    assertThat( vehicle.getFirstUsedDate(), equalTo( firstUsedDateString ) ) ;
    MotTest test = vehicle.getMotTests().get( 0 ) ;
    assertThat( test.getCompletedDate(), equalTo( motCompletionDateString ) ) ;
    assertThat( test.getExpiryDate(), equalTo( motExpiryDateString ) ) ;
    assertThat( test.getRfrAndComments(), hasSize( 1 ) ) ;
    RfrAndAdvisoryItem rfr = test.getRfrAndComments().get( 0 ) ;
    assertThat( rfr.getType(), equalTo( rfrType ) ) ;
    assertThat( rfr.getText(), equalTo( rfrText ) ) ;
  }

  private void AssertMinimalVehicleIsCorrect( Vehicle vehicle )
  {
    // note: vehicle ID is not tested because it is not set and cannot be set
    assertThat( vehicle.getRegistration(), equalTo( registration ) ) ;
    assertThat( vehicle.getMake(), equalTo( make ) ) ;
    assertThat( vehicle.getModel(), equalTo( model ) ) ;
    assertThat( vehicle.getFuelType(), equalTo( fuelType ) ) ;
    assertThat( vehicle.getPrimaryColour(), equalTo( primaryColour ) ) ;
    assertThat( vehicle.getMotTests(), hasSize( 1 ) ) ;
    MotTest test = vehicle.getMotTests().get( 0 ) ;
    assertThat( test, notNullValue() ) ;
    assertThat( test.getTestResult(), equalTo( testResult ) ) ;
    assertThat( test.getOdometerUnit(), equalTo( odometerUnit ) ) ;
    assertThat( test.getOdometerValue(), equalTo( String.valueOf( odometerReading ) ) ) ;
  }

  private final int vehicleIdIndex = 1 ;
  private final int motTestIdIndex = 2 ;
  private final int motTestRFRMapIDIndex = 3 ;
  private final int registrationIndex = 4 ;
  private final int makeIndex = 5 ;
  private final int modelIndex = 6 ;
  private final int firstUsedDateIndex = 7 ;
  private final int fuelTypeIndex = 8 ;
  private final int primaryColourIndex = 9 ;
  private final int motCompletionDateIndex = 12 ;
  private final int testResultIndex = 13 ;
  private final int motExpiryDateIndex = 14 ;
  private final int odometerReadingIndex = 15 ;
  private final int odometerUnitIndex = 16 ;
  private final int motTestNumberIndex = 18 ;
  private final int rfrTypeIndex = 19 ;
  private final int rfrTextIndex = 20 ;

  private final int vehicleId1 = 234 ; // 1
  private final int vehicleId2 = 822 ; // 1
  private final long motTestId1 = 7891234 ; // 2
  private final long motTestId2 = 7991234 ; // 2
  private final long motTestRFRMapID = 28234 ; // 3
  private final String registration = "AA01AAA" ; // 4
  private final String make = "TESLA" ; // 5
  private final String model = "INVISIBLE" ; // 6
  private final Date firstUsedDate ; // 7
  private final String firstUsedDateString ;
  private final Timestamp firstUsedTimestamp ; // 7
  private final String fuelType = "RAINBOWS" ; // 8
  private final String primaryColour = "BLACK" ; // 9
  private final Date motCompletionDate ; // 12
  private final Timestamp motCompletionTimestamp ; // 12
  private final String motCompletionDateString ; // 12
  private final String testResult = "PASS" ; // 13
  private final Date motExpiryDate ; // 14
  private final Timestamp motExpiryTimestamp ; // 14
  private final String motExpiryDateString ; // 14
  private final int odometerReading = 23992340 ; // 15
  private final String odometerUnit = "YARD" ; // 16
  private final BigDecimal motTestNumber = BigDecimal.valueOf( 89723948L ) ; // 18
  private final String rfrType = "ADVISORY" ; // 19
  private final String rfrText = "Too shiny" ; // 20

  {
    LocalDateTime firstUsedDateTime = LocalDateTime.now().minusYears( 5 ) ;
    LocalDateTime motCompletionDateTime = LocalDateTime.now() ;
    LocalDateTime motExpiryDateTime = motCompletionDateTime.plusYears( 1 ) ;

    firstUsedDate = toDate( firstUsedDateTime ) ;
    firstUsedTimestamp = toTimestamp( firstUsedDateTime ) ;
    motCompletionDate = toDate( motCompletionDateTime ) ;
    motCompletionTimestamp = toTimestamp( motCompletionDateTime ) ;
    motExpiryDate = toDate( motExpiryDateTime ) ;
    motExpiryTimestamp = toTimestamp( motExpiryDateTime ) ;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern( "yyyy.MM.dd" ) ;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern( "yyyy.MM.dd HH:mm:ss" ) ;
    firstUsedDateString = firstUsedDateTime.format( dateFormatter ) ;
    motCompletionDateString = motCompletionDateTime.format( dateTimeFormatter ) ;
    motExpiryDateString = motExpiryDateTime.format( dateFormatter ) ;
  }

  private static long toEpochMilli( LocalDateTime ldt )
  {
    return ldt.atZone( ZoneId.systemDefault() ).toInstant().toEpochMilli() ;
  }

  private static Date toDate( LocalDateTime ldt )
  {
    return new Date( toEpochMilli( ldt ) ) ;
  }

  private static Timestamp toTimestamp( LocalDateTime ldt )
  {
    return new Timestamp( toEpochMilli( ldt ) ) ;
  }

  /**
   * Create a mock result set and initialise it so that it will produce valid
   * values for every field
   * 
   * @return a ResultSet object which is a fully-configured mock
   */
  private ResultSet CreateMockResultSetForFullPath() throws SQLException
  {
    ResultSet mockResultSet = mock( ResultSet.class ) ;

    // we want it to return two rows in total
    when( mockResultSet.next() ).thenReturn( true ).thenReturn( true ).thenReturn( false ) ;

    // but each one can be the same, for simplicity, except vehicleID because
    // there's logic around that in the mapper
    when( mockResultSet.getInt( vehicleIdIndex ) ).thenReturn( vehicleId1 ).thenReturn( vehicleId2 ) ;
    when( mockResultSet.getLong( motTestIdIndex ) ).thenReturn( motTestId1 ).thenReturn( motTestId2 ) ;
    when( mockResultSet.getLong( motTestRFRMapIDIndex ) ).thenReturn( motTestRFRMapID ) ;
    when( mockResultSet.getString( registrationIndex ) ).thenReturn( registration ) ;
    when( mockResultSet.getString( makeIndex ) ).thenReturn( make ) ;
    when( mockResultSet.getString( modelIndex ) ).thenReturn( model ) ;
    when( mockResultSet.getDate( firstUsedDateIndex ) ).thenReturn( firstUsedDate ) ;
    when( mockResultSet.getTimestamp( firstUsedDateIndex ) ).thenReturn( firstUsedTimestamp ) ;
    when( mockResultSet.getString( fuelTypeIndex ) ).thenReturn( fuelType ) ;
    when( mockResultSet.getString( primaryColourIndex ) ).thenReturn( primaryColour ) ;
    when( mockResultSet.getTimestamp( motCompletionDateIndex ) ).thenReturn( motCompletionTimestamp ) ;
    when( mockResultSet.getDate( motCompletionDateIndex ) ).thenReturn( motCompletionDate ) ;
    when( mockResultSet.getString( testResultIndex ) ).thenReturn( testResult ) ;
    when( mockResultSet.getTimestamp( motExpiryDateIndex ) ).thenReturn( motExpiryTimestamp ) ;
    when( mockResultSet.getDate( motExpiryDateIndex ) ).thenReturn( motExpiryDate ) ;
    when( mockResultSet.getInt( odometerReadingIndex ) ).thenReturn( odometerReading ) ;
    when( mockResultSet.getString( odometerUnitIndex ) ).thenReturn( odometerUnit ) ;
    when( mockResultSet.getBigDecimal( motTestNumberIndex ) ).thenReturn( motTestNumber ) ;
    when( mockResultSet.getString( rfrTypeIndex ) ).thenReturn( rfrType ) ;
    when( mockResultSet.getString( rfrTextIndex ) ).thenReturn( rfrText ) ;

    return mockResultSet ;
  }

  private ResultSet CreateMockResultSetForMinimalPath() throws SQLException
  {
    ResultSet mockResultSet = mock( ResultSet.class ) ;

    // we want it to return two rows in total
    when( mockResultSet.next() ).thenReturn( true ).thenReturn( true ).thenReturn( false ) ;

    // but each one can be the same, for simplicity, except vehicleID because
    // there's logic around that in the mapper
    when( mockResultSet.getInt( vehicleIdIndex ) ).thenReturn( vehicleId1 ).thenReturn( vehicleId2 ) ;
    when( mockResultSet.getLong( motTestIdIndex ) ).thenReturn( motTestId1 ).thenReturn( motTestId2 ) ;
    when( mockResultSet.getLong( motTestRFRMapIDIndex ) ).thenReturn( 0L ) ;
    when( mockResultSet.getString( registrationIndex ) ).thenReturn( registration ) ;
    when( mockResultSet.getString( makeIndex ) ).thenReturn( make ) ;
    when( mockResultSet.getString( modelIndex ) ).thenReturn( model ) ;
    when( mockResultSet.getString( fuelTypeIndex ) ).thenReturn( fuelType ) ;
    when( mockResultSet.getString( primaryColourIndex ) ).thenReturn( primaryColour ) ;
    when( mockResultSet.getString( testResultIndex ) ).thenReturn( testResult ) ;
    when( mockResultSet.getInt( odometerReadingIndex ) ).thenReturn( odometerReading ) ;
    when( mockResultSet.getString( odometerUnitIndex ) ).thenReturn( odometerUnit ) ;
    when( mockResultSet.getBigDecimal( motTestNumberIndex ) ).thenReturn( motTestNumber ) ;
    when( mockResultSet.getString( rfrTypeIndex ) ).thenReturn( rfrType ) ;
    when( mockResultSet.getString( rfrTextIndex ) ).thenReturn( rfrText ) ;

    return mockResultSet ;
  }
}
