package uk.gov.dvsa.mot.persist.jdbc;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.dvsa.mot.persist.MotTestReadDao;
import uk.gov.dvsa.mot.persist.model.MotTest;
import uk.gov.dvsa.mot.test.utility.ResultSetMockHelper;
import uk.gov.dvsa.mot.trade.api.InternalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MotTestReadDaoTest {
    private MotTestReadDao motTestReadDao;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @Before
    public void beforeTest() throws SQLException {
        // Arrange - Set-up the result set mock with valid data for output.
        ResultSetMockHelper.initialize(mockResultSet);

        // Arrange - Setup mocks
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        // Arrange - Create object under test
        motTestReadDao = new MotTestReadDaoJdbc(mockConnection);
    }

    @After
    public void afterTest() {
        // Clean up.
        motTestReadDao = null;
        mockConnection = null;
        mockStatement = null;
        mockResultSet = null;
    }

    @Test
    public void getMotTestCurrentById_WithNoMatches_ReturnsNull() throws SQLException {
        // Arrange - Set-up result set mock
        when(mockResultSet.next()).thenReturn(false);

        // Act - Execute method
        MotTest actual = motTestReadDao.getMotTestCurrentById(anyInt());

        // Assert - Test the response
        assertNull("actual is not null", actual);
    }

    @Test
    public void getMotTestCurrentById_WithMatches_ReturnsMotTestCurrentObject() throws SQLException {
        // Arrange - Set-up result set mock
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        // Act - Execute method
        MotTest actual = motTestReadDao.getMotTestCurrentById(anyInt());

        // Assert - Test the response
        assertNotNull("actual is null", actual);
    }

    @Test
    public void getMotTestCurrentById_ThrowsSqlException_ThrowsInternalException() throws SQLException {
        // Arrange - Set-up result set mock
        String expectedExceptionMessage = "This is a test exception message.";
        when(mockStatement.executeQuery()).thenThrow(new SQLException(expectedExceptionMessage));

        // Act - Execute method
        Exception exception = null;
        try {
            motTestReadDao.getMotTestCurrentById(anyInt());
        } catch (InternalException ex) {
            exception = ex;
        }

        // Assert - Verify the exception
        assertNotNull(exception);

        // Assert - Check the exception properties
        assertThat(exception.getCause().getMessage(), equalTo(expectedExceptionMessage));
    }

    @Test
    public void getMotTestCurrentsByDateRange_WithNoMatches_ReturnsEmptyList() throws SQLException {
        // Arrange - Set-up result set mock
        when(mockResultSet.next()).thenReturn(false);

        // Act
        List<MotTest> list = motTestReadDao.getMotTestCurrentsByDateRange(new Date(), new Date());

        // Assert
        assertNotNull(list);
        assertTrue(list.size() == 0);
    }

    @Test
    public void getMotTestCurrentsByDateRange_WithMatches_ReturnsPopulatedList() throws SQLException {
        // Arrange - Set-up result set mock
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        // Act
        List<MotTest> list = motTestReadDao.getMotTestCurrentsByDateRange(new Date(), new Date());

        // Assert
        assertNotNull(list);
        assertTrue(list.size() == 1);
    }

    @Test
    public void getMotTestCurrentsByDateRange_ThrowsSqlException_ThrowsInternalException() throws SQLException {
        // Arrange - Set-up result set mock
        final String expectedExceptionMessage = "This is a test exception message.";
        when(mockStatement.executeQuery()).thenThrow(new SQLException(expectedExceptionMessage));

        InternalException exception = null;

        // Act - Execute method
        try {
            motTestReadDao.getMotTestCurrentsByDateRange(new Date(), new Date());
        } catch (InternalException ex) {
            exception = ex;
        }

        // Assert - Verify the exception
        assertNotNull(exception);

        // Assert - Check the exception properties
        assertThat(exception.getCause().getMessage(), equalTo(expectedExceptionMessage));
    }
}