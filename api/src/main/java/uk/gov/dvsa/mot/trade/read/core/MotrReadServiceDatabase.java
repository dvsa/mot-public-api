package uk.gov.dvsa.mot.trade.read.core;

import com.google.inject.Inject;

import uk.gov.dvsa.mot.app.util.CollectionUtils;
import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
import uk.gov.dvsa.mot.persist.ProvideDbConnection;
import uk.gov.dvsa.mot.trade.api.DvlaVehicle;
import uk.gov.dvsa.mot.trade.api.MotrResponse;
import uk.gov.dvsa.mot.trade.service.DvlaVehicleFirstMotDueDateCalculator;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MotrReadServiceDatabase implements MotrReadService {

    private static final SimpleDateFormat SDF_DATE_ISO_8601 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_YEAR = new SimpleDateFormat("yyyy");

    private final MotTestReadService motTestReadService;
    private final VehicleReadService vehicleReadService;

    @Inject
    public MotrReadServiceDatabase(MotTestReadService motTestReadService, VehicleReadService vehicleReadService) {

        this.vehicleReadService = vehicleReadService;
        this.motTestReadService = motTestReadService;
    }

    @Override
    @ProvideDbConnection
    public MotrResponse getLatestMotTestForMotOrDvlaVehicleByRegistration(String registration) {

        List<Vehicle> vehicles = vehicleReadService.findByRegistration(registration);
        MotrReadServiceDatabase.VehicleAndLatestMot vehicleAndLatestMot = getVehicleAndLatestMotTestPass(vehicles);

        if (vehicleAndLatestMot == null || !vehicleAndLatestMot.hasMotTest()) {
            return getDvlaVehicleByRegistration(registration);
        }

        return mapVehicleAndLatestMotToMotrResponse(vehicleAndLatestMot);
    }

    @Override
    @ProvideDbConnection
    public MotrResponse getLatestMotTestByRegistration(String registration) {

        List<Vehicle> vehicles = vehicleReadService.findByRegistration(registration);
        MotrReadServiceDatabase.VehicleAndLatestMot vehicleAndLatestMot = getVehicleAndLatestMotTestPass(vehicles);

        if (vehicleAndLatestMot == null || !vehicleAndLatestMot.hasMotTest()) {
            return null;
        }

        return mapVehicleAndLatestMotToMotrResponse(vehicleAndLatestMot);
    }

    @Override
    @ProvideDbConnection
    public MotrResponse getLatestMotTestForDvlaVehicleByRegistration(String registration) {

        List<DvlaVehicle> vehicles = vehicleReadService.findDvlaVehicleByRegistration(registration);

        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            return null;
        }

        return getLatestDvlaVehicleAndMapToMotrResponse(vehicles);
    }

    @Override
    @ProvideDbConnection
    public MotrResponse getLatestMotTestByDvlaVehicleId(Integer dvlaVehicleId) {

        List<Vehicle> vehicles = vehicleReadService.findByDvlaVehicleId(dvlaVehicleId);
        MotrReadServiceDatabase.VehicleAndLatestMot vehicleAndLatestMot = getVehicleAndLatestMotTestPass(vehicles);

        if (vehicleAndLatestMot == null || !vehicleAndLatestMot.hasMotTest()) {
            return getDvlaVehicleById(dvlaVehicleId);
        }

        return mapVehicleAndLatestMotToMotrResponse(vehicleAndLatestMot);
    }

    @Override
    @ProvideDbConnection
    public MotrResponse getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber) {

        List<Vehicle> vehicles = vehicleReadService.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber);
        MotrReadServiceDatabase.VehicleAndLatestMot vehicleAndLatestMot = getVehicleAndLatestMotTestPass(vehicles);
        if (vehicleAndLatestMot == null) {
            return null;
        }
        return mapVehicleAndLatestMotToMotrResponse(vehicleAndLatestMot);
    }

    private MotrResponse getLatestDvlaVehicleAndMapToMotrResponse(List<DvlaVehicle> vehicles) {
        DvlaVehicle dvlaVehicle = selectMostRecentDvlaVehicle(vehicles);
        MotrResponse motrResponse = mapDvlaVehicleToMotrResponse(dvlaVehicle);

        if (dvlaVehicle.getEuClassification() == null
                || dvlaVehicle.getEuClassification().equals("N2")
                || dvlaVehicle.getEuClassification().equals("N3")) {
            return motrResponse;
        }

        Date firstMotDueDate = DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle);

        if (firstMotDueDate != null) {
            motrResponse.setMotTestExpiryDate(
                    SDF_DATE_ISO_8601.format(firstMotDueDate));
        }

        return motrResponse;
    }

    private VehicleAndLatestMot getVehicleAndLatestMotTestPass(List<Vehicle> vehicles) {

        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            return null;
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

        return new VehicleAndLatestMot(vehicle, motTest);
    }

    private MotrResponse getDvlaVehicleById(Integer dvlaVehicleId) {

        List<DvlaVehicle> vehicles = vehicleReadService.findDvlaVehicleById(dvlaVehicleId);

        return getLatestDvlaVehicleAndMapToTradeVehicle(vehicles);
    }

    private MotrResponse getDvlaVehicleByRegistration(String registration) {

        List<DvlaVehicle> vehicles = vehicleReadService.findDvlaVehicleByRegistration(registration);

        return getLatestDvlaVehicleAndMapToTradeVehicle(vehicles);
    }

    private MotrResponse mapVehicleAndLatestMotToMotrResponse(VehicleAndLatestMot vehicleAndLatestMot) {

        MotrResponse motrResponse = new MotrResponse();
        Vehicle vehicle = vehicleAndLatestMot.getVehicle();
        MotTest motTest = vehicleAndLatestMot.getMotTest();

        if (vehicle != null) {
            motrResponse.setRegistration(vehicle.getRegistration());
            motrResponse.setMake(vehicle.getMake());
            motrResponse.setModel(vehicle.getModel());
            motrResponse.setPrimaryColour(vehicle.getPrimaryColour());

            if (!"Not Stated".equalsIgnoreCase(vehicle.getSecondaryColour())) {
                motrResponse.setSecondaryColour(vehicle.getSecondaryColour());
            }

            if (vehicle.getManufactureDate() != null) {
                motrResponse.setManufactureYear(SDF_YEAR.format(vehicle.getManufactureDate()));
            }
        }

        if (motTest != null) {
            if (motTest.getExpiryDate() != null) {
                motrResponse.setMotTestExpiryDate(SDF_DATE_ISO_8601.format(motTest.getExpiryDate()));
            }
            if (motTest.getNumber() != null) {
                motrResponse.setMotTestNumber(motTest.getNumber().toString());
            }
        }

        return motrResponse;
    }

    private MotrResponse getLatestDvlaVehicleAndMapToTradeVehicle(List<DvlaVehicle> vehicles) {

        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            return null;
        }

        DvlaVehicle dvlaVehicle = selectMostRecentDvlaVehicle(vehicles);
        MotrResponse motrResponse = mapDvlaVehicleToMotrResponse(dvlaVehicle);

        if (dvlaVehicle.getEuClassification() == null
                || dvlaVehicle.getEuClassification().equals("N2")
                || dvlaVehicle.getEuClassification().equals("N3")) {
            return null;
        }

        Date firstMotDueDate = DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle);

        if (firstMotDueDate == null) {
            return null;
        }

        motrResponse.setMotTestExpiryDate(
                SDF_DATE_ISO_8601.format(firstMotDueDate));

        return motrResponse;
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

    private MotrResponse mapDvlaVehicleToMotrResponse(DvlaVehicle dvlaVehicle) {

        MotrResponse motrResponse = new MotrResponse();

        motrResponse.setRegistration(dvlaVehicle.getRegistration());
        motrResponse.setMake(dvlaVehicle.getMakeDetail());
        motrResponse.setModel(dvlaVehicle.getModelDetail());
        motrResponse.setMakeInFull(dvlaVehicle.getMakeInFull());
        motrResponse.setPrimaryColour(dvlaVehicle.getColour1());
        motrResponse.setDvlaId(Integer.toString(dvlaVehicle.getDvlaVehicleId()));

        if (!"Not Stated".equalsIgnoreCase(dvlaVehicle.getColour2())) {
            motrResponse.setSecondaryColour(dvlaVehicle.getColour2());
        }

        if (dvlaVehicle.getManufactureDate() != null) {
            motrResponse.setManufactureYear(SDF_YEAR.format(dvlaVehicle.getManufactureDate()));
        }

        return motrResponse;
    }

    private class VehicleAndLatestMot {

        private Vehicle vehicle;
        private MotTest mot;

        private VehicleAndLatestMot(Vehicle vehicle, MotTest mot) {

            this.vehicle = vehicle;
            this.mot = mot;
        }

        private Vehicle getVehicle() {

            return vehicle;
        }

        private MotTest getMotTest() {

            return mot;
        }

        private boolean hasMotTest() {

            return mot != null;
        }
    }
}
