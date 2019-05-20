package uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle;

import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.response.mapper.MockVehicleDataHelper;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class SearchVehicleTestTypeMapFilterTest {

    @Test
    public void vehicleTestsTypeNotWhitelistedAreOmitted() {

        Vehicle vehicle = MockVehicleDataHelper.getSearchVehicle("HGV", false);

        assertTrue(SearchVehicleTestTypeMapFilter.canDisplayTest(
                MockVehicleDataHelper.getSearchTest(2000, true, vehicle, "ANNUAL MV")
        ));

        assertTrue(SearchVehicleTestTypeMapFilter.canDisplayTest(
                MockVehicleDataHelper.getSearchTest(2000, true, vehicle, "PAID RETEST")
        ));

        assertFalse(SearchVehicleTestTypeMapFilter.canDisplayTest(
                MockVehicleDataHelper.getSearchTest(2000, true, vehicle, "SOME OTHER TEST")
        ));

        assertFalse(SearchVehicleTestTypeMapFilter.canDisplayTest(
                MockVehicleDataHelper.getSearchTest(2000, true, vehicle, "PAID")
        ));
    }
}