package uk.gov.dvsa.mot.motr.service;

import com.google.inject.Inject;

import uk.gov.dvsa.mot.app.util.CollectionUtils;
import uk.gov.dvsa.mot.motr.model.DvlaVehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.model.MotVehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.model.VehicleWithLatestTest;
import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
import uk.gov.dvsa.mot.persist.ProvideDbConnection;
import uk.gov.dvsa.mot.trade.api.DvlaVehicle;
import uk.gov.dvsa.mot.trade.service.DvlaVehicleFirstMotDueDateCalculator;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MotrReadServiceDatabase implements MotrReadService {


    private final MotTestReadService motTestReadService;
    private final VehicleReadService vehicleReadService;

    @Inject
    public MotrReadServiceDatabase(MotTestReadService motTestReadService, VehicleReadService vehicleReadService) {

        this.vehicleReadService = vehicleReadService;
        this.motTestReadService = motTestReadService;
    }

    @Override
    public Optional<VehicleWithLatestTest> getLatestMotTestByRegistration(String registration) {

        List<Vehicle> vehicles = vehicleReadService.findByRegistration(registration);
        Optional<VehicleWithLatestTest> vehicleAndLatestMot = getVehicleAndLatestMotTestPass(vehicles);

        return vehicleAndLatestMot.filter(VehicleWithLatestTest::hasTest);
    }

    @Override
    @ProvideDbConnection
    public Optional<VehicleWithLatestTest> getLatestMotTestForDvlaVehicleByRegistration(String registration) {
        List<DvlaVehicle> vehicles = vehicleReadService.findDvlaVehicleByRegistration(registration);

        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            return Optional.empty();
        }

        DvlaVehicle dvlaVehicle = selectMostRecentDvlaVehicle(vehicles);
        LocalDate firstMotDueDate = DateConverter.toLocalDate(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle));

        return Optional.of(new DvlaVehicleWithLatestTest(dvlaVehicle, firstMotDueDate));
    }

    @Override
    public Optional<VehicleWithLatestTest> getLatestMotTestByDvlaVehicleId(Integer dvlaVehicleId) {
        List<Vehicle> vehicles = vehicleReadService.findByDvlaVehicleId(dvlaVehicleId);

        Optional<VehicleWithLatestTest> vehicleAndLatestMot = getVehicleAndLatestMotTestPass(vehicles);

        if (vehicleAndLatestMot.isPresent() && vehicleAndLatestMot.get().hasTest()) {
            return vehicleAndLatestMot;
        }

        return getDvlaVehicleById(dvlaVehicleId);

    }

    @Override
    public Optional<VehicleWithLatestTest> getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber) {
        List<Vehicle> vehicles = vehicleReadService.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber);
        return getVehicleAndLatestMotTestPass(vehicles);
    }

    private Optional<VehicleWithLatestTest> getVehicleAndLatestMotTestPass(List<Vehicle> vehicles) {

        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            return Optional.empty();
        }

        Vehicle vehicle = null;
        MotTest motTest = null;

        for (Vehicle v : vehicles) {
            MotTest mt = motTestReadService.getLatestMotTestPassByVehicle(v);

            // If the vehicle or MOT hasn't been set yet
            // or the latest expiryDate is after the current Expiry date
            if ((vehicle == null || motTest == null || motTest.getExpiryDate() == null)
                    || (mt != null && (mt.getExpiryDate() != null && mt.getExpiryDate().after(motTest.getExpiryDate())))) {
                vehicle = v;
                motTest = mt;
            }
        }

        return Optional.of(new MotVehicleWithLatestTest(vehicle, motTest));
    }

    private Optional<VehicleWithLatestTest> getDvlaVehicleById(Integer dvlaVehicleId) {

        List<DvlaVehicle> vehicles = vehicleReadService.findDvlaVehicleById(dvlaVehicleId);

        return getLatestDvlaVehicleWithTestDueDate(vehicles);
    }

    private Optional<VehicleWithLatestTest> getLatestDvlaVehicleWithTestDueDate(List<DvlaVehicle> vehicles) {
        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            return Optional.empty();
        }

        DvlaVehicle dvlaVehicle = selectMostRecentDvlaVehicle(vehicles);

        if (dvlaVehicle.getEuClassification() == null
                || dvlaVehicle.getEuClassification().equals("N2")
                || dvlaVehicle.getEuClassification().equals("N3")) {
            return Optional.empty();
        }

        LocalDate firstMotDueDate = DateConverter.toLocalDate(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle));

        return Optional.of(new DvlaVehicleWithLatestTest(dvlaVehicle, firstMotDueDate));
    }

    // This should be taken care of in sql query not in code - extracted to method and kept for backward compatibility with MOTR query
    private DvlaVehicle selectMostRecentDvlaVehicle(List<DvlaVehicle> vehicles) {

        DvlaVehicle dvlaVehicle = vehicles.get(0);

        if (vehicles.size() > 1 && dvlaVehicle.getLastUpdatedOn() != null) {
            for (DvlaVehicle dvlaVehicle1 : vehicles) {

                if (dvlaVehicle1.getLastUpdatedOn() != null && dvlaVehicle1.getLastUpdatedOn().after(dvlaVehicle.getLastUpdatedOn())) {
                    dvlaVehicle = dvlaVehicle1;
                }
            }
        }

        return dvlaVehicle;
    }
}
