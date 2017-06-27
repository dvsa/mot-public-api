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
import java.util.List;

public class VehicleResultSetExtractor implements ResultSetExtractor<List<Vehicle>> {
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

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
                int currVehicleId = rs.getInt(1);
                long currMotTestId = rs.getLong(2);
                long currMotTestRfrMapId = rs.getLong(3);

                if (currVehicleId != vehicleId) {
                    vehicleId = currVehicleId;

                    vehicle = new Vehicle();
                    motTests = new ArrayList<>();
                    vehicle.setMotTests(motTests);

                    vehicles.add(vehicle);

                    // vehicle.setId( vehicleId );
                    vehicle.setRegistration(rs.getString(4));
                    vehicle.setMake(rs.getString(5));
                    vehicle.setModel(rs.getString(6));
                    if (rs.getDate(7) != null) {
                        vehicle.setFirstUsedDate(SDF_DATE.format(rs.getTimestamp(7)));
                    }
                    vehicle.setFuelType(rs.getString(8));
                    vehicle.setPrimaryColour(rs.getString(9));
                    // field 10 secondary colour not used
                }

                if (currMotTestId != motTestId) {
                    motTestId = currMotTestId;

                    MotTest motTest = new MotTest();
                    rfrAndAdvisoryItems = new ArrayList<>();
                    motTest.setRfrAndComments(rfrAndAdvisoryItems);

                    motTests.add(motTest);

                    // motTest.setId( currMotTestId ) ;
                    // field 11 - started date not used
                    if (rs.getDate(12) != null) {
                        motTest.setCompletedDate(SDF_DATE_TIME.format(rs.getTimestamp(12)));
                    }
                    motTest.setTestResult(rs.getString(13));
                    if (rs.getDate(14) != null) {
                        motTest.setExpiryDate(SDF_DATE.format(rs.getTimestamp(14)));
                    }
                    motTest.setOdometerValue(String.valueOf(rs.getInt(15)));
                    motTest.setOdometerUnit(rs.getString(16));
                    // field 17 - odometer result type not used
                    motTest.setMotTestNumber(String.valueOf(rs.getBigDecimal(18)));
                }

                if (currMotTestRfrMapId != 0) {
                    RfrAndAdvisoryItem rfrAndAdvisoryItem = new RfrAndAdvisoryItem();
                    rfrAndAdvisoryItem.setType(rs.getString(19));
                    rfrAndAdvisoryItem.setText(rs.getString(20));
                    rfrAndAdvisoryItems.add(rfrAndAdvisoryItem);
                }
            }

            return vehicles;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }
}
