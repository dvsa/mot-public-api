package uk.gov.dvsa.mot.trade.read.core;

import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.util.Date;
import java.util.List;

public interface TradeReadService {
    List<String> getMakes();

    List<DisplayMotTestItem> getMotTestsByRegistrationAndMake(String registration, String make);

    List<Vehicle> getVehiclesByVehicleId(int id);

    List<Vehicle> getVehiclesMotTestsByMotTestNumber(long number);

    List<Vehicle> getVehiclesByRegistrationAndMake(String registration, String make);

    List<Vehicle> getVehiclesByPage(int page);

    List<Vehicle> getVehiclesByDatePage(Date date, Integer page);

    Vehicle getLatestMotTestByRegistration(String registration);

    Vehicle getDvlaVehicleByRegistration(String registration);

    Vehicle getDvlaVehicleById(Integer dvlaVehicleId);

    Vehicle getLatestMotTestByDvlaVehicleId(Integer dvlaVehicleId);

    Vehicle getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber);
}
