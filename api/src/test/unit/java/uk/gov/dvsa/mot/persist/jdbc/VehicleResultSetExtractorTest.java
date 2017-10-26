package uk.gov.dvsa.mot.persist.jdbc;

import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.InternalException;
import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static uk.gov.dvsa.mot.test.utility.Matchers.hasSize;

public class VehicleResultSetExtractorTest {

    private static final String VEHICLE_ID = "vehicle_id";
    private static final String MOT_TEST_ID = "mot_test_id";
    private static final String RFR_MAP_ID = "rfrmap_id";
    private static final String REGISTRATION = "registration";
    private static final String MAKE = "make_name";
    private static final String MODEL = "model_name";
    private static final String FIRST_USED_DATE = "first_used_date";
    private static final String FUEL_TYPE = "fuel_type";
    private static final String PRIMARY_COLOUR = "primary_colour";
    private static final String SECONDARY_COLOUR = "secondary_colour";
    private static final String MOT_START_DATE = "started_date";
    private static final String MOT_COMPLETED_DATE = "mot_test_completed_date";
    private static final String MOT_TEST_RESULT = "test_result";
    private static final String MOT_EXPIRY_DATE = "expiry_date";
    private static final String ODOMETER_VALUE = "odometer_value";
    private static final String ODOMETER_UNIT = "odometer_unit";
    private static final String ODOMETER_RESULT_TYPE = "odometer_result_type";
    private static final String MOT_TEST_NUMBER = "mot_test_number";
    private static final String RFR_TYPE = "rfr_type";
    private static final String RFR_TEXT = "rfr_and_comments";
    private static final String RFR_DANGEROUS = "rfr_dangerous";

    private final int vehicleId1 = 234; // 1
    private final int vehicleId2 = 822; // 1
    private final long motTestId1 = 7891234; // 2
    private final long motTestId2 = 7991234; // 2
    private final long motTestRfrMapId = 28234; // 3
    private final String registration = "AA01AAA"; // 4
    private final String make = "TESLA"; // 5
    private final String model = "INVISIBLE"; // 6
    private final Date firstUsedDate; // 7
    private final String firstUsedDateString;
    private final Timestamp firstUsedTimestamp; // 7
    private final String fuelType = "RAINBOWS"; // 8
    private final String primaryColour = "BLACK"; // 9
    private final Date motCompletionDate; // 12
    private final Timestamp motCompletionTimestamp; // 12
    private final String motCompletionDateString; // 12
    private final String testResult = "PASS"; // 13
    private final Date motExpiryDate; // 14
    private final Timestamp motExpiryTimestamp; // 14
    private final String motExpiryDateString; // 14
    private final int odometerReading = 23992340; // 15
    private final String odometerUnit = "YARD"; // 16
    private final BigDecimal motTestNumber = BigDecimal.valueOf(89723948L); // 18
    private final String rfrType = "ADVISORY"; // 19
    private final String rfrText = "Too shiny"; // 20
    private final boolean rfrDangerous = true; // 21

    {
        LocalDateTime firstUsedDateTime = LocalDateTime.now().minusYears(5);
        LocalDateTime motCompletionDateTime = LocalDateTime.now();
        LocalDateTime motExpiryDateTime = motCompletionDateTime.plusYears(1);

        firstUsedDate = toDate(firstUsedDateTime);
        firstUsedTimestamp = toTimestamp(firstUsedDateTime);
        motCompletionDate = toDate(motCompletionDateTime);
        motCompletionTimestamp = toTimestamp(motCompletionDateTime);
        motExpiryDate = toDate(motExpiryDateTime);
        motExpiryTimestamp = toTimestamp(motExpiryDateTime);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        firstUsedDateString = firstUsedDateTime.format(dateFormatter);
        motCompletionDateString = motCompletionDateTime.format(dateTimeFormatter);
        motExpiryDateString = motExpiryDateTime.format(dateFormatter);
    }

    private static long toEpochMilli(LocalDateTime ldt) {

        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static Date toDate(LocalDateTime ldt) {

        return new Date(toEpochMilli(ldt));
    }

    private static Timestamp toTimestamp(LocalDateTime ldt) {

        return new Timestamp(toEpochMilli(ldt));
    }

    /**
     * Checks that a vehicle is mapped out of a result set correctly.
     */
    @Test
    public void mapsVehicle() throws SQLException {

        ResultSet rs = createMockResultSetForFullPath();

        List<Vehicle> actual = new VehicleResultSetExtractor().extractData(rs);

        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(2));

        // both vehicles are the same
        for (Vehicle v : actual) {
            assertVehicleIsCorrect(v);
        }
    }

    @Test
    public void mapsVehicle_Minimal() throws SQLException {

        ResultSet rs = createMockResultSetForMinimalPath();

        List<Vehicle> actual = new VehicleResultSetExtractor().extractData(rs);

        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(2));

