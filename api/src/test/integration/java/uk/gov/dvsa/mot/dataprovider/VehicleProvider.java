package uk.gov.dvsa.mot.dataprovider;

import uk.gov.dvsa.mot.trade.api.Vehicle;

public interface VehicleProvider {
    Vehicle getClass4Vehicle();
    
    Vehicle getDvlaVehicle();
}
