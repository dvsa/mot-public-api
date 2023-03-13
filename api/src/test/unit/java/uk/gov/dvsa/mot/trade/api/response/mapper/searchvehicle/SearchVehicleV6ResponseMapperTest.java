package uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle;

import org.junit.Before;
import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.response.mapper.MockVehicleDataHelper;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.AnnualTestResponse;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.AnnualTestV1Response;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.DefectResponse;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.DefectV1Response;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.SearchVehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.searchvehicle.SearchVehicleV1Response;
import uk.gov.dvsa.mot.vehicle.hgv.model.Defect;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

public class SearchVehicleV6ResponseMapperTest {

    private SearchVehicleV6ResponseMapper vehicleResponseMapper;

    @Before
    public void init() {
        vehicleResponseMapper = new SearchVehicleV6ResponseMapper();
    }

    @Test
    public void map_mapsAllPropertiesCorrectly() {
        List<Vehicle> vehiclesFromDb = Arrays.asList(
                MockVehicleDataHelper.getSearchVehicle("HGV", true),
                MockVehicleDataHelper.getSearchVehicle("PSV", true),
                MockVehicleDataHelper.getSearchVehicle("Trailer", false)
        );

        List<SearchVehicleResponse> mappedVehicles = vehicleResponseMapper.map(vehiclesFromDb);

        assertVehiclesMapped(vehiclesFromDb, mappedVehicles);
    }

    private void assertVehiclesMapped(List<Vehicle> vehicles, List<SearchVehicleResponse> mappedVehicles) {
        assertEquals(vehicles.size(), mappedVehicles.size());

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            assertEquals(SearchVehicleV1Response.class, mappedVehicles.get(i).getClass());
            SearchVehicleV1Response responseVehicle = (SearchVehicleV1Response) mappedVehicles.get(i);

            assertEquals(vehicle.getVehicleIdentifier(), responseVehicle.getRegistration());
            assertEquals(vehicle.getMake(), responseVehicle.getMake());
            assertEquals(vehicle.getModel(), responseVehicle.getModel());
            assertEquals(vehicle.getVehicleType(), responseVehicle.getVehicleType());
            assertEquals(vehicle.getVehicleClass(), responseVehicle.getVehicleClass());
            assertEquals(
                    vehicleResponseMapper.transformDate(vehicle.getRegistrationDate()),
                    responseVehicle.getRegistrationDate()
            );
            assertEquals(
                    vehicleResponseMapper.transformDate(vehicle.getManufactureDate()),
                    responseVehicle.getManufactureDate()
            );
            assertEquals(
                    vehicleResponseMapper.transformDate(vehicle.getTestCertificateExpiryDate()),
                    responseVehicle.getAnnualTestExpiryDate()
            );
            assertAnnualTestMapped(vehicle.getTestHistory(), responseVehicle.getAnnualTests());
        }
    }

    private void assertAnnualTestMapped(List<TestHistory> annualTests, List<AnnualTestResponse> mappedAnnualTests) {
        if (annualTests == null) {
            assertNull(mappedAnnualTests);
            return;
        }
        assertEquals(annualTests.size(), mappedAnnualTests.size());

        for (int i = 0; i < annualTests.size(); i++) {
            TestHistory annualTest = annualTests.get(i);

            assertEquals(AnnualTestV1Response.class, mappedAnnualTests.get(i).getClass());
            AnnualTestV1Response responseTest = (AnnualTestV1Response) mappedAnnualTests.get(i);

            assertEquals(annualTest.getTestType(), responseTest.getTestType());
            assertEquals(
                    vehicleResponseMapper.transformDate(annualTest.getTestDate()),
                    responseTest.getTestDate()
            );
            assertEquals(annualTest.getTestResult(), responseTest.getTestResult());
            assertEquals(annualTest.getTestCertificateSerialNo(), responseTest.getTestCertificateNumber());
            assertEquals(
                    vehicleResponseMapper.transformDate(annualTest.getTestCertificateExpiryDateAtTest()),
                    responseTest.getExpiryDate()
            );
            assertEquals(annualTest.getNumberOfAdvisoryDefectsAtTest().toString(), responseTest.getNumberOfAdvisoryDefectsAtTest());
            assertEquals(annualTest.getNumberOfDefectsAtTest().toString(), responseTest.getNumberOfDefectsAtTest());

            assertDefectsMapped(annualTest.getTestHistoryDefects(), responseTest.getDefects());
        }
    }

    private void assertDefectsMapped(List<Defect> defects, List<DefectResponse> mappedDefects) {
        if (defects == null) {
            assertNull(mappedDefects);
            return;
        }
        assertEquals(defects.size(), mappedDefects.size());

        for (int i = 0; i < defects.size(); i++) {
            Defect defect = defects.get(i);

            assertEquals(DefectV1Response.class, mappedDefects.get(i).getClass());
            DefectV1Response responseDefect = (DefectV1Response) mappedDefects.get(i);

            assertEquals(defect.getFailureItemNo().toString(), responseDefect.getFailureItemNo());
            assertEquals(defect.getFailureReason(), responseDefect.getFailureReason());
            assertEquals(defect.getSeverityCode(), responseDefect.getSeverityCode());
            assertEquals(defect.getSeverityDescription(), responseDefect.getSeverityDescription());
        }
    }
}
