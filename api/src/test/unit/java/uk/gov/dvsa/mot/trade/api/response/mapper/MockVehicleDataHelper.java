package uk.gov.dvsa.mot.trade.api.response.mapper;

import uk.gov.dvsa.mot.trade.api.MotTest;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.Defect;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.moth.DefectText;
import uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothDefect;
import uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothTestHistory;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothVehicle getMothVehicle(int id) {
        uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothVehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothVehicle();

        vehicle.setRegistration("FX1" + id);
        vehicle.setMakeName("ford");
        vehicle.setModelName("focus");
        vehicle.setManufacturedDate(ZonedDateTime.parse("2009-10-10T00:00:00Z"));
        vehicle.setFuelType("diesel");
        vehicle.setVehicleClassGroupCode("V");
        vehicle.setFirstUsedDate(ZonedDateTime.parse("2010-10-10T00:00:00Z"));
        vehicle.setFirstMotDueDate(ZonedDateTime.parse("2011-10-10T00:00:00Z"));

        vehicle.setMotTestHistory(Arrays.asList(
                getMotMothTest(2015, "PASS", "CVS"),
                getMotMothTest(2016, "FAIL", "CVS"),
                getMotMothTest(2017, "PASS", "CVS"),
                getMotMothTest(2017, "PASS", "MTS")
        ));

        return vehicle;
    }

    public static Vehicle getVehicleWithNoTests(int id) {

        Vehicle vehicle = getVehicle(id);
        vehicle.setMotTests(null);
        vehicle.setMotTestNumber(null);

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

    private static MothTestHistory getMotMothTest(Integer year, String testResult, String origin) {
        MothTestHistory testHistory = new MothTestHistory();
        testHistory.setOrigin(origin);
        testHistory.setCompletedDate(ZonedDateTime.parse(year + "-10-10T00:00:00Z"));
        testHistory.setType("Test Type");
        testHistory.setTestResult(testResult);
        testHistory.setMotTestNumber("9999" + year);
        testHistory.setExpiryDate(ZonedDateTime.parse(year + "-10-10T00:00:00Z"));

        MothDefect mothDefect1 = new MothDefect();
        DefectText defectText1 = new DefectText();
        defectText1.setComment("Test1");
        mothDefect1.setId("1");
        mothDefect1.setType("ADVISORY");
        mothDefect1.setText(defectText1);

        MothDefect mothDefect2 = new MothDefect();
        DefectText defectText2 = new DefectText();
        defectText2.setComment("Test2");
        mothDefect2.setId("2");
        mothDefect2.setType("DANGEROUS");
        mothDefect2.setText(defectText2);

        MothDefect mothDefect3 = new MothDefect();
        DefectText defectText3 = new DefectText();
        defectText3.setComment("Test3");
        mothDefect3.setId("3");
        mothDefect3.setType("ADVISORY");
        mothDefect3.setText(defectText3);

        testHistory.setDefects(Arrays.asList(
                mothDefect1,
                mothDefect2,
                mothDefect3
        ));

        return testHistory;
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

    public static uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle getSearchVehicleContainingSpecificTestType(
            String vehicleType, List<String> testTypes) {

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = getSearchVehicle(vehicleType, true);

        vehicle.getTestHistory().clear();

        for (String testType: testTypes) {
            vehicle.getTestHistory().add(getSearchTest(2000, true, vehicle, testType));
        }

        return vehicle;
    }

    public static uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle getSearchVehicle(String vehicleType, boolean hasTests) {

        uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle = new uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle();

        vehicle.setVehicleIdentifier(vehicleType + "REG123");
        vehicle.setMake("Some" + vehicleType + "Make");
        vehicle.setModel("Some" + vehicleType + "Model");
        vehicle.setManufactureDate("30/12/2003");
        vehicle.setVehicleType(vehicleType);
        vehicle.setVehicleClass("V");
        vehicle.setRegistrationDate("30/06/2003");
        vehicle.setTestCertificateExpiryDate("09/03/2019");

        if (hasTests) {
            List<TestHistory> testHistory = new ArrayList<>();
            testHistory.add(getSearchTest(2010, true, vehicle));
            testHistory.add(getSearchTest(2009, false, vehicle));
            vehicle.setTestHistory(testHistory);
        }

        return vehicle;
    }

    public static TestHistory getSearchTest(int year, boolean passed, uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle, String testType) {
        TestHistory testHistory = getSearchTest(year, passed, vehicle);
        testHistory.setTestType(testType);

        return testHistory;
    }

    public static TestHistory getSearchTest(int year, boolean passed, uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle vehicle) {

        TestHistory testHistory = new TestHistory();

        switch (vehicle.getVehicleType()) {
            case "HGV":
                testHistory.setTestType("ANNUAL MV");
                break;
            case "PSV":
                testHistory.setTestType("ANNUAL PSV LARGE");
                break;
            case "Trailer":
                testHistory.setTestType("ANNUAL TRAILER");
                break;
            default:
                throw new IllegalArgumentException("Invalid vehicle type set: valid types are 'HGV', 'PSV' and 'Trailer'");
        }

        testHistory.setLocation("CHECK SITE 44272, STREET_NAME 44272, TOWN 44272, COUNTRY 44272, XY99 0ZZ");
        testHistory.setTestResult(((passed) ? "Pass" : "Fail"));
        testHistory.setTestDate("01/01/2019");
        testHistory.setTestCertificateExpiryDateAtTest("01/01/2020");
        testHistory.setTestCertificateSerialNo("GG000001");
        testHistory.setVehicleIdentifierAtTest(vehicle.getVehicleIdentifier());

        if (passed) {
            testHistory.setNumberOfAdvisoryDefectsAtTest(0);
            testHistory.setNumberOfDefectsAtTest(0);
        } else {
            testHistory.setNumberOfAdvisoryDefectsAtTest(0);
            testHistory.setNumberOfDefectsAtTest(2);

            List<Defect> defectList = new ArrayList<>();
            defectList.add(getSearchDefect(38, "F"));
            defectList.add(getSearchDefect(63, "A"));
            testHistory.setTestHistoryDefects(defectList);
        }

        return testHistory;
    }

    private static Defect getSearchDefect(int failureItemNo, String severityCode) {

        Defect defect = new Defect();

        defect.setFailureItemNo(failureItemNo);
        if (failureItemNo == 38) {
            defect.setFailureReason("Service Brake Operation");
        } else if (failureItemNo == 63) {
            defect.setFailureReason("Lamps");
        }

        defect.setSeverityCode(severityCode);
        switch (severityCode) {
            case "F":
                defect.setSeverityDescription("Failure");
                break;
            case "A":
                defect.setSeverityDescription("Advisory");
                break;
            case "R":
                defect.setSeverityDescription("Pass after rectification");
                break;
            default:
                throw new IllegalArgumentException("Invalid severity code set: valid codes are 'F', 'A' and 'R'");
        }

        return defect;
    }
}
