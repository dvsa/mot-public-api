package uk.gov.dvsa.mot.motr.model;

import java.time.LocalDate;
import java.time.Year;

public interface VehicleWithLatestTest {
    boolean hasTest();
    
    boolean hasTestExpiryDate();

    String getRegistration();

    String getMake();

    String getModel();

    String getPrimaryColour();

    String getSecondaryColour();

    boolean hasManufactureYear();

    Year getManufactureYear();

    LocalDate getTestExpiryDate();

    boolean hasTestNumber();

    String getTestNumber();

    String getDvlaVehicleId();

    String getVehicleType();
}
