package uk.gov.dvsa.mot.trade.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleV5Response extends VehicleResponse {
    private String vehicleId;
    private String registrationDate;
    private String manufactureDate;
    private String engineSize;
    private String motTestDueDate;
    private List<MotTestV2Response> motTests;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(Integer engineSize) {
        if (engineSize != null) {
            this.engineSize = engineSize.toString();
        }
    }

    public String getMotTestDueDate() {
        return motTestDueDate;
    }

    public void setMotTestDueDate(String motTestDueDate) {
        this.motTestDueDate = motTestDueDate;
    }

    public List<MotTestV2Response> getMotTests() {
        return motTests;
    }

    public void setMotTests(List<MotTestV2Response> motTests) {
        this.motTests = motTests;
    }
}
