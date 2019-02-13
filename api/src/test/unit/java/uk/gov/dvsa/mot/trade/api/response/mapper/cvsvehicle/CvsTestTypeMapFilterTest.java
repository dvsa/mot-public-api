package uk.gov.dvsa.mot.trade.api.response.mapper.cvsvehicle;

import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.response.mapper.MockVehicleDataHelper;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class CvsTestTypeMapFilterTest {

    @Test
    public void vehicleTestsTypeNotWhitelistedAreOmitted() {

        Vehicle vehicle = MockVehicleDataHelper.getCvsVehicle("HGV", false);

        assertTrue(CvsTestTypeMapFilter.canDisplayTest(
                MockVehicleDataHelper.getCvsTest(2000, true, vehicle, "ANNUAL MV")
        ));

        assertTrue(CvsTestTypeMapFilter.canDisplayTest(
                MockVehicleDataHelper.getCvsTest(2000, true, vehicle, "PAID RETEST")
        ));

        assertFalse(CvsTestTypeMapFilter.canDisplayTest(
                MockVehicleDataHelper.getCvsTest(2000, true, vehicle, "SOME OTHER TEST")
        ));

        assertFalse(CvsTestTypeMapFilter.canDisplayTest(
                MockVehicleDataHelper.getCvsTest(2000, true, vehicle, "PAID")
        ));
    }
}