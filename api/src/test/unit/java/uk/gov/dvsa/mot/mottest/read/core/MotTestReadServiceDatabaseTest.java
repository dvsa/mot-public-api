package uk.gov.dvsa.mot.mottest.read.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.persist.Database;
import uk.gov.dvsa.mot.persist.jdbc.MotTestReadDaoJdbc;
import uk.gov.dvsa.mot.persist.model.MotTestStatus;
import uk.gov.dvsa.mot.persist.model.MotTestType;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MotTestReadServiceDatabaseTest {

    MotTestReadServiceDatabase motTestReadServiceDatabase;

    @Mock
    Database databaseMock;

    @Mock
    MotTestReadDaoJdbc motTestReadDaoMock;

    @Before
    public void beforeTest() throws SQLException {
        // Arrange - Set-up the mocks
        when(databaseMock.getMotTestReadDao()).thenReturn(motTestReadDaoMock);

        // Arrange - Create class under test
        motTestReadServiceDatabase = new MotTestReadServiceDatabase(databaseMock);
    }

    @After
    public void afterTest() {

        motTestReadServiceDatabase = null;
    }

    private List<uk.gov.dvsa.mot.persist.model.MotTest> getMotTestList() {
        // Arrange - Set-up mocks
        uk.gov.dvsa.mot.persist.model.MotTest motTest = mock(uk.gov.dvsa.mot.persist.model.MotTest.class,
                RETURNS_DEEP_STUBS);
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = new ArrayList<>(1);
        result.add(motTest);

        return result;
    }

    // getMotTestById Tests
    // ----------------------
    @Test
    public void getMotTestById_WithInvalidId_ReturnsNull() {

        final int motTestId = 2342345;
        // Arrange - Set-up mock
        when(motTestReadDaoMock.getMotTestById(motTestId)).thenReturn(null);

        // Act
        MotTest actual = motTestReadServiceDatabase.getMotTestById(motTestId);

        // Assert - Check the response
        assertNull(actual);
    }

    @Test
    public void getMotTestById_WithMatches_ReturnsMotTest() {
        // Arrange - Set-up mocks
        final int motTestId = 18923;
        uk.gov.dvsa.mot.persist.model.MotTest motTest = mock(uk.gov.dvsa.mot.persist.model.MotTest.class,
                RETURNS_DEEP_STUBS);
        when(motTestReadDaoMock.getMotTestById(motTestId)).thenReturn(motTest);

        // Act
        MotTest actual = motTestReadServiceDatabase.getMotTestById(motTestId);

        // Assert - Check the response and mapping
        assertNotNull(actual);
    }

    // getMotTestByNumber Tests
    // ------------------------
    @Test
    public void getMotTestByNumber_WithNoMatches_ReturnsNull() {

        final int testNumber = 781234;
        // Arrange - Set-up mock
        when(motTestReadDaoMock.getMotTestByNumber(testNumber)).thenReturn(null);

        // Act
        MotTest actual = motTestReadServiceDatabase.getMotTestByNumber(testNumber);

        // Assert - Check the response
        assertNull(actual);
    }

    @Test
    public void getMotTestByNumber_WithMatches_ReturnsMotTest() {
        // Arrange - Set-up mocks
        final int motTestNumber = 897123;
        MotTestStatus motTestStatus = new MotTestStatus();
        motTestStatus.setName("Status name");

        MotTestType motTestType = new MotTestType();
        motTestType.setDescription("Test Type Description");

        uk.gov.dvsa.mot.persist.model.MotTest motTest = new uk.gov.dvsa.mot.persist.model.MotTest();
        motTest.setMotTestType(motTestType);
        motTest.setMotTestStatus(motTestStatus);
        motTest.setMotTestCurrentRfrMaps(Arrays.asList());

        when(motTestReadDaoMock.getMotTestByNumber(motTestNumber)).thenReturn(motTest);

        // Act
        MotTest actual = motTestReadServiceDatabase.getMotTestByNumber(motTestNumber);

        // Assert - Check the response and mapping
        assertThat(actual, notNullValue());
        assertThat(actual, instanceOf(uk.gov.dvsa.mot.mottest.api.MotTest.class));
    }

    // getMotTestsByVehicleId Tests
    // -------------------------
    @Test
    public void getMotTestsByVehicleId_WithNoMatches_ReturnsEmptyList() {
        // Arrange - Set-up mock
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = new ArrayList<>();
        when(motTestReadDaoMock.getMotTestsByVehicleId(anyInt())).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByVehicleId(anyInt());

        // Assert - Check the response
        assertNotNull(actual);
        assertThat(actual, instanceOf(List.class));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getMotTestsByVehicleId_WithNullList_ReturnsEmptyList() {
        // Arrange - Set-up mock
        when(motTestReadDaoMock.getMotTestsByVehicleId(anyInt())).thenReturn(null);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByVehicleId(anyInt());

        // Assert - Check the response
        assertNotNull(actual);
        assertThat(actual, instanceOf(List.class));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getMotTestsByVehicleId_WithMatches_ReturnsMotTestList() {
        // Arrange - Set-up mocks
        when(motTestReadDaoMock.getMotTestsByVehicleId(anyInt())).thenReturn(getMotTestList());

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByVehicleId(anyInt());

        // Assert - Check the response and mapping
        assertNotNull(actual);
        assertTrue(actual.size() == 1);

        MotTest actualItem = actual.get(0);
        assertNotNull(actualItem);
    }

    // getMotTestsByDateRange Tests
    // -------------------------------
    @Test
    public void getMotTestsByDateRange_WithNoMatches_ReturnsEmptyList() {
        // Arrange - Set-up mock
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = new ArrayList<>();
        when(motTestReadDaoMock.getMotTestsByDateRange(any(Date.class), any(Date.class))).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByDateRange(new Date(), new Date());

        // Assert - Check the response
        assertNotNull(actual);
    }

    @Test
    public void getMotTestsByDateRange_WithMatches_ReturnsMotTest() {
        // Arrange - Set-up mocks
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = getMotTestList();
        when(motTestReadDaoMock.getMotTestsByDateRange(any(Date.class), any(Date.class))).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByDateRange(new Date(), new Date());

        // Assert - Check the response and mapping
        assertNotNull(actual);
        assertTrue(actual.size() == 1);

        MotTest actualItem = actual.get(0);
        assertNotNull(actualItem);
    }

    // getMotTestsByPage Tests
    // --------------------------
    @Test
    public void getMotTestsByPage_WithNoMatches_ReturnsEmptyList() {
        // Arrange - Set-up mock
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = new ArrayList<>();
        when(motTestReadDaoMock.getMotTestCurrentsByPage(any(Long.class), any(Long.class))).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByPage(1L, 1L);

        // Assert - Check the response
        assertNotNull(actual);
    }

    @Test
    public void getMotTestsByPage_WithMatches_ReturnsMotTestList() {
        // Arrange - Set-up mocks
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = getMotTestList();

        when(motTestReadDaoMock.getMotTestCurrentsByPage(any(Long.class), any(Long.class))).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByPage(1L, 1L);

        // Assert - Check the response and mapping
        assertNotNull(actual);
        assertTrue(actual.size() == 1);

        MotTest actualItem = actual.get(0);
        assertNotNull(actualItem);
    }

    @Test
    public void getMotTestsByPage_WithNullParameters_ReturnsMotTestList() {
        // Arrange - Set-up mocks
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = getMotTestList();
        when(motTestReadDaoMock.getMotTestCurrentsByPage(any(Long.class), any(Long.class))).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByPage(null, null);

        // Assert - Check the response and mapping
        assertNotNull(actual);
        assertTrue(actual.size() == 1);

        MotTest actualItem = actual.get(0);
        assertNotNull(actualItem);
    }

    // getMotTestsByDatePage Tests
    // ----------------------------

    @Test
    public void getMotTestsByDatePage_WithNoMatches_ReturnsEmptyList() {
        // Arrange - Set-up mock
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = new ArrayList<>();
        when(motTestReadDaoMock.getMotTestsByDateRange(any(Date.class), any(Date.class))).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByDatePage(new Date(), 1);

        // Assert - Check the response
        assertNotNull(actual);
    }

    @Test
    public void getMotTestsByDatePage_WithMatches_ReturnsMotTestList() {
        // Arrange - Set-up mocks
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = getMotTestList();
        when(motTestReadDaoMock.getMotTestsByDateRange(any(Date.class), any(Date.class))).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByDatePage(new Date(), 1);

        // Assert - Check the response and mapping
        assertNotNull(actual);
        assertTrue(actual.size() == 1);

        MotTest actualItem = actual.get(0);
        assertNotNull(actualItem);
    }

    @Test
    public void getMotTestsByDatePage_WithNullParameters_ReturnsMotTestList() {
        // Arrange - Set-up mocks
        List<uk.gov.dvsa.mot.persist.model.MotTest> result = getMotTestList();
        when(motTestReadDaoMock.getMotTestsByDateRange(any(Date.class), any(Date.class))).thenReturn(result);

        // Act
        List<MotTest> actual = motTestReadServiceDatabase.getMotTestsByDatePage(null, null);

        // Assert - Check the response and mapping
        assertNotNull(actual);
        assertThat(actual.size(), equalTo(1));

        MotTest actualItem = actual.get(0);

        assertThat(actualItem, notNullValue());
        assertThat(actualItem, instanceOf(uk.gov.dvsa.mot.mottest.api.MotTest.class));
    }

    @Test
    public void getLatestMotTestPassByVehicle_ValidVehicle_OnlyFailedTests_ReturnsNull() {

        final int vehicleId = 78923;
        final Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        MotTestStatus failedStatus = new MotTestStatus();
        failedStatus.setName("FAILED");

        uk.gov.dvsa.mot.persist.model.MotTest failedTest1 = new uk.gov.dvsa.mot.persist.model.MotTest();
        failedTest1.setMotTestStatus(failedStatus);
        uk.gov.dvsa.mot.persist.model.MotTest failedTest2 = new uk.gov.dvsa.mot.persist.model.MotTest();
        failedTest2.setMotTestStatus(failedStatus);

        when(motTestReadDaoMock.getMotTestsByVehicleId(vehicleId))
                .thenReturn(Arrays.asList(failedTest1, failedTest2));

        MotTest actual = motTestReadServiceDatabase.getLatestMotTestPassByVehicle(vehicle);
        assertThat(actual, nullValue());
    }

    @Test
    public void getLatestMotTestPassByVehicle_NullTestList_ReturnsNull() {

        final int vehicleId = 78923;
        final Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        when(motTestReadDaoMock.getMotTestsByVehicleId(vehicleId)).thenReturn(null);

        MotTest actual = motTestReadServiceDatabase.getLatestMotTestPassByVehicle(vehicle);
        assertThat(actual, nullValue());
    }

    @Test
    public void getLatestMotTestPassByVehicle_ValidVehicle_MixedPassedFailedTests_ReturnsPassedTest() {

        final int vehicleId = 78923;
        final Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        MotTestType testType = new MotTestType();
        testType.setDescription("Test Type");

        MotTestStatus failedStatus = new MotTestStatus();
        failedStatus.setName("FAILED");

        MotTestStatus passedStatus = new MotTestStatus();
        passedStatus.setName("PASSED");

        uk.gov.dvsa.mot.persist.model.MotTest failedTest = new uk.gov.dvsa.mot.persist.model.MotTest();
        failedTest.setMotTestStatus(failedStatus);
        failedTest.setNumber(BigDecimal.valueOf(12351352));
        failedTest.setMotTestType(testType);
        uk.gov.dvsa.mot.persist.model.MotTest passedTest = new uk.gov.dvsa.mot.persist.model.MotTest();
        passedTest.setMotTestStatus(passedStatus);
        passedTest.setNumber(BigDecimal.valueOf(8348235));
        passedTest.setMotTestType(testType);
        passedTest.setMotTestCurrentRfrMaps(Arrays.asList());

        when(motTestReadDaoMock.getMotTestsByVehicleId(vehicleId))
                .thenReturn(Arrays.asList(failedTest, passedTest));

        MotTest actual = motTestReadServiceDatabase.getLatestMotTestPassByVehicle(vehicle);
        assertThat(actual, notNullValue());
        assertThat(actual.getNumber(), equalTo((long) passedTest.getNumber().intValue()));
    }

}