package uk.gov.dvsa.mot.vehicle.hgv.response;

import uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothVehicle;

public class ResponseVehicle {
    MothVehicle vehicle;

    public MothVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(MothVehicle vehicle) {
        this.vehicle = vehicle;
    }
}
