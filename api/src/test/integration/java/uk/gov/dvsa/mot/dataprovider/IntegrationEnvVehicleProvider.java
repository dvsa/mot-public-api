package uk.gov.dvsa.mot.dataprovider;

import uk.gov.dvsa.mot.trade.api.Vehicle;

public class IntegrationEnvVehicleProvider implements VehicleProvider {

    public Vehicle getClass4Vehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistration("FNZ6110");
        vehicle.setMake("RENAULT");

        return vehicle;
    }

    public Vehicle getDvlaVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistration("F50GGP");

        return vehicle;
    }
}
