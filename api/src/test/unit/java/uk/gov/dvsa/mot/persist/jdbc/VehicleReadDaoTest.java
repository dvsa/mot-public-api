package uk.gov.dvsa.mot.persist.jdbc ;

import static org.hamcrest.CoreMatchers.equalTo ;
import static org.hamcrest.CoreMatchers.not ;
import static org.hamcrest.CoreMatchers.notNullValue ;
import static org.hamcrest.CoreMatchers.nullValue ;
import static org.junit.Assert.assertThat ;
import static org.mockito.ArgumentMatchers.anyString ;
import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.verify ;
import static org.mockito.Mockito.when ;
import static org.mockito.hamcrest.MockitoHamcrest.argThat ;
import static uk.gov.dvsa.mot.test.utility.Matchers.isEmpty ;

import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.util.List ;

import org.junit.After ;
import org.junit.Before ;
import org.junit.Test ;
import org.junit.runner.RunWith ;
import org.mockito.Mock ;
import org.mockito.junit.MockitoJUnitRunner ;

import com.mysql.jdbc.Connection ;
import com.mysql.jdbc.PreparedStatement ;

import uk.gov.dvsa.mot.persist.model.DvlaVehicle ;
import uk.gov.dvsa.mot.persist.model.Vehicle ;
import uk.gov.dvsa.mot.test.utility.ResultSetMockHelper ;
import uk.gov.dvsa.mot.trade.api.InternalException ;

@RunWith( MockitoJUnitRunner.class )
public class VehicleReadDaoTest
{

  private VehicleReadDaoMySQLJDBC vehicleReadReadDao ;

  @Mock
  private Connection mockConnection ;

  @Mock
  private PreparedStatement mockStatement ;

  @Mock
  private ResultSet mockResultSet ;

  @Before
  public void beforeTest() throws SQLException
  {
    // Arrange - Set-up the result set mock with valid data for output.
    ResultSetMockHelper.Initialize( mockResultSet ) ;

    // Arrange - Setup mocks
    when( mockStatement.executeQuery() ).thenReturn( mockResultSet ) ;

    // Arrange - Create object under test
    vehicleReadReadDao = new VehicleReadDaoMySQLJDBC( mockConnection ) ;
  }

  @After
  public void afterTest()
  {
    // Clean up.
    vehicleReadReadDao = null ;
    mockConnection = null ;
    mockStatement = null ;
    mockResultSet = null ;
  }

  @Test
  public void getVehicleById_WithNoMatches_ReturnsNull() throws SQLException
  {
    // Arrange - Set-up result set mock
    when( mockResultSet.next() ).thenReturn( false ) ;
    when( mockConnection.prepareStatement( anyString() ) ).thenReturn( mockStatement ) ;

    // Act - Execute method
    Vehicle actual = vehicleReadReadDao.getVehicleById( 1 ) ;

    // Assert - Test the response
    assertThat( actual, nullValue() ) ;
  }

  @Test
  public void getVehicleById_WithMatches_ReturnsMotTestCurrentObject() throws SQLException
  {
    // Arrange - Set-up result set mock
    when( mockResultSet.next() ).thenReturn( true ).thenReturn( false ) ;
    when( mockConnection.prepareStatement( anyString() ) ).thenReturn( mockStatement ) ;

    // Act - Execute method
    Vehicle actual = vehicleReadReadDao.getVehicleById( 1 ) ;

    // Assert - Test the response
    assertThat( actual, notNullValue() ) ;
  }

  @Test( expected = InternalException.class )
  public void getVehicleById_ThrowingSqlException_ThrowsInternalException() throws InternalException, SQLException
  {
    when( mockStatement.executeQuery() ).thenThrow( new SQLException( "" ) ) ;
    when( mockConnection.prepareStatement( anyString() ) ).thenReturn( mockStatement ) ;

    vehicleReadReadDao.getVehicleById( 1 ) ;
  }