        // both vehicles are the same
        for (Vehicle v : actual) {
            assertMinimalVehicleIsCorrect(v);
        }
    }

    @Test(expected = InternalException.class)
    public void mapsVehicle_Throws_SqlException() throws SQLException {

        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenThrow(new SQLException(""));

        new VehicleResultSetExtractor().extractData(rs);
    }

    private void assertVehicleIsCorrect(Vehicle vehicle) {

        assertMinimalVehicleIsCorrect(vehicle);
        assertThat(vehicle.getFirstUsedDate(), equalTo(firstUsedDateString));
        MotTest test = vehicle.getMotTests().get(0);
        assertThat(test.getCompletedDate(), equalTo(motCompletionDateString));
        assertThat(test.getExpiryDate(), equalTo(motExpiryDateString));
        assertThat(test.getRfrAndComments(), hasSize(1));
        RfrAndAdvisoryItem rfr = test.getRfrAndComments().get(0);
        assertThat(rfr.getType(), equalTo(rfrType));
        assertThat(rfr.getText(), equalTo(rfrText));
        assertThat(rfr.isDangerous(), equalTo(rfrDangerous));
    }

    private void assertMinimalVehicleIsCorrect(Vehicle vehicle) {
        // note: vehicle ID is not tested because it is not set and cannot be set
        assertThat(vehicle.getRegistration(), equalTo(registration));
        assertThat(vehicle.getMake(), equalTo(make));
        assertThat(vehicle.getModel(), equalTo(model));
        assertThat(vehicle.getFuelType(), equalTo(fuelType));
        assertThat(vehicle.getPrimaryColour(), equalTo(primaryColour));
        assertThat(vehicle.getMotTests(), hasSize(1));
        MotTest test = vehicle.getMotTests().get(0);
        assertThat(test, notNullValue());
        assertThat(test.getTestResult(), equalTo(testResult));
        assertThat(test.getOdometerUnit(), equalTo(odometerUnit));
        assertThat(test.getOdometerValue(), equalTo(String.valueOf(odometerReading)));
    }

    /**
     * Create a mock result set and initialise it so that it will produce valid
     * values for every field
     *
     * @return a ResultSet object which is a fully-configured mock
     */
    private ResultSet createMockResultSetForFullPath() throws SQLException {

        ResultSet mockResultSet = mock(ResultSet.class);

        // we want it to return two rows in total
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        // but each one can be the same, for simplicity, except vehicleID because
        // there's logic around that in the mapper
        when(mockResultSet.getInt(VEHICLE_ID)).thenReturn(vehicleId1).thenReturn(vehicleId2);
        when(mockResultSet.getLong(MOT_TEST_ID)).thenReturn(motTestId1).thenReturn(motTestId2);
        when(mockResultSet.getLong(RFR_MAP_ID)).thenReturn(motTestRfrMapId);
        when(mockResultSet.getString(REGISTRATION)).thenReturn(registration);
        when(mockResultSet.getString(MAKE)).thenReturn(make);
        when(mockResultSet.getString(MODEL)).thenReturn(model);
        when(mockResultSet.getDate(FIRST_USED_DATE)).thenReturn(firstUsedDate);
        when(mockResultSet.getTimestamp(FIRST_USED_DATE)).thenReturn(firstUsedTimestamp);
        when(mockResultSet.getString(FUEL_TYPE)).thenReturn(fuelType);
        when(mockResultSet.getString(PRIMARY_COLOUR)).thenReturn(primaryColour);
        when(mockResultSet.getTimestamp(MOT_COMPLETED_DATE)).thenReturn(motCompletionTimestamp);
        when(mockResultSet.getDate(MOT_COMPLETED_DATE)).thenReturn(motCompletionDate);
        when(mockResultSet.getString(MOT_TEST_RESULT)).thenReturn(testResult);
        when(mockResultSet.getTimestamp(MOT_EXPIRY_DATE)).thenReturn(motExpiryTimestamp);
        when(mockResultSet.getDate(MOT_EXPIRY_DATE)).thenReturn(motExpiryDate);
        when(mockResultSet.getInt(ODOMETER_VALUE)).thenReturn(odometerReading);
        when(mockResultSet.getString(ODOMETER_UNIT)).thenReturn(odometerUnit);
        when(mockResultSet.getBigDecimal(MOT_TEST_NUMBER)).thenReturn(motTestNumber);
        when(mockResultSet.getString(RFR_TYPE)).thenReturn(rfrType);
        when(mockResultSet.getString(RFR_TEXT)).thenReturn(rfrText);
        when(mockResultSet.getBoolean(RFR_DANGEROUS)).thenReturn(rfrDangerous);

        return mockResultSet;
    }

    private ResultSet createMockResultSetForMinimalPath() throws SQLException {

        ResultSet mockResultSet = mock(ResultSet.class);

        // we want it to return two rows in total
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        // but each one can be the same, for simplicity, except vehicleID because
        // there's logic around that in the mapper
        when(mockResultSet.getInt(VEHICLE_ID)).thenReturn(vehicleId1).thenReturn(vehicleId2);
        when(mockResultSet.getLong(MOT_TEST_ID)).thenReturn(motTestId1).thenReturn(motTestId2);
        when(mockResultSet.getLong(RFR_MAP_ID)).thenReturn(0L);
        when(mockResultSet.getString(REGISTRATION)).thenReturn(registration);
        when(mockResultSet.getString(MAKE)).thenReturn(make);
        when(mockResultSet.getString(MODEL)).thenReturn(model);
        when(mockResultSet.getString(FUEL_TYPE)).thenReturn(fuelType);
        when(mockResultSet.getString(PRIMARY_COLOUR)).thenReturn(primaryColour);
        when(mockResultSet.getString(MOT_TEST_RESULT)).thenReturn(testResult);
        when(mockResultSet.getInt(ODOMETER_VALUE)).thenReturn(odometerReading);
        when(mockResultSet.getString(ODOMETER_UNIT)).thenReturn(odometerUnit);
        when(mockResultSet.getBigDecimal(MOT_TEST_NUMBER)).thenReturn(motTestNumber);
        when(mockResultSet.getString(RFR_TYPE)).thenReturn(rfrType);
        when(mockResultSet.getString(RFR_TEXT)).thenReturn(rfrText);
        when(mockResultSet.getBoolean(RFR_DANGEROUS)).thenReturn(rfrDangerous);

        return mockResultSet;
    }
}
