package uk.gov.dvsa.mot.vehicle.hgv.model.moth;

import uk.gov.dvsa.mot.vehicle.hgv.model.Defect;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MothVehicleMapper {

    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static Vehicle mapFromMothVehicle(MothVehicle mothVehicle) {
        Vehicle vehicle = new Vehicle();

        Optional<MothTestHistory> latestPassedMot = mothVehicle.getMotTestHistory().stream()
                .filter(mothTestHistory -> mothTestHistory.getTestResult().contains("PASS"))
                .max(Comparator.comparing(MothTestHistory::getCompletedDate));

        vehicle.setVehicleIdentifier(mothVehicle.getRegistration());
        vehicle.setMake(mothVehicle.getMakeName());
        vehicle.setModel(mothVehicle.getModelName());
        if (mothVehicle.getManufacturedDate() != null) {
            vehicle.setManufactureDate(mothVehicle.getManufacturedDate().toLocalDate()
                    .format(outputFormatter));
        }
        vehicle.setVehicleType(mothVehicle.getVehicleType());
        vehicle.setVehicleClass(mothVehicle.getVehicleClassGroupCode());

        if (mothVehicle.getFirstUsedDate() != null) {
            vehicle.setRegistrationDate(mothVehicle.getFirstUsedDate().toLocalDate()
                    .format(outputFormatter));
        }

        if (latestPassedMot.isPresent()) {
            vehicle.setTestCertificateExpiryDate(latestPassedMot.get().getExpiryDate() != null
                    ? latestPassedMot.get().getExpiryDate().toLocalDate()
                    .format(outputFormatter) : null);
        } else {
            if (mothVehicle.getFirstMotDueDate() != null) {
                vehicle.setTestCertificateExpiryDate(mothVehicle.getFirstMotDueDate().toLocalDate()
                        .format(outputFormatter));
            }
        }
        
        if (mothVehicle.getMotTestHistory() != null && !mothVehicle.getMotTestHistory().isEmpty()) {
            vehicle.setTestHistory(
                    mothVehicle.getMotTestHistory().stream().map(MothVehicleMapper::apply).collect(Collectors.toList())
            );
        }
        return vehicle;
    }

    private static TestHistory apply(MothTestHistory mothTestHistory) {
        TestHistory testHistory = new TestHistory();
        testHistory.setTestDate(mothTestHistory.getCompletedDate().toLocalDate()
                .format(outputFormatter));
        testHistory.setTestType(mothTestHistory.getType());
        testHistory.setTestResult(mothTestHistory.getTestResult());
        if (isValidString(String.valueOf(mothTestHistory.getMotTestNumber()))) {
            testHistory.setTestCertificateSerialNo(String.valueOf(mothTestHistory.getMotTestNumber()));
        } else {
            testHistory.setTestCertificateSerialNo("Not applicable");
        }
        if (mothTestHistory.getExpiryDate() != null) {
            testHistory.setTestCertificateExpiryDateAtTest(mothTestHistory.getExpiryDate().toLocalDate()
                    .format(outputFormatter));
        }

        if (mothTestHistory.getGarageDetails() != null) {
            testHistory.setLocation(mothTestHistory.getGarageDetails().toString());
        }

        if (mothTestHistory.getDefects() != null) {
            testHistory.setNumberOfDefectsAtTest(mothTestHistory.getDefects().size());
            testHistory.setNumberOfAdvisoryDefectsAtTest((int) mothTestHistory.getDefects().stream()
                    .filter(defect -> Objects.equals(defect.getType(), "ADVISORY"))
                    .count());
            if (!mothTestHistory.getDefects().isEmpty()) {
                testHistory.setTestHistoryDefects(
                        mothTestHistory.getDefects().stream().map(MothVehicleMapper::apply).collect(Collectors.toList())
                );
            }
        }

        return testHistory;
    }

    private static Defect apply(MothDefect mothDefect) {
        Defect defect = new Defect();
        if (mothDefect.getId() != null) {
            defect.setFailureItemNo(Integer.valueOf(mothDefect.getId()));
        } else {
            defect.setFailureItemNo(0);
        }
        defect.setSeverityCode(mothDefect.getDeficiencyCategoryCode());
        defect.setSeverityDescription(mothDefect.getType());

        StringBuilder failureReason = new StringBuilder();
        if (mothDefect.getText() != null) {
            if (isValidString(mothDefect.getText().getLateral())) {
                failureReason.append(mothDefect.getText().getLateral()).append(" ");
            }
            if (isValidString(mothDefect.getText().getLongitudinal())) {
                failureReason.append(mothDefect.getText().getLongitudinal()).append(" ");
            }
            if (isValidString(mothDefect.getText().getVertical())) {
                failureReason.append(mothDefect.getText().getVertical()).append(" ");
            }
            if (mothDefect.getText().getCategory() != null) {
                for (DefectTextCategory defectTextCategory: mothDefect.getText().getCategory()) {
                    if (isValidString(defectTextCategory.getDescription())) {
                        failureReason.append(defectTextCategory.getDescription()).append(" ");
                    }
                }
            }
            if (mothDefect.getText().getDescription() != null) {
                for (DefectTextDescription defectTextDescription: mothDefect.getText().getDescription()) {
                    if (isValidString(defectTextDescription.getName())) {
                        failureReason.append(defectTextDescription.getName()).append(" ");
                    }
                }
            }
            if (isValidString(mothDefect.getText().getComment())) {
                failureReason.append(mothDefect.getText().getComment()).append(" ");
            }
            if (isValidString(mothDefect.getText().getInspectionManualReference())) {
                failureReason.append("(").append(mothDefect.getText().getInspectionManualReference()).append(")");
            }
            defect.setFailureReason(failureReason.toString().trim());
        }

        return defect;
    }

    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty() && !str.toLowerCase().contains("null");
    }
}
