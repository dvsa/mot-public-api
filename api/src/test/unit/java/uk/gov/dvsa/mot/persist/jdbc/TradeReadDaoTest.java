package uk.gov.dvsa.mot.persist.jdbc;

import org.junit.Before;
import org.mockito.Mock;

import uk.gov.dvsa.mot.persist.ConnectionFactory;
import uk.gov.dvsa.mot.persist.TradeReadDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class TradeReadDaoTest {
    ConnectionFactory connectionFactory;

    @Mock
    Connection connectionMock;

    @Mock
    PreparedStatement preparedStatementMock;

    @Mock
    ResultSet resultSetMock;

    TradeReadDao tradeReadDao;

    @Before
    public void setUp() {

        connectionFactory = () -> connectionMock;
        tradeReadDao = new TradeReadDaoJdbc();
    }

    //@Test(expected = InternalException.class)
    public void getVehiclesMotTestsByRegistrationAndMake_TurnsSqlExceptionToInternalException() throws SQLException {

        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException(""));

        tradeReadDao.getVehiclesMotTestsByRegistrationAndMake("Reg", "make");
    }

    //@Test
    public void getVehiclesMotTestsByRegistrationAndMake_SetsParametersProperly() throws SQLException {

        final String registration = "AA01AAA";
        final String make = "TESLA";

        when(connectionMock.prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_REGISTRATION_AND_MAKE))
                .thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        tradeReadDao.getVehiclesMotTestsByRegistrationAndMake(registration, make);

        verify(preparedStatementMock).setObject(1, registration);
        verify(preparedStatementMock).setObject(2, "%" + make + "%");
        verify(preparedStatementMock).setObject(3, registration);
        verify(preparedStatementMock).setObject(4, "%" + make + "%");
    }

    //@Test(expected = InternalException.class)
    public void getVehiclesMotTestsByDateRange_TurnsSqlExceptionToInternalException() throws SQLException {

        final Date startDate = new Date();
        final Date endDate = new Date();

        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException(""));

        tradeReadDao.getVehiclesMotTestsByDateRange(startDate, endDate);
    }

    //@Test
    public void getVehiclesMotTestsByDateRange_SetsTimestampsCorrectly() throws SQLException {

        final LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 7, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1);

        final Date startDate = Date.from(startDateTime.toInstant(ZoneOffset.UTC));
        final Date endDate = Date.from(endDateTime.toInstant(ZoneOffset.UTC));

        final Timestamp startTimestamp = new Timestamp(startDate.getTime());
        final Timestamp endTimestamp = new Timestamp(endDate.getTime());

        //System.out.print(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_DATE_RANGE);

        when(connectionMock.prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_DATE_RANGE))
                .thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        tradeReadDao.getVehiclesMotTestsByDateRange(startDate, endDate);

        verify(preparedStatementMock).setObject(1, startTimestamp);
        verify(preparedStatementMock).setObject(2, endTimestamp);
        verify(preparedStatementMock).setObject(3, startTimestamp);
        verify(preparedStatementMock).setObject(4, endTimestamp);
        verify(preparedStatementMock).setObject(5, endTimestamp);
        verify(preparedStatementMock).setObject(6, startTimestamp);
        verify(preparedStatementMock).setObject(7, endTimestamp);
        verify(preparedStatementMock).setObject(8, startTimestamp);
        verify(preparedStatementMock).setObject(9, endTimestamp);
        verify(preparedStatementMock).setObject(10, startTimestamp);
        verify(preparedStatementMock).setObject(11, endTimestamp);
        verify(preparedStatementMock).setObject(12, endTimestamp);
    }

    //@Test(expected = InternalException.class)
    public void getVehiclesMotTestsByRange_ConvertsSqlExceptionToInternalException() throws SQLException {

        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException(""));

        tradeReadDao.getVehiclesMotTestsByRange(0, 1000);
    }

    //@Test
    public void getVehiclesMotTestsByRange_SetsRangeParametersCorrectly() throws SQLException {

        final int startVehicleId = 23;
        final int endVehicleId = 2334;

        when(connectionMock.prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_RANGE))
                .thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        tradeReadDao.getVehiclesMotTestsByRange(startVehicleId, endVehicleId);

        verify(preparedStatementMock).setObject(1, startVehicleId);
        verify(preparedStatementMock).setObject(2, endVehicleId);
        verify(preparedStatementMock).setObject(3, startVehicleId);
        verify(preparedStatementMock).setObject(4, endVehicleId);
    }

    //@Test
    public void getVehiclesMotTestsByVehicleId_SetsParameters() throws SQLException {

        final int vehicleId = 23;

        when(connectionMock.prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_VEHICLE_ID))
                .thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        tradeReadDao.getVehiclesMotTestsByVehicleId(vehicleId);

        verify(preparedStatementMock).setObject(1, vehicleId);
        verify(preparedStatementMock).setObject(2, vehicleId);
    }

    //@Test(expected = InternalException.class)
    public void getVehiclesMotTestsByVehicleId_ConvertsSqlException() throws SQLException {

        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException(""));

        tradeReadDao.getVehiclesMotTestsByVehicleId(4);
    }

    //@Test
    public void getVehiclesMotTestsByMotTestNumber_SetsParameters() throws SQLException {

        final long testNumber = 78923;

        when(connectionMock.prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_MOT_TEST_NUMBER))
                .thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        tradeReadDao.getVehiclesMotTestsByMotTestNumber(testNumber);

        verify(preparedStatementMock).setObject(1, testNumber);
        verify(preparedStatementMock).setObject(2, testNumber);
    }

    //@Test(expected = InternalException.class)
    public void getVehiclesMotTestsByMotTestNumber_ConvertsSqlException() throws SQLException {

        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException(""));

        tradeReadDao.getVehiclesMotTestsByMotTestNumber(4);
    }
}