package uk.gov.dvsa.mot.motr.model;

import org.junit.Test;

import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HgvPsvVehicleWithLatestTestTest {

    private static final String REGISTRATION = "REG123456";
    private static final String DVLA_VEHICLE_ID = "1234";
    private static final String TEST_NUMBER = "12345678";

    private static final String SECOND_TEST_NUMBER = "8754321";
    private static final String MAKE = "MAKE";
    private static final String MODEL = "MODEL";
    private static final int YEAR_OF_MANUFACTURE = 2013;

    @Test
    public void vehicleModelDataShouldMatchHgvPsvVehicle() {
        Vehicle vehicle = createVehicleWithTestHistory("PSV");
        VehicleWithLatestTest vehicleModel = new HgvPsvVehicleWithLatestTest(vehicle, DVLA_VEHICLE_ID);

        assertEquals(DVLA_VEHICLE_ID, vehicleModel.getDvlaVehicleId());
        assertEquals(REGISTRATION, vehicleModel.getRegistration());
        assertEquals("PSV", vehicleModel.getVehicleType());
        assertEquals(MAKE, vehicleModel.getMake());
        assertEquals(MODEL, vehicleModel.getModel());
        assertEquals(Year.of(YEAR_OF_MANUFACTURE), vehicleModel.getManufactureYear());
        assertEquals(LocalDate.of(2013, 1, 3), vehicleModel.getTestDate());
    }

    @Test
    public void whenVehicleTestHistoryIsNotEmpty_AnnualTestExpiryDateShouldBeTheExpiryDate() throws Exception {
        Vehicle vehicle = createVehicleWithTestHistory("HGV");
        VehicleWithLatestTest vehicleModel = new HgvPsvVehicleWithLatestTest(vehicle, DVLA_VEHICLE_ID);

        assertEquals(LocalDate.of(2018, 01, 01), vehicleModel.getTestExpiryDate());
    }

    @Test
    public void whenVehicleTestHistoryIsEmptyAndVehicleIsHgv_AnnualTestExpiryDateShouldBeCalculated() throws Exception {
        Vehicle vehicle = createVehicle("HGV");
        vehicle.setRegistrationDate("31/03/2017");
        VehicleWithLatestTest vehicleModel = new HgvPsvVehicleWithLatestTest(vehicle, DVLA_VEHICLE_ID);

        assertEquals(LocalDate.of(2018, 03, 31), vehicleModel.getTestExpiryDate());
    }

    @Test
    public void whenHgvPsvExpiryDateIsUnknown_returnVehicleWithNullExpiry() throws Exception {
        Vehicle vehicle = createVehicle("HGV");
        VehicleWithLatestTest vehicleModel = new HgvPsvVehicleWithLatestTest(vehicle, DVLA_VEHICLE_ID);

        assertNull(vehicleModel.getTestExpiryDate());
    }

    @Test
    public void whenVehicleTestHistoryIsNotEmpty_TestNumberShouldBeReturnedAsMotTestNumber() throws Exception {
        Vehicle vehicle = createVehicleWithTestHistory("HGV");
        VehicleWithLatestTest vehicleModel = new HgvPsvVehicleWithLatestTest(vehicle, DVLA_VEHICLE_ID);

        assertEquals(TEST_NUMBER, vehicleModel.getTestNumber());
    }

    @Test
    public void whenVehicleTestHistoryIsNotEmpty_LatestTestNumberShouldBeReturnedAsMotTestNumber() throws Exception {
        Vehicle vehicle = createVehicleWithTestHistory("HGV");
        List<TestHistory> testHistory = new ArrayList<>();
        TestHistory historyItem =  new TestHistory();
        historyItem.setTestCertificateSerialNo(TEST_NUMBER);
        historyItem.setTestDate("03/01/2013");
        testHistory.add(historyItem);
        TestHistory historyItem2 =  new TestHistory();
        historyItem2.setTestCertificateSerialNo(SECOND_TEST_NUMBER);
        historyItem2.setTestDate("03/01/2011");
        testHistory.add(historyItem2);
        vehicle.setTestHistory(testHistory);
        VehicleWithLatestTest vehicleModel = new HgvPsvVehicleWithLatestTest(vehicle, DVLA_VEHICLE_ID);

        assertEquals(TEST_NUMBER, vehicleModel.getTestNumber());
    }

    private Vehicle createVehicleWithTestHistory(String vehicleType) {
        List<TestHistory> testHistory = new ArrayList<>();
        TestHistory historyItem =  new TestHistory();
        historyItem.setTestCertificateSerialNo(TEST_NUMBER);
        historyItem.setTestDate("03/01/2013");
        testHistory.add(historyItem);

        Vehicle vehicle = createVehicle(vehicleType);
        vehicle.setTestHistory(testHistory);
        vehicle.setTestCertificateExpiryDate("01/01/2018");
        return vehicle;
    }

    private uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle createVehicle(String vehicleType) {
        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();

        vehicle.setVehicleIdentifier(REGISTRATION);
        vehicle.setMake(MAKE);
        vehicle.setModel(MODEL);
        vehicle.setVehicleType(vehicleType);
        vehicle.setYearOfManufacture(YEAR_OF_MANUFACTURE);

        return vehicle;
    }
}
