package uk.gov.dvsa.mot.trade.api.response.mapper.cvsvehicle;

import uk.gov.dvsa.mot.app.util.CollectionUtils;
import uk.gov.dvsa.mot.trade.api.response.cvsvehicle.AnnualTestResponse;
import uk.gov.dvsa.mot.trade.api.response.cvsvehicle.AnnualTestV1Response;
import uk.gov.dvsa.mot.trade.api.response.cvsvehicle.CvsVehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.cvsvehicle.CvsVehicleV1Response;
import uk.gov.dvsa.mot.trade.api.response.cvsvehicle.DefectResponse;
import uk.gov.dvsa.mot.trade.api.response.cvsvehicle.DefectV1Response;
import uk.gov.dvsa.mot.vehicle.hgv.model.Defect;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class CvsVehicleV6ResponseMapper extends CvsVehicleResponseMapper {

    public List<CvsVehicleResponse> map(List<Vehicle> vehicles) {
        return vehicles.stream().map(this::mapVehicle).collect(Collectors.toList());
    }

    private CvsVehicleResponse mapVehicle(Vehicle vehicle) {
        CvsVehicleV1Response vehicleResponse = new CvsVehicleV1Response();
        this.fillBaseVehicleResponseProperties(vehicleResponse, vehicle);
        vehicleResponse.setAnnualTestExpiryDate(transformDate(vehicle.getTestCertificateExpiryDate()));

        if (!CollectionUtils.isNullOrEmpty(vehicle.getTestHistory())) {
            vehicleResponse.setAnnualTests(
                    vehicle.getTestHistory()
                            .stream()
                            .map(this::mapTest)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList())
            );
        }

        return vehicleResponse;
    }

    private AnnualTestResponse mapTest(TestHistory testHistory) {
        if (!CvsTestTypeMapFilter.canDisplayTest(testHistory)) {
            return null;
        }

        AnnualTestV1Response annualTestResponse = new AnnualTestV1Response();
        this.fillBaseTestResponseProperties(annualTestResponse, testHistory);

        if (!CollectionUtils.isNullOrEmpty(testHistory.getTestHistoryDefects())) {
            annualTestResponse.setDefects(
                    testHistory.getTestHistoryDefects().stream().map(this::mapDefect).collect(Collectors.toList())
            );
        }

        return annualTestResponse;
    }

    private DefectResponse mapDefect(Defect defectItem) {
        DefectV1Response defectResponse = new DefectV1Response();
        defectResponse.setFailureItemNo(defectItem.getFailureItemNo());
        defectResponse.setSeverityCode(defectItem.getSeverityCode());
        defectResponse.setSeverityDescription(defectItem.getSeverityDescription());
        defectResponse.setFailureReason(defectItem.getFailureReason());

        return defectResponse;
    }
}
