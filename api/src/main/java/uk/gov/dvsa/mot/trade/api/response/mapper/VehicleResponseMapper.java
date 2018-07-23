package uk.gov.dvsa.mot.trade.api.response.mapper;

import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.MotTestResponse;
import uk.gov.dvsa.mot.trade.api.response.VehicleResponse;

import java.util.List;

public abstract class VehicleResponseMapper {
    public abstract List<VehicleResponse> map(List<Vehicle> vehicles);

    protected void fillBaseVehicleResponseProperties(VehicleResponse vehicleResponse, Vehicle vehicle) {
        vehicleResponse.setRegistration(vehicle.getRegistration());
        vehicleResponse.setMake(vehicle.getMake());
        vehicleResponse.setMakeInFull(vehicle.getMakeInFull());
        vehicleResponse.setModel(vehicle.getModel());
        vehicleResponse.setFirstUsedDate(vehicle.getFirstUsedDate());
        vehicleResponse.setFuelType(vehicle.getFuelType());
        vehicleResponse.setPrimaryColour(vehicle.getPrimaryColour());
        vehicleResponse.setSecondaryColour(vehicle.getSecondaryColour());
        vehicleResponse.setDvlaId(vehicle.getDvlaId());
        vehicleResponse.setManufactureYear(vehicle.getManufactureYear());
    }

    protected void fillBaseMotTestResponseProperties(MotTestResponse motTestResponse, MotTest motTest) {
        motTestResponse.setCompletedDate(motTest.getCompletedDate());
        motTestResponse.setExpiryDate(motTest.getExpiryDate());
        motTestResponse.setOdometerValue(motTest.getOdometerValue());
        motTestResponse.setOdometerUnit(motTest.getOdometerUnit());
        motTestResponse.setMotTestNumber(motTest.getMotTestNumber());
        motTestResponse.setTestResult(motTest.getTestResult());
    }
}
