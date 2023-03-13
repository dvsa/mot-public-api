package uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle;

import uk.gov.dvsa.mot.trade.api.response.searchvehicle.AnnualTestResponse;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.SearchVehicleResponse;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class SearchVehicleResponseMapper {

    private static final String SEARCH_API_DATE_PATTERN = "d/M/yyyy";
    private static final String TRADE_API_DATE_PATTERN = "yyyy.MM.dd";

    public abstract List<SearchVehicleResponse> map(List<Vehicle> vehicles);

    protected void fillBaseVehicleResponseProperties(SearchVehicleResponse searchVehicleResponse, Vehicle vehicle) {
        searchVehicleResponse.setRegistration(vehicle.getVehicleIdentifier());
        searchVehicleResponse.setMake(vehicle.getMake());
        searchVehicleResponse.setModel(vehicle.getModel());
        searchVehicleResponse.setManufactureDate(transformDate(vehicle.getManufactureDate()));
        searchVehicleResponse.setVehicleType(vehicle.getVehicleType());
        searchVehicleResponse.setVehicleClass(vehicle.getVehicleClass());
        searchVehicleResponse.setRegistrationDate(transformDate(vehicle.getRegistrationDate()));
    }

    protected void fillBaseTestResponseProperties(AnnualTestResponse annualTestResponse, TestHistory testHistory) {
        annualTestResponse.setTestDate(transformDate(testHistory.getTestDate()));
        annualTestResponse.setTestType(testHistory.getTestType());
        annualTestResponse.setTestResult(testHistory.getTestResult());
        annualTestResponse.setTestCertificateNumber(testHistory.getTestCertificateSerialNo());
        annualTestResponse.setExpiryDate(transformDate(testHistory.getTestCertificateExpiryDateAtTest()));
        if (testHistory.getNumberOfDefectsAtTest() != null) {
            annualTestResponse.setNumberOfDefectsAtTest(testHistory.getNumberOfDefectsAtTest().toString());
        }
        if (testHistory.getNumberOfAdvisoryDefectsAtTest() != null) {
            annualTestResponse.setNumberOfAdvisoryDefectsAtTest(testHistory.getNumberOfAdvisoryDefectsAtTest().toString());
        }
    }

    protected String transformDate(String searchApiDate) {

        if (searchApiDate == null) {
            return null;
        }

        LocalDate date = LocalDate.parse(
                searchApiDate,
                DateTimeFormatter.ofPattern(SEARCH_API_DATE_PATTERN)
        );

        return date.format(DateTimeFormatter.ofPattern(TRADE_API_DATE_PATTERN));
    }
}
