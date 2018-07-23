package uk.gov.dvsa.mot.trade.api.response.mapper;

import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.MotTestV1Response;
import uk.gov.dvsa.mot.trade.api.response.RfrAndAdvisoryResponse;
import uk.gov.dvsa.mot.trade.api.response.VehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.VehicleV1Response;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleV1ResponseMapper extends VehicleResponseMapper {

    public List<VehicleResponse> map(List<Vehicle> vehicles) {
        return vehicles.stream().map(this::mapVehicle).collect(Collectors.toList());
    }

    private VehicleResponse mapVehicle(Vehicle vehicle) {
        VehicleV1Response vehicleResponse = new VehicleV1Response();
        this.fillBaseVehicleResponseProperties(vehicleResponse, vehicle);

        vehicleResponse.setMotTestExpiryDate(vehicle.getMotTestDueDate());
        vehicleResponse.setMotTests(
                vehicle.getMotTests().stream().map(this::mapTest).collect(Collectors.toList())
        );

        return vehicleResponse;
    }

    private MotTestV1Response mapTest(MotTest motTest) {
        MotTestV1Response motTestResponse = new MotTestV1Response();
        this.fillBaseMotTestResponseProperties(motTestResponse, motTest);

        motTestResponse.setRfrAndComments(
                motTest.getRfrAndComments().stream().map(this::mapRfr).collect(Collectors.toList())
        );

        return motTestResponse;
    }

    private RfrAndAdvisoryResponse mapRfr(RfrAndAdvisoryItem rfrAndAdvisoryItem) {
        RfrAndAdvisoryResponse rfrResponse = new RfrAndAdvisoryResponse();
        rfrResponse.setText(rfrAndAdvisoryItem.getText());
        rfrResponse.setType(rfrAndAdvisoryItem.getType());

        return rfrResponse;
    }
}
