package uk.gov.dvsa.mot.dataprovider;

import uk.gov.dvsa.mot.trade.api.Vehicle;

public class IntegrationEnvVehicleProvider implements VehicleProvider {

    public Vehicle getClass4Vehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistration("AC94MHS");
        vehicle.setMake("TOYOTA");

        return vehicle;
    }

    public Vehicle getDvlaVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistration("FI50VJW");

        return vehicle;
    }
}
