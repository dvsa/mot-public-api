package uk.gov.dvsa.mot.persist.jdbc;

import java.sql.ResultSet ;
import java.text.SimpleDateFormat ;
import java.util.ArrayList ;
import java.util.List ;

import uk.gov.dvsa.mot.trade.api.InternalException ;
import uk.gov.dvsa.mot.trade.api.MotTest ;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem ;
import uk.gov.dvsa.mot.trade.api.Vehicle ;

/**
 * Helper class to map query results to vehicles.
 */
public class ResultSetToVehicleMapper {
  private static final SimpleDateFormat sdfDate = new SimpleDateFormat( "yyyy.MM.dd" ) ;
  private static final SimpleDateFormat sdfDateTime = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss" ) ;
  
  /**
   * Takes a ResultSet from a vehicle query and extracts a Vehicle object from it.
   * @param resultSet
   * @return
   */
  public static List<Vehicle> mapResultSetToVehicle( ResultSet resultSet )
  {
    List<Vehicle> lVehicle = new ArrayList<>() ;

    Vehicle vehicle ;
    List<MotTest> lMotTest = null ;
    List<RfrAndAdvisoryItem> lRfrAndAdvisoryItem = null ;

    int vehicleId = 0 ;
    long motTestId = 0 ;

    try
    {
      while ( resultSet.next() )
      {
        int currVehicleId = resultSet.getInt( 1 ) ;
        long currMotTestId = resultSet.getLong( 2 ) ;
        long currMotTestRfrMapId = resultSet.getLong( 3 ) ;

        if ( currVehicleId != vehicleId )
        {
          vehicleId = currVehicleId ;

          vehicle = new Vehicle() ;
          lMotTest = new ArrayList<>() ;
          vehicle.setMotTests( lMotTest ) ;

          lVehicle.add( vehicle ) ;

          // vehicle.setId( vehicleId );
          vehicle.setRegistration( resultSet.getString( 4 ) ) ;
          vehicle.setMake( resultSet.getString( 5 ) ) ;
          vehicle.setModel( resultSet.getString( 6 ) ) ;
          if ( resultSet.getDate( 7 ) != null )
          {
            vehicle.setFirstUsedDate( sdfDate.format( resultSet.getTimestamp( 7 ) ) ) ;
          }
          vehicle.setFuelType( resultSet.getString( 8 ) ) ;
          vehicle.setPrimaryColour( resultSet.getString( 9 ) ) ;
          // field 10 secondary colour not used
        }

        if ( currMotTestId != motTestId )
        {
          motTestId = currMotTestId ;

          MotTest motTest = new MotTest() ;
          lRfrAndAdvisoryItem = new ArrayList<>() ;
          motTest.setRfrAndComments( lRfrAndAdvisoryItem ) ;

          lMotTest.add( motTest ) ;

          // motTest.setId( currMotTestId ) ;
          // field 11 - started date not used
          if ( resultSet.getDate( 12 ) != null )
          {
            motTest.setCompletedDate( sdfDateTime.format( resultSet.getTimestamp( 12 ) ) ) ;
          }
          motTest.setTestResult( resultSet.getString( 13 ) ) ;
          if ( resultSet.getDate( 14 ) != null )
          {
            motTest.setExpiryDate( sdfDate.format( resultSet.getTimestamp( 14 ) ) ) ;
          }
          motTest.setOdometerValue( String.valueOf( resultSet.getInt( 15 ) ) ) ;
          motTest.setOdometerUnit( resultSet.getString( 16 ) ) ;
          // field 17 - odometer result type not used
          motTest.setMotTestNumber( String.valueOf( resultSet.getBigDecimal( 18 ) ) ) ;
        }

        if ( currMotTestRfrMapId != 0 )
        {
          RfrAndAdvisoryItem rfrAndAdvisoryItem = new RfrAndAdvisoryItem() ;
          rfrAndAdvisoryItem.setType( resultSet.getString( 19 ) ) ;
          rfrAndAdvisoryItem.setText( resultSet.getString( 20 ) ) ;
          lRfrAndAdvisoryItem.add( rfrAndAdvisoryItem ) ;
        }
      }

      return lVehicle ;
    }
    catch ( Exception e )
    {
      throw new InternalException( e ) ;
    }
  }
}
