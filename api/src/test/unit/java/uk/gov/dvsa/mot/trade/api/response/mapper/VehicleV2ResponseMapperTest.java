package uk.gov.dvsa.mot.trade.api.response.mapper;

import org.junit.Before;
import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.MotTestV2Response;
import uk.gov.dvsa.mot.trade.api.response.RfrAndAdvisoryV2Response;
import uk.gov.dvsa.mot.trade.api.response.VehicleResponse;
import uk.gov.dvsa.mot.trade.api.response.VehicleV2Response;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VehicleV2ResponseMapperTest {

    private VehicleV2ResponseMapper vehicleResponseMapper;

    @Before
    public void init() {
        vehicleResponseMapper = new VehicleV2ResponseMapper();
    }

    @Test
    public void map_mapsAllPropertiesCorrectly() {
        List<Vehicle> vehiclesFromDb = Arrays.asList(
                MockVehicleDataHelper.getVehicle(12),
                MockVehicleDataHelper.getVehicle(33),
                MockVehicleDataHelper.getVehicleWithNoTests(44)
        );

        List<VehicleResponse> mappedVehicles = vehicleResponseMapper.map(vehiclesFromDb);

        assertVehiclesMapped(vehiclesFromDb, mappedVehicles);
    }

    private void assertVehiclesMapped(List<Vehicle> vehicles, List<VehicleResponse> mappedVehicles) {
        assertEquals(vehicles.size(), mappedVehicles.size());

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            assertEquals(VehicleV2Response.class, mappedVehicles.get(i).getClass());
            VehicleV2Response responseVehicle = (VehicleV2Response) mappedVehicles.get(i);

            assertEquals(vehicle.getPrimaryColour(), responseVehicle.getPrimaryColour());
            assertEquals(vehicle.getDvlaId(), responseVehicle.getDvlaId());
            assertEquals(vehicle.getFirstUsedDate(), responseVehicle.getFirstUsedDate());
            assertEquals(vehicle.getFuelType(), responseVehicle.getFuelType());
            assertEquals(vehicle.getMake(), responseVehicle.getMake());
            assertEquals(vehicle.getMakeInFull(), responseVehicle.getMakeInFull());
            assertEquals(vehicle.getManufactureYear(), responseVehicle.getManufactureYear());
            assertEquals(vehicle.getModel(), responseVehicle.getModel());
            assertEquals(vehicle.getMotTestDueDate(), responseVehicle.getMotTestExpiryDate());
            assertEquals(vehicle.getRegistration(), responseVehicle.getRegistration());
            assertEquals(vehicle.getSecondaryColour(), responseVehicle.getSecondaryColour());
            assertMotTestMapped(vehicle.getMotTests(), responseVehicle.getMotTests());
        }
    }

    private void assertMotTestMapped(List<MotTest> motTests, List<MotTestV2Response> mappedMotTests) {
        if (motTests == null) {
            assertNull(mappedMotTests);
            return;
        }
        assertEquals(motTests.size(), mappedMotTests.size());

        for (int i = 0; i < motTests.size(); i++) {
            MotTest test = motTests.get(i);

            assertEquals(MotTestV2Response.class, mappedMotTests.get(i).getClass());
            MotTestV2Response responseTest = mappedMotTests.get(i);

            assertEquals(test.getCompletedDate(), responseTest.getCompletedDate());
            assertEquals(test.getExpiryDate(), responseTest.getExpiryDate());
            assertEquals(test.getMotTestNumber(), responseTest.getMotTestNumber());
            assertEquals(test.getOdometerUnit(), responseTest.getOdometerUnit());
            assertEquals(test.getOdometerValue(), responseTest.getOdometerValue());
            assertEquals(test.getTestResult(), responseTest.getTestResult());
            assertEquals(test.getOdometerResultType(), responseTest.getOdometerResultType());
            assertRfrsMapped(test.getRfrAndComments(), responseTest.getRfrAndComments());
        }
    }

    private void assertRfrsMapped(List<RfrAndAdvisoryItem> rfrs, List<RfrAndAdvisoryV2Response> mappedRfrs) {
        assertEquals(rfrs.size(), mappedRfrs.size());

        for (int i = 0; i < rfrs.size(); i++) {
            RfrAndAdvisoryItem rfr = rfrs.get(i);

            assertEquals(RfrAndAdvisoryV2Response.class, mappedRfrs.get(i).getClass());
            RfrAndAdvisoryV2Response responseRfr = mappedRfrs.get(i);

            assertEquals(rfr.getType(), responseRfr.getType());
            assertEquals(rfr.getText(), responseRfr.getText());
            assertEquals(rfr.isDangerous(), responseRfr.isDangerous());
        }
    }
}
