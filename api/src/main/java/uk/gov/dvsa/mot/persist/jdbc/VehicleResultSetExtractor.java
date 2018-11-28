package uk.gov.dvsa.mot.persist.jdbc;

import uk.gov.dvsa.mot.persist.jdbc.util.ResultSetExtractor;
import uk.gov.dvsa.mot.trade.api.InternalException;
import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleResultSetExtractor implements ResultSetExtractor<List<Vehicle>> {
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private static final Map<String, String> ODOMETER_RESULT_TYPE_MAP;
    
    static {
        ODOMETER_RESULT_TYPE_MAP = new HashMap<>();
        ODOMETER_RESULT_TYPE_MAP.put("OK", "READ");
        ODOMETER_RESULT_TYPE_MAP.put("NOT_READ", "UNREADABLE");
        ODOMETER_RESULT_TYPE_MAP.put("NO_METER", "NO_ODOMETER");
    }

    private static final String VEHICLE_ID = "vehicle_id";
    private static final String MOT_TEST_ID = "mot_test_id";
    private static final String RFR_MAP_ID = "rfrmap_id";
    private static final String REGISTRATION = "registration";
    private static final String MAKE = "make_name";
    private static final String MODEL = "model_name";
    private static final String FIRST_USED_DATE = "first_used_date";
    private static final String FUEL_TYPE = "fuel_type";
    private static final String MANUFACTURE_DATE = "manufacture_date";
    private static final String REGISTRATION_DATE = "registration_date";
    private static final String CYLINDER_CAPACITY = "cylinder_capacity";
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
    private static final String RFR_DEFICIENCY_CODE = "rfr_deficiency_code";
    private static final String RFR_DEFICIENCY_DESCRIPTION = "rfr_deficiency_description";

    @Override
    public List<Vehicle> extractData(ResultSet rs) throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();

        Vehicle vehicle;
        List<MotTest> motTests = null;
        List<RfrAndAdvisoryItem> rfrAndAdvisoryItems = null;

        int vehicleId = 0;
        long motTestId = 0;

        try {
            while (rs.next()) {
                int currVehicleId = rs.getInt(VEHICLE_ID);
                long currMotTestId = rs.getLong(MOT_TEST_ID);
                long currMotTestRfrMapId = rs.getLong(RFR_MAP_ID);

                if (currVehicleId != vehicleId) {
                    vehicleId = currVehicleId;

                    vehicle = new Vehicle();
                    motTests = new ArrayList<>();
                    vehicle.setMotTests(motTests);

                    vehicles.add(vehicle);

                    vehicle.setRegistration(rs.getString(REGISTRATION));
                    vehicle.setMake(rs.getString(MAKE));
                    vehicle.setModel(rs.getString(MODEL));
                    if (rs.getDate(FIRST_USED_DATE) != null) {
                        vehicle.setFirstUsedDate(SDF_DATE.format(rs.getTimestamp(FIRST_USED_DATE)));
                    }
                    vehicle.setFuelType(rs.getString(FUEL_TYPE));
                    if (rs.getDate(MANUFACTURE_DATE) != null) {
                        vehicle.setManufactureDate(SDF_DATE.format(rs.getDate(MANUFACTURE_DATE)));
                    }
                    if (rs.getDate(REGISTRATION_DATE) != null) {
                        vehicle.setRegistrationDate(SDF_DATE.format(rs.getDate(REGISTRATION_DATE)));
                    }
                    if (rs.getInt(CYLINDER_CAPACITY) != 0) {
                        vehicle.setCylinderCapacity(rs.getInt(CYLINDER_CAPACITY));
                    }
                    vehicle.setPrimaryColour(rs.getString(PRIMARY_COLOUR));
                }

                if (currMotTestId != motTestId) {
                    motTestId = currMotTestId;

                    MotTest motTest = new MotTest();
                    rfrAndAdvisoryItems = new ArrayList<>();
                    motTest.setRfrAndComments(rfrAndAdvisoryItems);

                    motTests.add(motTest);
                    
                    if (rs.getDate(MOT_COMPLETED_DATE) != null) {
                        motTest.setCompletedDate(SDF_DATE_TIME.format(rs.getTimestamp(MOT_COMPLETED_DATE)));
                    }
                    motTest.setTestResult(rs.getString(MOT_TEST_RESULT));
                    if (rs.getDate(MOT_EXPIRY_DATE) != null) {
                        motTest.setExpiryDate(SDF_DATE.format(rs.getTimestamp(MOT_EXPIRY_DATE)));
                    }
                    motTest.setOdometerValue(String.valueOf(rs.getInt(ODOMETER_VALUE)));
                    motTest.setOdometerUnit(rs.getString(ODOMETER_UNIT));
                    if (rs.getString(ODOMETER_RESULT_TYPE) != null) {
                        motTest.setOdometerResultType(ODOMETER_RESULT_TYPE_MAP.get(rs.getString(ODOMETER_RESULT_TYPE)));
                    }
                    motTest.setMotTestNumber(String.valueOf(rs.getBigDecimal(MOT_TEST_NUMBER)));
                }

                if (currMotTestRfrMapId != 0) {
                    RfrAndAdvisoryItem rfrAndAdvisoryItem = new RfrAndAdvisoryItem();
                    rfrAndAdvisoryItem.setType(rs.getString(RFR_TYPE));
                    rfrAndAdvisoryItem.setText(rs.getString(RFR_TEXT));
                    rfrAndAdvisoryItem.setDangerous(rs.getBoolean(RFR_DANGEROUS));
                    rfrAndAdvisoryItem.setDeficiencyCategoryCode(rs.getString(RFR_DEFICIENCY_CODE));
                    rfrAndAdvisoryItem.setDeficiencyCategoryDescription(rs.getString(RFR_DEFICIENCY_DESCRIPTION));
                    rfrAndAdvisoryItems.add(rfrAndAdvisoryItem);
                }
            }

            return vehicles;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }
}
