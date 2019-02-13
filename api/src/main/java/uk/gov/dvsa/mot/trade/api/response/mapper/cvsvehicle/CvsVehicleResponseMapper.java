package uk.gov.dvsa.mot.trade.api.response.mapper.cvsvehicle;

import uk.gov.dvsa.mot.trade.api.response.cvsvehicle.AnnualTestResponse;
import uk.gov.dvsa.mot.trade.api.response.cvsvehicle.CvsVehicleResponse;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class CvsVehicleResponseMapper {

    private static final DateTimeFormatter HGV_PSV_API_DATE_PATTERN = DateTimeFormatter.ofPattern("d/M/yyyy");

    public abstract List<CvsVehicleResponse> map(List<Vehicle> vehicles);

    protected void fillBaseVehicleResponseProperties(CvsVehicleResponse cvsVehicleResponse, Vehicle vehicle) {
        cvsVehicleResponse.setRegistration(vehicle.getVehicleIdentifier());
        cvsVehicleResponse.setMake(vehicle.getMake());
        cvsVehicleResponse.setModel(vehicle.getModel());
        cvsVehicleResponse.setManufactureDate(transformDate(vehicle.getManufactureDate()));
        cvsVehicleResponse.setVehicleType(vehicle.getVehicleType());
        cvsVehicleResponse.setVehicleClass(vehicle.getVehicleClass());
        cvsVehicleResponse.setRegistrationDate(transformDate(vehicle.getRegistrationDate()));
    }

    protected void fillBaseTestResponseProperties(AnnualTestResponse annualTestResponse, TestHistory testHistory) {
        annualTestResponse.setTestDate(transformDate(testHistory.getTestDate()));
        annualTestResponse.setTestType(testHistory.getTestType());
        annualTestResponse.setTestResult(testHistory.getTestResult());
        annualTestResponse.setTestCertificateNumber(testHistory.getTestCertificateSerialNo());
        annualTestResponse.setExpiryDate(transformDate(testHistory.getTestCertificateExpiryDateAtTest()));
        annualTestResponse.setNumberOfDefectsAtTest(testHistory.getNumberOfDefectsAtTest().toString());
        annualTestResponse.setNumberOfAdvisoryDefectsAtTest(testHistory.getNumberOfAdvisoryDefectsAtTest().toString());
    }

    protected String transformDate(String hgvPsvApiDate) {

        if (hgvPsvApiDate == null) {
            return null;
        }

        LocalDate date = LocalDate.parse(hgvPsvApiDate, HGV_PSV_API_DATE_PATTERN);

        return date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
