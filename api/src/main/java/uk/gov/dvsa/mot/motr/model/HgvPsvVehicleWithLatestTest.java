package uk.gov.dvsa.mot.motr.model;

import uk.gov.dvsa.mot.trade.service.AnnualTestExpiryDateCalculator;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.constraints.NotNull;

import static java.util.Objects.requireNonNull;

public class HgvPsvVehicleWithLatestTest implements VehicleWithLatestTest {

    private static final DateTimeFormatter HGV_PSV_API_DATE_PATTERN = DateTimeFormatter.ofPattern("d/M/yyyy");

    private Vehicle vehicle;
    private String dvlaVehicleId;
    private LocalDate testExpiryDate;
    private TestHistory latestTest;

    public HgvPsvVehicleWithLatestTest(@NotNull Vehicle vehicle, String dvlaVehicleId) {
        requireNonNull(vehicle);
        this.vehicle = vehicle;
        this.dvlaVehicleId = dvlaVehicleId;

        testExpiryDate = calculateTestExpiryDate(vehicle);
        latestTest = findLatestTest(vehicle);
    }

    @Override
    public boolean hasTest() {
        return latestTest != null;
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
        return latestTest != null && latestTest.getTestCertificateSerialNo() != null;
    }

    @Override
    public String getTestNumber() {
        return latestTest != null ? latestTest.getTestCertificateSerialNo() : null;
    }

    @Override
    public boolean hasTestDate() {
        return latestTest != null && latestTest.getTestDate() != null;
    }

    @Override
    public LocalDate getTestDate() {
        return latestTest != null && latestTest.getTestDate() != null
            ?  LocalDate.parse(latestTest.getTestDate(), HGV_PSV_API_DATE_PATTERN) : null;
    }

    @Override
    public String getDvlaVehicleId() {
        return dvlaVehicleId;
    }

    @Override
    public String getVehicleType() {
        return vehicle.getVehicleType();
    }

    @Override
    public String getMotVehicleClass() {
        return null;
    }

    private static LocalDate calculateTestExpiryDate(Vehicle vehicle) {
        AnnualTestExpiryDateCalculator annualTestExpiryDateCalculator = new AnnualTestExpiryDateCalculator();
        return annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle)
                .orElse(null);
    }

    private static TestHistory findLatestTest(Vehicle vehicle) {
        if (vehicle.getTestHistory() != null && !vehicle.getTestHistory().isEmpty()) {
            return vehicle.getTestHistory().get(0);
        } else {
            return null;
        }
    }
}
