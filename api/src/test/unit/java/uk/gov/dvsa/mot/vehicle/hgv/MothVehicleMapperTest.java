package uk.gov.dvsa.mot.vehicle.hgv;

import org.junit.Test;

import uk.gov.dvsa.mot.helper.FieldCounter;
import uk.gov.dvsa.mot.trade.api.response.mapper.MockVehicleDataHelper;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothTestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothVehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothVehicleMapper;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MothVehicleMapperTest {

    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Test
    public void map_mapsAllPropertiesCorrectly() {
        MothVehicle mockedVehicles = MockVehicleDataHelper.getMothVehicle(14);

        Vehicle mappedVehicles = MothVehicleMapper.mapFromMothVehicle(mockedVehicles);

        assertVehiclesMapped(mockedVehicles, mappedVehicles);
    }

    private void assertVehiclesMapped(MothVehicle vehicle, Vehicle mappedVehicle) {

        int fields = FieldCounter.getNumberOfFieldsFromClass(
                mappedVehicle.getClass().getDeclaredFields(),
                mappedVehicle.getClass().getSuperclass().getDeclaredFields()
        );

        Optional<MothTestHistory> latestPassedMot = vehicle.getMotTestHistory().stream()
                .filter(mothTestHistory -> mothTestHistory.getTestResult().contains("PASS"))
                .max(Comparator.comparing(MothTestHistory::getCompletedDate));

        // Verify if someone didn't add a mapped field to Vehicle response by mistake
        assertEquals(14, fields);

        assertEquals(vehicle.getFirstUsedDate().toLocalDate()
                .format(outputFormatter), mappedVehicle.getRegistrationDate());
        assertEquals(vehicle.getVehicleType(), mappedVehicle.getVehicleType());
        assertEquals(vehicle.getMakeName(), mappedVehicle.getMake());
        assertEquals(vehicle.getManufacturedDate().toLocalDate()
                .format(outputFormatter), mappedVehicle.getManufactureDate());
        assertEquals(vehicle.getModelName(), mappedVehicle.getModel());
        assertEquals(vehicle.getVehicleClassGroupCode(), mappedVehicle.getVehicleClass());
        assertEquals(vehicle.getRegistration(), mappedVehicle.getVehicleIdentifier());

        if (latestPassedMot.isPresent()) {
            assertEquals(latestPassedMot.get().getExpiryDate().toLocalDate()
                    .format(outputFormatter), mappedVehicle.getTestCertificateExpiryDate());
        } else {
            assertEquals(vehicle.getFirstMotDueDate().toLocalDate()
                    .format(outputFormatter), mappedVehicle.getTestCertificateExpiryDate());
        }


        assertMotTestMapped(vehicle.getMotTestHistory(), mappedVehicle.getTestHistory());
    }

    private void assertMotTestMapped(List<MothTestHistory> motTests, List<TestHistory> mappedMotTests) {
        if (motTests == null) {
            assertNull(mappedMotTests);
            return;
        }

        List<MothTestHistory> filteredMotTests = motTests.stream()
                .filter(motTestVehicle -> motTestVehicle.getOrigin()
                .contains("CVS")).collect(Collectors.toList());

        assertEquals(filteredMotTests.size(), mappedMotTests.size());
        for (int i = 0; i < filteredMotTests.size(); i++) {
            MothTestHistory test = filteredMotTests.get(i);

            assertEquals(TestHistory.class, mappedMotTests.get(i).getClass());
            TestHistory responseTest = mappedMotTests.get(i);

            int fields = FieldCounter.getNumberOfFieldsFromClass(
                    responseTest.getClass().getDeclaredFields(),
                    responseTest.getClass().getSuperclass().getDeclaredFields()
            );

            // Verify if someone didn't add/remove a mapped field to TestHistory response by mistake
            assertEquals(11, fields);

            assertEquals(test.getCompletedDate().toLocalDate()
                    .format(outputFormatter), responseTest.getTestDate());
            assertEquals(test.getExpiryDate().toLocalDate()
                    .format(outputFormatter), responseTest.getTestCertificateExpiryDateAtTest());
            assertEquals(test.getMotTestNumber(), responseTest.getTestCertificateSerialNo());
            assertEquals(test.getType(), responseTest.getTestType());
            assertEquals(test.getDefects().size(), (int) responseTest.getNumberOfDefectsAtTest());
            assertEquals(test.getDefects().stream()
                    .filter(defect -> Objects.equals(defect.getType(), "ADVISORY"))
                    .count(), (int) responseTest.getNumberOfAdvisoryDefectsAtTest());
        }
    }
}
