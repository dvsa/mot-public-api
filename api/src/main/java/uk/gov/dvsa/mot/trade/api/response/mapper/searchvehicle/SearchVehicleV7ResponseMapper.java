package uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle;

import uk.gov.dvsa.mot.app.util.CollectionUtils;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.AnnualTestResponse;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.AnnualTestV2Response;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.DefectResponse;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.DefectV1Response;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.SearchVehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.SearchVehicleV1Response;
import uk.gov.dvsa.mot.vehicle.hgv.model.Defect;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchVehicleV7ResponseMapper extends SearchVehicleResponseMapper {

    public List<SearchVehicleResponse> map(List<Vehicle> vehicles) {
        return vehicles.stream().map(this::mapVehicle).collect(Collectors.toList());
    }

    private SearchVehicleResponse mapVehicle(Vehicle vehicle) {
        SearchVehicleV1Response vehicleResponse = new SearchVehicleV1Response();
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

        AnnualTestV2Response annualTestResponse = new AnnualTestV2Response();
        this.fillBaseTestResponseProperties(annualTestResponse, testHistory);
        annualTestResponse.setLocation(testHistory.getLocation());

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
