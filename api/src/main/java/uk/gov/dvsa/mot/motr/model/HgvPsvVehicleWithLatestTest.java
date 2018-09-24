package uk.gov.dvsa.mot.motr.model;

import uk.gov.dvsa.mot.trade.service.AnnualTestExpiryDateCalculator;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.time.LocalDate;
import java.time.Year;

import javax.validation.constraints.NotNull;

import static java.util.Objects.requireNonNull;

public class HgvPsvVehicleWithLatestTest implements VehicleWithLatestTest {

    private Vehicle vehicle;
    private String dvlaVehicleId;
    private LocalDate testExpiryDate;
    private String testNumber;

    public HgvPsvVehicleWithLatestTest(@NotNull Vehicle vehicle, String dvlaVehicleId) {
        requireNonNull(vehicle);
        this.vehicle = vehicle;
        this.dvlaVehicleId = dvlaVehicleId;

        testExpiryDate = calculateTestExpiryDate(vehicle);
        testNumber = findLatestTestNumber(vehicle);
    }

    @Override
    public boolean hasTest() {
        return vehicle.getTestHistory() != null && vehicle.getTestHistory().length > 0;
    }

    @Override
    public boolean hasTestExpiryDate() {
        return testExpiryDate != null;
    }

    @Override
    public String getRegistration() {
        return vehicle.getVehicleIdentifier();
    }

    @Override
    public String getMake() {
        return vehicle.getMake();
    }

    @Override
    public String getModel() {
        return vehicle.getModel();
    }

    @Override
    public String getPrimaryColour() {
        return null;
    }

    @Override
    public String getSecondaryColour() {
        return null;
    }

    @Override
    public boolean hasManufactureYear() {
        return vehicle.getYearOfManufacture() != null;
    }

    @Override
    public Year getManufactureYear() {
        return vehicle.getYearOfManufacture() != null ? Year.of(vehicle.getYearOfManufacture()) : null;
    }

    @Override
    public LocalDate getTestExpiryDate() {
        return testExpiryDate;
    }

    @Override
    public boolean hasTestNumber() {
        return testNumber != null;
    }

    @Override
    public String getTestNumber() {
        return testNumber;
    }

    @Override
    public String getDvlaVehicleId() {
        return dvlaVehicleId;
    }

    @Override
    public String getVehicleType() {
        return vehicle.getVehicleType();
    }

    private static LocalDate calculateTestExpiryDate(Vehicle vehicle) {
        AnnualTestExpiryDateCalculator annualTestExpiryDateCalculator = new AnnualTestExpiryDateCalculator();
        return annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle)
                .orElse(null);
    }

    private static String findLatestTestNumber(Vehicle vehicle) {
        if (vehicle.getTestHistory() != null && vehicle.getTestHistory().length > 0) {
            TestHistory[] testHistory = vehicle.getTestHistory();
            return testHistory[testHistory.length - 1].getTestCertificateSerialNo();
        } else {
            return null;
        }
    }

}
