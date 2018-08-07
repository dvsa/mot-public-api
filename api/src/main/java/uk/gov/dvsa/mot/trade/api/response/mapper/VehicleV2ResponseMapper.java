package uk.gov.dvsa.mot.trade.api.response.mapper;

import uk.gov.dvsa.mot.app.util.CollectionUtils;
import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.MotTestV2Response;
import uk.gov.dvsa.mot.trade.api.response.RfrAndAdvisoryV2Response;
import uk.gov.dvsa.mot.trade.api.response.VehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.VehicleV2Response;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleV2ResponseMapper extends VehicleResponseMapper {

    public List<VehicleResponse> map(List<Vehicle> vehicles) {
        return vehicles.stream().map(this::mapVehicle).collect(Collectors.toList());
    }

    private VehicleResponse mapVehicle(Vehicle vehicle) {
        VehicleV2Response vehicleResponse = new VehicleV2Response();
        this.fillBaseVehicleResponseProperties(vehicleResponse, vehicle);

        vehicleResponse.setMotTestExpiryDate(vehicle.getMotTestDueDate());
        if (!CollectionUtils.isNullOrEmpty(vehicle.getMotTests())) {
            vehicleResponse.setMotTests(
                    vehicle.getMotTests().stream().map(this::mapTest).collect(Collectors.toList())
            );
        }

        return vehicleResponse;
    }

    private MotTestV2Response mapTest(MotTest motTest) {
        MotTestV2Response motTestResponse = new MotTestV2Response();
        this.fillBaseMotTestResponseProperties(motTestResponse, motTest);

        motTestResponse.setOdometerResultType(motTest.getOdometerResultType());
        motTestResponse.setRfrAndComments(
                motTest.getRfrAndComments().stream().map(this::mapRfr).collect(Collectors.toList())
        );

        return motTestResponse;
    }

    private RfrAndAdvisoryV2Response mapRfr(RfrAndAdvisoryItem rfrAndAdvisoryItem) {
        RfrAndAdvisoryV2Response rfrResponse = new RfrAndAdvisoryV2Response();
        rfrResponse.setDangerous(rfrAndAdvisoryItem.isDangerous());
        rfrResponse.setText(rfrAndAdvisoryItem.getText());
        rfrResponse.setType(rfrAndAdvisoryItem.getType());

        return rfrResponse;
    }
}
