package uk.gov.dvsa.mot.vehicle.read.core;

import com.google.inject.Inject;

import uk.gov.dvsa.mot.persist.ProvideDbConnection;
import uk.gov.dvsa.mot.persist.VehicleReadDao;
import uk.gov.dvsa.mot.persist.model.DvlaVehicle;
import uk.gov.dvsa.mot.persist.model.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Resource
public class VehicleReadServiceDatabase implements VehicleReadService {
    private final VehicleReadDao vehicleReadDao;

    @Inject
    public VehicleReadServiceDatabase(VehicleReadDao vehicleReadDao) {

        this.vehicleReadDao = vehicleReadDao;
    }

    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.vehicle.api.Vehicle getVehicleById(int id) {

        Vehicle vehicle = vehicleReadDao.getVehicleById(id);

        return mapVehicleSqltoJson(vehicle);
    }

    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.vehicle.api.Vehicle getVehicleByIdAndVersion(int id, int version) {

        Vehicle vehicle = vehicleReadDao.getVehicleByIdAndVersion(id, version);

        return mapVehicleSqltoJson(vehicle);
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.vehicle.api.Vehicle> getVehiclesById(int startId, int endId) {

        List<Vehicle> vehicles = vehicleReadDao.getVehiclesById(startId, endId);

        return mapVehiclesSqltoJson(vehicles);
    }

    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.vehicle.api.Vehicle findByRegistrationAndMake(String registration, String make) {

        Vehicle vehicle = vehicleReadDao.getVehicleByFullRegAndMake(registration, make);

        return mapVehicleSqltoJson(vehicle);
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.vehicle.api.Vehicle> findByRegistration(String registration) {

        List<Vehicle> vehicles = vehicleReadDao.getVehicleByFullRegistration(registration);

        return mapVehiclesSqltoJson(vehicles);
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.vehicle.api.Vehicle> findByDvlaVehicleId(Integer dvlaVehicleId) {

        List<Vehicle> vehicles = vehicleReadDao.getVehicleByDvlaVehicleId(dvlaVehicleId);

        return mapVehiclesSqltoJson(vehicles);
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.trade.api.DvlaVehicle> findDvlaVehicleByRegistration(String registration) {

        List<DvlaVehicle> vehicles = vehicleReadDao.getDvlaVehicleByFullRegistration(registration);

        return mapDvlaVehiclesSqltoJson(vehicles);
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.trade.api.DvlaVehicle> findDvlaVehicleById(Integer dvlaVehicleId) {

        List<DvlaVehicle> vehicles = vehicleReadDao.getDvlaVehicleByDvlaVehicleId(dvlaVehicleId);

        return mapDvlaVehiclesSqltoJson(vehicles);
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.vehicle.api.Vehicle> findByMotTestNumberWithSameRegistrationAndVin(long motTestNumber) {

        List<Vehicle> vehicles = vehicleReadDao.getVehiclesByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        return mapVehiclesSqltoJson(vehicles);
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.vehicle.api.Vehicle> getVehiclesByPage(int offset, int limit) {

        List<Vehicle> vehicles = vehicleReadDao.getVehiclesByPage(offset, limit);

        return mapVehiclesSqltoJson(vehicles);
    }

    @Override
    @ProvideDbConnection
    public List<String> getMakes() {

        return vehicleReadDao.getMakes().stream().map(m -> m.getName()).collect(Collectors.toList());
    }

    private List<uk.gov.dvsa.mot.vehicle.api.Vehicle> mapVehiclesSqltoJson(List<Vehicle> storedVehicles) {

        if (storedVehicles == null) {
            return Arrays.asList();
        }

        List<uk.gov.dvsa.mot.vehicle.api.Vehicle> vehicles = new ArrayList<>();

        for (Vehicle vehicle : storedVehicles) {
            vehicles.add(mapVehicleSqltoJson(vehicle));
        }

        return vehicles;
    }

    private List<uk.gov.dvsa.mot.trade.api.DvlaVehicle> mapDvlaVehiclesSqltoJson(List<DvlaVehicle> storedVehicles) {

        if (storedVehicles == null) {
            return Arrays.asList();
        }

        List<uk.gov.dvsa.mot.trade.api.DvlaVehicle> vehicles = new ArrayList<>();

        for (DvlaVehicle vehicle : storedVehicles) {
            vehicles.add(mapDvlaVehicleSqltoJson(vehicle));
        }

        return vehicles;
    }

    protected uk.gov.dvsa.mot.vehicle.api.Vehicle mapVehicleSqltoJson(Vehicle vehicle) {

        if (vehicle != null) {
            uk.gov.dvsa.mot.vehicle.api.Vehicle jsonVehicle = new uk.gov.dvsa.mot.vehicle.api.Vehicle();

            jsonVehicle.setId(vehicle.getId());
            jsonVehicle.setRegistration(vehicle.getRegistration());
            jsonVehicle.setVin(vehicle.getVin());

            if (vehicle.getCountryOfRegistrationLookup() != null) {
                jsonVehicle.setCountryOfRegistration(vehicle.getCountryOfRegistrationLookup().getName());
            }
            jsonVehicle.setDvlaVehicleId(vehicle.getDvlaVehicleId());
            jsonVehicle.setEngineNumber(vehicle.getEngineNumber());
            jsonVehicle.setChassisNumber(vehicle.getChassisNumber());

            jsonVehicle.setYear(vehicle.getYear());
            jsonVehicle.setFirstRegistrationDate(vehicle.getFirstRegistrationDate());
            jsonVehicle.setFirstUsedDate(vehicle.getFirstUsedDate());
            jsonVehicle.setIsDamaged(vehicle.getIsDamaged());
            jsonVehicle.setIsDestroyed(vehicle.getIsDestroyed());
            jsonVehicle.setIsIncognito(vehicle.getIsIncognito());
            jsonVehicle.setIsNewAtFirstReg(vehicle.getIsNewAtFirstReg());
            jsonVehicle.setManufactureDate(vehicle.getManufactureDate());
            jsonVehicle.setPrimaryColour(vehicle.getPrimaryColour().getName());
            if (vehicle.getSecondaryColour() != null) {
                jsonVehicle.setSecondaryColour(vehicle.getSecondaryColour().getName());
            }
            jsonVehicle.setWeight(vehicle.getWeight());
            if (vehicle.getWeightSourceLookup() != null) {
                jsonVehicle.setWeightSource(vehicle.getWeightSourceLookup().getName());
            }

            if (vehicle.getModelDetail().getModel() != null) {
                jsonVehicle.setModel(vehicle.getModelDetail().getModel().getName());

                if (vehicle.getModelDetail().getModel().getMake() != null) {
                    jsonVehicle.setMake(vehicle.getModelDetail().getModel().getMake().getName());
                }
            }

            if (vehicle.getModelDetail().getVehicleClass() != null) {
                jsonVehicle.setVehicleClass(vehicle.getModelDetail().getVehicleClass().getName());
            }

            if (vehicle.getModelDetail().getTransmissionType() != null) {
                jsonVehicle.setTransmissionType(vehicle.getModelDetail().getTransmissionType().getName());
            }

            if (vehicle.getModelDetail().getBodyType() != null) {
                jsonVehicle.setBodyType(vehicle.getModelDetail().getBodyType().getName());
            }

            if (vehicle.getModelDetail().getFuelType() != null) {
                jsonVehicle.setFuelType(vehicle.getModelDetail().getFuelType().getName());
            }
            jsonVehicle.setCylinderCapacity(vehicle.getModelDetail().getCylinderCapacity());
            jsonVehicle.setEuClassification(vehicle.getModelDetail().getEuClassification());
            if (vehicle.getModelDetail().getWheelplanType() != null) {
                jsonVehicle.setWheelplan(vehicle.getModelDetail().getWheelplanType().getName());
            }

            if (vehicle.getEmptyReasonMap() != null) {
                if (vehicle.getEmptyReasonMap().getEmptyVinReasonLookup() != null) {
                    jsonVehicle.setEmptyVinReason(vehicle.getEmptyReasonMap().getEmptyVinReasonLookup().getName());
                }
                if (vehicle.getEmptyReasonMap().getEmptyVrmReasonLookup() != null) {
                    jsonVehicle.setEmptyVrmReason(vehicle.getEmptyReasonMap().getEmptyVrmReasonLookup().getName());
                }
            }

            jsonVehicle.setCreatedBy(String.valueOf(vehicle.getCreatedBy()));
            jsonVehicle.setCreatedOn(vehicle.getCreatedOn());
            jsonVehicle.setLastUpdatedBy(String.valueOf(vehicle.getLastUpdatedBy()));
            jsonVehicle.setLastUpdatedOn(vehicle.getLastUpdatedOn());
            jsonVehicle.setVersion(vehicle.getVersion());

            return jsonVehicle;
        } else {
            return null;
        }
    }

    protected uk.gov.dvsa.mot.trade.api.DvlaVehicle mapDvlaVehicleSqltoJson(DvlaVehicle vehicle) {

        if (vehicle != null) {
            uk.gov.dvsa.mot.trade.api.DvlaVehicle jsonVehicle = new uk.gov.dvsa.mot.trade.api.DvlaVehicle();

            jsonVehicle.setId(vehicle.getId());
            jsonVehicle.setRegistration(vehicle.getRegistration());

            jsonVehicle.setDvlaVehicleId(vehicle.getDvlaVehicleId());

            jsonVehicle.setFirstRegistrationDate(vehicle.getFirstRegistrationDate());
            jsonVehicle.setManufactureDate(vehicle.getManufactureDate());
            jsonVehicle.setColour1(vehicle.getColour1().getName());
            if (vehicle.getColour2() != null) {
                jsonVehicle.setColour2(vehicle.getColour2().getName());
            }

            if (vehicle.getModelDetail() != null) {
                jsonVehicle.setModelDetail(vehicle.getModelDetail().getName());

                if (vehicle.getMakeDetail() != null) {
                    jsonVehicle.setMakeDetail(vehicle.getMakeDetail().getName());
                }
            }

            if (vehicle.getBodyTypeCode() != null) {
                jsonVehicle.setBodyTypeCode(vehicle.getBodyTypeCode());
            }

            jsonVehicle.setEuClassification(vehicle.getEuClassification());

            jsonVehicle.setLastUpdatedOn(vehicle.getLastUpdatedOn());

            return jsonVehicle;
        } else {
            return null;
        }
    }
}