  @Test
  public void getVehiclebyIdAndVersion_WithNoMatches_ReturnsNull() throws SQLException
  {
    final int vehicleId = 890321 ;
    final int version = 23000 ;

    PreparedStatement mockStatement2 = mock( PreparedStatement.class ) ;

    when( mockStatement2.executeQuery() ).thenReturn( mockResultSet ) ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetVehicleByIdAndVersion ) )
        .thenReturn( mockStatement ) ;
    when( mockConnection.prepareStatement( VehicleReadSql.queryGetVehicleHistByIdAndVersion ) )
        .thenReturn( mockStatement2 ) ;

    Vehicle actual = vehicleReadReadDao.getVehicleByIdAndVersion( vehicleId, version ) ;

    verify( mockStatement ).setInt( 1, vehicleId ) ;
    verify( mockStatement ).setInt( 2, version ) ;
    verify( mockStatement2 ).setInt( 1, vehicleId ) ;
    verify( mockStatement2 ).setInt( 2, version ) ;
    assertThat( actual, nullValue() ) ;
  }

  @Test
  public void getVehicleByIdAndVersion_NoCurrent_GoesToHistory() throws SQLException
  {
    final int vehicleId = 890321 ;
    final int version = 23000 ;

    PreparedStatement mockStatement2 = mock( PreparedStatement.class ) ;
    ResultSet resultSet2 = mock( ResultSet.class ) ;
    ResultSetMockHelper.Initialize( resultSet2 ) ;

    when( resultSet2.next() ).thenReturn( true ) ;

    when( mockStatement2.executeQuery() ).thenReturn( resultSet2 ) ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetVehicleHistByIdAndVersion ) )
        .thenReturn( mockStatement2 ) ;
    when( mockConnection
        .prepareStatement( argThat( not( equalTo( VehicleReadSql.queryGetVehicleHistByIdAndVersion ) ) ) ) )
            .thenReturn( mockStatement ) ;

    Vehicle actual = vehicleReadReadDao.getVehicleByIdAndVersion( vehicleId, version ) ;

    verify( mockStatement ).setInt( 1, vehicleId ) ;
    verify( mockStatement ).setInt( 2, version ) ;
    verify( mockStatement2 ).setInt( 1, vehicleId ) ;
    verify( mockStatement2 ).setInt( 2, version ) ;
    assertThat( actual, notNullValue() ) ;
  }

  @Test
  public void getVehiclebyIdAndVersion_WithMatches_ReturnsVehicle() throws SQLException
  {
    final int vehicleId = 890321 ;
    final int version = 23000 ;

    PreparedStatement mockStatement2 = mock( PreparedStatement.class ) ;
    ResultSet resultSet2 = mock( ResultSet.class ) ;
    ResultSetMockHelper.Initialize( resultSet2 ) ;

    when( resultSet2.next() ).thenReturn( true ) ;
    when( mockStatement2.executeQuery() ).thenReturn( resultSet2 ) ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetVehicleByIdAndVersion ) )
        .thenReturn( mockStatement2 ) ;
    when( mockConnection.prepareStatement( argThat( not( equalTo( VehicleReadSql.queryGetVehicleByIdAndVersion ) ) ) ) )
        .thenReturn( mockStatement ) ;

    Vehicle actual = vehicleReadReadDao.getVehicleByIdAndVersion( vehicleId, version ) ;

    verify( mockStatement2 ).setInt( 1, vehicleId ) ;
    verify( mockStatement2 ).setInt( 2, version ) ;
    assertThat( actual, notNullValue() ) ;
  }

  @Test( expected = InternalException.class )
  public void getVehiclebyIdAndVersion_ExecuteThrows_ThrowsInternalException() throws SQLException, InternalException
  {
    final int vehicleId = 890321 ;
    final int version = 23000 ;

    PreparedStatement mockStatement2 = mock( PreparedStatement.class ) ;
    ResultSet resultSet2 = mock( ResultSet.class ) ;
    ResultSetMockHelper.Initialize( resultSet2 ) ;

    when( mockStatement2.executeQuery() ).thenThrow( new SQLException( "" ) ) ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetVehicleByIdAndVersion ) )
        .thenReturn( mockStatement2 ) ;

    vehicleReadReadDao.getVehicleByIdAndVersion( vehicleId, version ) ;
  }

  @Test( expected = InternalException.class )
  public void getVehiclebyIdAndVersion_PrepareThrows_ThrowsInternalException() throws SQLException, InternalException
  {
    final int vehicleId = 890321 ;
    final int version = 23000 ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetVehicleByIdAndVersion ) )
        .thenThrow( new SQLException( "" ) ) ;

    vehicleReadReadDao.getVehicleByIdAndVersion( vehicleId, version ) ;
  }

  @Test( expected = InternalException.class )
  public void getVehicleByIdAndVersion_NoCurrent_GoesToHistory_HistoryThrows_ThrowsInternalException()
      throws SQLException, InternalException
  {
    final int vehicleId = 890321 ;
    final int version = 23000 ;

    PreparedStatement mockStatement2 = mock( PreparedStatement.class ) ;

    when( mockStatement2.executeQuery() ).thenThrow( new SQLException( "" ) ) ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetVehicleHistByIdAndVersion ) )
        .thenReturn( mockStatement2 ) ;
    when( mockConnection
        .prepareStatement( argThat( not( equalTo( VehicleReadSql.queryGetVehicleHistByIdAndVersion ) ) ) ) )
            .thenReturn( mockStatement ) ;

    vehicleReadReadDao.getVehicleByIdAndVersion( vehicleId, version ) ;
  }

  @Test
  public void getDvlaVehicleById_HappyPath() throws SQLException
  {
    final int dvlaVehicleId = 89047 ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetDvlaVehicleById ) ).thenReturn( mockStatement ) ;
    when( mockResultSet.next() ).thenReturn( true ) ;

    DvlaVehicle actual = vehicleReadReadDao.getDvlaVehicleById( dvlaVehicleId ) ;

    assertThat( actual, notNullValue() ) ;
  }

  @Test
  public void getDvlaVehicleById_NoVehicle_ReturnsNull() throws SQLException
  {
    final int dvlaVehicleId = 89047 ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetDvlaVehicleById ) ).thenReturn( mockStatement ) ;

    DvlaVehicle actual = vehicleReadReadDao.getDvlaVehicleById( dvlaVehicleId ) ;

    assertThat( actual, nullValue() ) ;
  }

  @Test( expected = InternalException.class )
  public void getDvlaVehicleById_DbThrows_ThrowsInternalException() throws SQLException, InternalException
  {
    final int dvlaVehicleId = 89047 ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetDvlaVehicleById ) )
        .thenThrow( new SQLException( "" ) ) ;

    vehicleReadReadDao.getDvlaVehicleById( dvlaVehicleId ) ;
  }

  @Test
  public void getVehiclesById_NoVehicles_ReturnsEmptyList() throws SQLException
  {
    final int startId = 1 ;
    final int endId = 10 ;

    when( mockConnection.prepareStatement( VehicleReadSql.queryGetVehiclesById ) ).thenReturn( mockStatement ) ;

    List<Vehicle> actual = vehicleReadReadDao.getVehiclesById( startId, endId ) ;

    assertThat( actual, notNullValue() ) ;
    assertThat( actual, isEmpty() ) ;
  }

}