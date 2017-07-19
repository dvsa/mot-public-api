package uk.gov.dvsa.mot.persist;

import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.util.Date;
import java.util.List;

public interface TradeReadDao {

    List<Vehicle> getVehiclesMotTestsByVehicleId(int vehicleId);

    List<Vehicle> getVehiclesMotTestsByMotTestNumber(long number);

    List<Vehicle> getVehiclesMotTestsByRegistrationAndMake(String registration, String make);

    List<Vehicle> getVehiclesMotTestsByDateRange(Date startDate, Date endDate);

    List<Vehicle> getVehiclesMotTestsByRange(int startVehicleId, int endVehicleId);
}