package uk.gov.dvsa.mot.trade.api.response.mapper;

import com.amazonaws.util.StringUtils;

import uk.gov.dvsa.mot.app.util.CollectionUtils;
import uk.gov.dvsa.mot.security.ParamObfuscator;
import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.MotTestV2Response;
import uk.gov.dvsa.mot.trade.api.response.RfrAndAdvisoryV2Response;
import uk.gov.dvsa.mot.trade.api.response.VehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.VehicleV5Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleV6ResponseMapper extends VehicleResponseMapper {

    private static final String TEST_TYPE_FAIL = "FAIL";
    private static final String RFR_DEFICIENCY_CATEGORY_PRE_EU_CODE = "PE";

    public List<VehicleResponse> map(List<Vehicle> vehicles) throws ParamObfuscator.ObfuscationException {
        List<VehicleResponse> list = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            VehicleResponse vehicleResponse = mapVehicle(vehicle);
            if (vehicle != null) {
                list.add(vehicleResponse);
            }
        }
        return list;
    }

    private VehicleResponse mapVehicle(Vehicle vehicle) throws ParamObfuscator.ObfuscationException {
        VehicleV5Response vehicleResponse = new VehicleV5Response();
        this.fillBaseVehicleResponseProperties(vehicleResponse, vehicle);
        vehicleResponse.setEngineSize(vehicle.getCylinderCapacity());
        vehicleResponse.setManufactureDate(vehicle.getManufactureDate());
        vehicleResponse.setRegistrationDate(vehicle.getRegistrationDate());

        vehicleResponse.setMotTestDueDate(vehicle.getMotTestDueDate());
        if (!CollectionUtils.isNullOrEmpty(vehicle.getMotTests())) {
            vehicleResponse.setMotTests(
                    vehicle.getMotTests().stream().map(this::mapTest).collect(Collectors.toList())
            );
            vehicleResponse.setVehicleId(
                    ParamObfuscator.obfuscate(vehicle.getId().toString())
            );
        }

        return vehicleResponse;
    }

    private MotTestV2Response mapTest(MotTest motTest) {
        MotTestV2Response motTestResponse = new MotTestV2Response();
        this.fillBaseMotTestResponseProperties(motTestResponse, motTest);

        motTestResponse.setOdometerResultType(motTest.getOdometerResultType());
        motTestResponse.setRfrAndComments(
                motTest.getRfrAndComments().stream().map(this::mapRfrItem).collect(Collectors.toList())
        );

        return motTestResponse;
    }

    private RfrAndAdvisoryV2Response mapRfrItem(RfrAndAdvisoryItem rfrAndAdvisoryItem) {
        RfrAndAdvisoryV2Response rfrResponse = new RfrAndAdvisoryV2Response();
        rfrResponse.setDangerous(rfrAndAdvisoryItem.isDangerous());
        rfrResponse.setText(rfrAndAdvisoryItem.getText());
        rfrResponse.setType(this.getRfrType(rfrAndAdvisoryItem));

        return rfrResponse;
    }

    private String getRfrType(RfrAndAdvisoryItem rfrAndAdvisoryItem) {
        if (!StringUtils.isNullOrEmpty(rfrAndAdvisoryItem.getType()) && rfrAndAdvisoryItem.getType().equals(TEST_TYPE_FAIL)) {
            if (StringUtils.isNullOrEmpty(rfrAndAdvisoryItem.getDeficiencyCategoryCode())
                    || rfrAndAdvisoryItem.getDeficiencyCategoryCode().equals(RFR_DEFICIENCY_CATEGORY_PRE_EU_CODE)) {

                return rfrAndAdvisoryItem.getType();
            } else {
                return StringUtils.upperCase(rfrAndAdvisoryItem.getDeficiencyCategoryDescription());
            }
        } else {
            return rfrAndAdvisoryItem.getType();
        }
    }
}
