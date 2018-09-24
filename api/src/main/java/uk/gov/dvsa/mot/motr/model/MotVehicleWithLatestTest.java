package uk.gov.dvsa.mot.motr.model;

import uk.gov.dvsa.mot.motr.service.DateConverter;
import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;

import java.time.LocalDate;
import java.time.Year;

import javax.validation.constraints.NotNull;

import static java.util.Objects.requireNonNull;

public class MotVehicleWithLatestTest implements VehicleWithLatestTest {

    private Vehicle vehicle;
    private MotTest test;

    public MotVehicleWithLatestTest(@NotNull Vehicle vehicle, MotTest test) {
        requireNonNull(vehicle);
        this.vehicle = vehicle;
        this.test = test;
    }

    @Override
    public boolean hasTest() {
        return test != null;
    }

    @Override
    public boolean hasTestExpiryDate() {
        return test != null && test.getExpiryDate() != null;
    }

    @Override
    public String getRegistration() {
        return vehicle.getRegistration();
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
        return vehicle.getPrimaryColour();
    }

    @Override
    public String getSecondaryColour() {
        return vehicle.getSecondaryColour();
    }

    @Override
    public Year getManufactureYear() {
        return hasManufactureYear() ?  DateConverter.toYear(vehicle.getManufactureDate()) : null;
    }

    @Override
    public boolean hasManufactureYear() {
        return vehicle.getManufactureDate() != null;
    }


    @Override
    public LocalDate getTestExpiryDate() {
        return test != null ? DateConverter.toLocalDate(test.getExpiryDate()) : null;
    }

    @Override
    public boolean hasTestNumber() {
        return test != null && test.getNumber() != null;
    }

    @Override
    public String getTestNumber() {
        return test != null && test.getNumber() != null ? test.getNumber().toString() : null;
    }

    @Override
    public String getDvlaVehicleId() {
        return null;
    }

    @Override
    public String getVehicleType() {
        return VehicleType.MOT.name();
    }
}
