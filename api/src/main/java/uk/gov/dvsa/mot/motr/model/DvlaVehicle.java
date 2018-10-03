package uk.gov.dvsa.mot.motr.model;

import uk.gov.dvsa.mot.motr.service.DateConverter;

import java.time.LocalDate;
import java.time.Year;

import javax.validation.constraints.NotNull;

import static java.util.Objects.requireNonNull;

public class DvlaVehicle implements VehicleWithLatestTest {

    private final uk.gov.dvsa.mot.trade.api.DvlaVehicle dvlaVehicle;
    private final LocalDate firstMotDueDate;

    public DvlaVehicle(@NotNull uk.gov.dvsa.mot.trade.api.DvlaVehicle dvlaVehicle, LocalDate firstMotDueDate) {
        requireNonNull(dvlaVehicle);
        this.dvlaVehicle = dvlaVehicle;
        this.firstMotDueDate = firstMotDueDate;
    }

    @Override
    public boolean hasTest() {
        return false;
    }

    @Override
    public boolean hasTestExpiryDate() {
        return firstMotDueDate != null;
    }

    @Override
    public String getRegistration() {
        return dvlaVehicle.getRegistration();
    }

    @Override
    public String getMake() {
        return dvlaVehicle.getMakeDetail();
    }

    @Override
    public String getModel() {
        return dvlaVehicle.getModelDetail();
    }

    @Override
    public String getPrimaryColour() {
        return dvlaVehicle.getColour1();
    }

    @Override
    public String getSecondaryColour() {
        return dvlaVehicle.getColour2();
    }

    @Override
    public Year getManufactureYear() {
        return dvlaVehicle != null ?  DateConverter.toYear(dvlaVehicle.getManufactureDate()) : null;
    }

    @Override
    public boolean hasManufactureYear() {
        return dvlaVehicle != null;
    }

    @Override
    public LocalDate getTestExpiryDate() {
        return firstMotDueDate;
    }

    @Override
    public boolean hasTestNumber() {
        return false;
    }

    @Override
    public String getTestNumber() {
        return null;
    }

    @Override
    public boolean hasTestDate() {
        return false;
    }

    @Override
    public LocalDate getTestDate() {
        return null;
    }

    @Override
    public String getDvlaVehicleId() {
        return Integer.toString(dvlaVehicle.getDvlaVehicleId());
    }

    @Override
    public String getVehicleType() {
        return VehicleType.MOT.name();
    }

    @Override
    public String getMotVehicleClass() {
        return null;
    }
}
