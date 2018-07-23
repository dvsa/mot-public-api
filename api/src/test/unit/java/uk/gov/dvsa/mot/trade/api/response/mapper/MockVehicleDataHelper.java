package uk.gov.dvsa.mot.trade.api.response.mapper;

import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.util.Arrays;

public class MockVehicleDataHelper {
    public static final String DEFICIENCY_CATEGORY_TYPE_PRE_EU = "PE";
    public static final String DEFICIENCY_CATEGORY_TYPE_FAIL = "F";

    public static Vehicle getVehicle(int id) {
        Vehicle vehicle = new Vehicle();

        vehicle.setMotTestNumber("9999" + id);
        vehicle.setPrimaryColour("red");
        vehicle.setDvlaId("12" + id);
        vehicle.setFirstUsedDate("2010-10-10");
        vehicle.setFuelType("diesel");
        vehicle.setId(id);
        vehicle.setMake("ford");
        vehicle.setMakeInFull("ford focus" + id);
        vehicle.setManufactureDate("2009-10-10");
        vehicle.setManufactureYear("2009");
        vehicle.setModel("focus");
        vehicle.setMotTestDueDate("2019-10-10");
        vehicle.setRegistration("FX1" + id);
        vehicle.setSecondaryColour("blue");
        vehicle.setMotTests(Arrays.asList(
                getMotTest(2015, "PASS"),
                getMotTest(2016, "FAIL"),
                getMotTest(2017, "PASS")
        ));

        return vehicle;
    }

    private static MotTest getMotTest(Integer year, String testResult) {
        MotTest motTest = new MotTest();

        motTest.setCompletedDate(year + "-10-10");
        motTest.setExpiryDate((year + 1) + "-10-10");
        motTest.setId(year.longValue());
        motTest.setMotTestNumber("9999" + year);
        motTest.setOdometerResultType("odo");
        motTest.setOdometerUnit("km");
        motTest.setOdometerValue("222" + year);
        motTest.setTestResult(testResult);
        motTest.setVehicleId(1234);
        motTest.setVehicleVersion(1);
        motTest.setRfrAndComments(Arrays.asList(
                getRfr("FAIL", DEFICIENCY_CATEGORY_TYPE_PRE_EU),
                getRfr("FAIL", DEFICIENCY_CATEGORY_TYPE_FAIL),
                getRfr("ADVISORY", DEFICIENCY_CATEGORY_TYPE_PRE_EU),
                getRfr("ADVISORY", DEFICIENCY_CATEGORY_TYPE_FAIL)
        ));

        return motTest;
    }

    private static RfrAndAdvisoryItem getRfr(String type, String deficiencyCode) {
        RfrAndAdvisoryItem rfr = new RfrAndAdvisoryItem();

        rfr.setType(type);
        rfr.setText(deficiencyCode + " text " + type);
        rfr.setDangerous(true);
        rfr.setDeficiencyCategoryCode(deficiencyCode);
        rfr.setDeficiencyCategoryDescription("Major");

        return rfr;
    }
}
