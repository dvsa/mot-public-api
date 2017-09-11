package uk.gov.dvsa.mot.vehicle.read.core;

import uk.gov.dvsa.mot.trade.api.DvlaVehicle;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;

import java.util.List;

public interface VehicleReadService {

    Vehicle getVehicleById(int id);

    Vehicle getVehicleByIdAndVersion(int it, int version);

    Vehicle findByRegistrationAndMake(String registration, String make);

    List<Vehicle> findByRegistration(String registration);

    List<Vehicle> findByDvlaVehicleId(Integer dvlaVehicleId);

    List<DvlaVehicle> findDvlaVehicleByRegistration(String registration);

    DvlaVehicle getDvlaVehicleByRegistration(String registration);

    List<DvlaVehicle> findDvlaVehicleById(Integer dvlaVehicleId);

    List<Vehicle> findByMotTestNumberWithSameRegistrationAndVin(long motTestNumber);

    List<Vehicle> getVehiclesById(int startId, int endId);

    List<Vehicle> getVehiclesByPage(int offset, int limit);

    List<String> getMakes();
}