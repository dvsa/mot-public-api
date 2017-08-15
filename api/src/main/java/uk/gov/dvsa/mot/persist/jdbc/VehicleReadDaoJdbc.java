package uk.gov.dvsa.mot.persist.jdbc;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.mottest.read.core.ConnectionManager;
import uk.gov.dvsa.mot.persist.VehicleReadDao;
import uk.gov.dvsa.mot.persist.jdbc.util.DbQueryRunner;
import uk.gov.dvsa.mot.persist.jdbc.util.DbQueryRunnerImpl;
import uk.gov.dvsa.mot.persist.jdbc.util.ResultSetRowMapper;
import uk.gov.dvsa.mot.persist.model.BodyType;
import uk.gov.dvsa.mot.persist.model.ColourLookup;
import uk.gov.dvsa.mot.persist.model.CountryLookup;
import uk.gov.dvsa.mot.persist.model.CountryOfRegistrationLookup;
import uk.gov.dvsa.mot.persist.model.DvlaMake;
import uk.gov.dvsa.mot.persist.model.DvlaModel;
import uk.gov.dvsa.mot.persist.model.DvlaVehicle;
import uk.gov.dvsa.mot.persist.model.EmptyReasonMap;
import uk.gov.dvsa.mot.persist.model.EmptyVinReasonLookup;
import uk.gov.dvsa.mot.persist.model.EmptyVrmReasonLookup;
import uk.gov.dvsa.mot.persist.model.FuelType;
import uk.gov.dvsa.mot.persist.model.Make;
import uk.gov.dvsa.mot.persist.model.Model;
import uk.gov.dvsa.mot.persist.model.ModelDetail;
import uk.gov.dvsa.mot.persist.model.TransmissionType;
import uk.gov.dvsa.mot.persist.model.Vehicle;
import uk.gov.dvsa.mot.persist.model.VehicleClass;
import uk.gov.dvsa.mot.persist.model.VehicleClassGroup;
import uk.gov.dvsa.mot.persist.model.WeightSourceLookup;
import uk.gov.dvsa.mot.persist.model.WheelplanType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class VehicleReadDaoJdbc implements VehicleReadDao {
    private static final Logger logger = Logger.getLogger(VehicleReadDaoJdbc.class);

    private ConnectionManager connectionManager;

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager) {

        this.connectionManager = connectionManager;
    }

    @Override
    public Vehicle getVehicleById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Vehicle> mapper = new VehicleRowMapper();

        return runner.executeQuery(VehicleReadSql.queryGetVehicleById, mapper, id);
    }

    @Override
    public Vehicle getVehicleByIdAndVersion(int id, int version) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Vehicle> mapper = new VehicleRowMapper();

        Vehicle vehicle = runner.executeQuery(VehicleReadSql.queryGetVehicleByIdAndVersion, mapper, id, version);

        if (vehicle == null) {
            vehicle = runner.executeQuery(VehicleReadSql.queryGetVehicleHistByIdAndVersion, mapper, id, version);
        }

        return vehicle;
    }

    @Override
    public List<Vehicle> getVehiclesById(int startId, int endId) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Vehicle> mapper = new VehicleRowMapper();

        return runner.executeQueryForList(VehicleReadSql.queryGetVehiclesById, mapper, startId, endId);
    }

    @Override
    public List<Vehicle> getVehiclesByPage(int offset, int limit) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Vehicle> mapper = new VehicleRowMapper();

        return runner.executeQueryForList(VehicleReadSql.queryGetVehiclesByPage, mapper, offset, limit);
    }

    @Override
    public Vehicle getVehicleByFullRegAndMake(String registration, String make) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Vehicle> mapper = new VehicleRowMapper();

        return runner.executeQuery(VehicleReadSql.queryGetVehicleByFullRegAndMake, mapper, registration, "%" + make + "%");
    }

    @Override
    public List<Vehicle> getVehicleByFullRegistration(String registration) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Vehicle> mapper = new VehicleRowMapper();

        return runner.executeQueryForList(VehicleReadSql.queryGetVehicleByFullRegistration, mapper, registration);
    }

    @Override
    public List<Vehicle> getVehicleByDvlaVehicleId(Integer dvlaVehicleId) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Vehicle> mapper = new VehicleRowMapper();

        return runner.executeQueryForList(VehicleReadSql.queryGetVehicleByDvlaVehicleId, mapper, dvlaVehicleId);
    }

    @Override
    public List<DvlaVehicle> getDvlaVehicleByFullRegistration(String registration) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<DvlaVehicle> mapper = new DvlaVehicleRowMapper();

        return runner.executeQueryForList(DvlaVehicleReadSql.selectDvlaVehicleByRegistration, mapper, registration);
    }

    @Override
    public List<DvlaVehicle> getDvlaVehicleByDvlaVehicleId(Integer dvlaVehicleId) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<DvlaVehicle> mapper = new DvlaVehicleRowMapper();

        return runner.executeQueryForList(DvlaVehicleReadSql.selectDvlaVehicleById, mapper, dvlaVehicleId);
    }

    @Override
    public List<Vehicle> getVehiclesByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Vehicle> mapper = new VehicleRowMapper();

        return runner.executeQueryForList(VehicleReadSql.queryGetVehiclesByMotTestNumberWithSameRegistrationAndVin, mapper, motTestNumber);
    }

    @Override
    public List<Make> getMakes() {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Make> mapper = new MakeRowMapper();

        return runner.executeQueryForList(VehicleReadSql.queryGetMakes, mapper);
    }

    @Override
    public ModelDetail getModelDetailById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<ModelDetail> mapper = new ModelDetailRowMapper();

        return runner.executeQuery(VehicleReadSql.queryGetModelDetailById, mapper, id);
    }

    @Override
    public DvlaModel getDvlaModelDetailByCode(String modelCode, String makeCode) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<DvlaModel> mapper = new DvlaModelDetailRowMapper();

        return runner.executeQuery(DvlaVehicleReadSql.selectDvlaModelNameByCode, mapper, modelCode, makeCode);
    }

    @Override
    public DvlaMake getDvlaMakeDetailByCode(String code) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<DvlaMake> mapper = new DvlaMakeDetailRowMapper();

        return runner.executeQuery(DvlaVehicleReadSql.selectDvlaMakeNameByCode, mapper, code);
    }

    @Override
    public EmptyReasonMap getEmptyReasonMapByVehicle(Vehicle parent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<EmptyReasonMap> mapper = rs -> {
            EmptyReasonMap emptyReasonMap = new EmptyReasonMap();

            emptyReasonMap.setId(rs.getInt(1));
            emptyReasonMap.setVehicle(parent);
            emptyReasonMap.setEmptyVinReasonLookup(getEmptyVinReasonLookupById(rs.getInt(3)));
            emptyReasonMap.setEmptyVrmReasonLookup(getEmptyVrmReasonLookupById(rs.getInt(4)));
            emptyReasonMap.setCreatedBy(rs.getInt(5));
            emptyReasonMap.setCreatedOn(rs.getTimestamp(6));
            emptyReasonMap.setLastUpdatedBy(rs.getInt(7));
            emptyReasonMap.setLastUpdatedOn(rs.getTimestamp(8));
            emptyReasonMap.setVersion(rs.getInt(9));

            return emptyReasonMap;
        };

        return runner.executeQuery(VehicleReadSql.queryGetEmptyReasonMapByVehicle, mapper, parent.getId());
    }

    @Override
    public EmptyVinReasonLookup getEmptyVinReasonLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<EmptyVinReasonLookup> mapper = rs -> {
            EmptyVinReasonLookup emptyVinReasonLookup = new EmptyVinReasonLookup();

            emptyVinReasonLookup.setId(rs.getInt(1));
            emptyVinReasonLookup.setCode(rs.getString(2));
            emptyVinReasonLookup.setName(rs.getString(3));
            emptyVinReasonLookup.setCreatedBy(rs.getInt(4));
            emptyVinReasonLookup.setCreatedOn(rs.getTimestamp(5));
            emptyVinReasonLookup.setLastUpdatedBy(rs.getInt(6));
            emptyVinReasonLookup.setLastUpdatedOn(rs.getTimestamp(7));
            emptyVinReasonLookup.setVersion(rs.getInt(8));

            return emptyVinReasonLookup;
        };

        return runner.executeQuery(VehicleReadSql.queryGetEmptyVinReasonLookupById, mapper, id);
    }

    @Override
    public EmptyVrmReasonLookup getEmptyVrmReasonLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<EmptyVrmReasonLookup> mapper = rs -> {
            EmptyVrmReasonLookup emptyVrmReasonLookup = new EmptyVrmReasonLookup();

            emptyVrmReasonLookup.setId(rs.getInt(1));
            emptyVrmReasonLookup.setCode(rs.getString(2));
            emptyVrmReasonLookup.setName(rs.getString(3));
            emptyVrmReasonLookup.setCreatedBy(rs.getInt(4));
            emptyVrmReasonLookup.setCreatedOn(rs.getTimestamp(5));
            emptyVrmReasonLookup.setLastUpdatedBy(rs.getInt(6));
            emptyVrmReasonLookup.setLastUpdatedOn(rs.getTimestamp(7));
            emptyVrmReasonLookup.setVersion(rs.getInt(8));

            return emptyVrmReasonLookup;
        };

        return runner.executeQuery(VehicleReadSql.queryGetEmptyVrmReasonLookupById, mapper, id);
    }

    @Override
    public Make getMakeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Make> mapper = new MakeRowMapper();
        return runner.executeQuery(VehicleReadSql.queryGetMakeById, mapper, id);
    }

    @Override
    public Model getModelById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Model> mapper = rs -> {
            Model model = new Model();

            model.setId(rs.getInt(1));
            model.setMake(getMakeById(rs.getInt(2)));
            model.setCode(rs.getString(3));
            model.setName(rs.getString(4));
            model.setIsVerified(rs.getBoolean(5));
            model.setCreatedBy(rs.getInt(6));
            model.setCreatedOn(rs.getTimestamp(7));
            model.setLastUpdatedBy(rs.getInt(8));
            model.setLastUpdatedOn(rs.getTimestamp(9));
            model.setVersion(rs.getInt(10));

            return model;
        };

        return runner.executeQuery(VehicleReadSql.queryGetModelById, mapper, id);
    }

    @Override
    public VehicleClass getVehicleClassById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<VehicleClass> mapper = rs -> {
            VehicleClass vehicleClass = new VehicleClass();

            vehicleClass.setId(rs.getInt(1));
            vehicleClass.setName(rs.getString(2));
            vehicleClass.setCode(rs.getString(3));
            vehicleClass.setVehicleClassGroup(getVehicleClassGroupById(rs.getInt(4)));
            vehicleClass.setCreatedBy(rs.getInt(5));
            vehicleClass.setCreatedOn(rs.getTimestamp(6));
            vehicleClass.setLastUpdatedBy(rs.getInt(7));
            vehicleClass.setLastUpdatedOn(rs.getTimestamp(8));
            vehicleClass.setVersion(rs.getInt(9));

            return vehicleClass;
        };

        return runner.executeQuery(VehicleReadSql.queryGetVehicleClassById, mapper, id);
    }

    @Override
    public VehicleClassGroup getVehicleClassGroupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<VehicleClassGroup> mapper = rs -> {
            VehicleClassGroup vehicleClassGroup = new VehicleClassGroup();

            vehicleClassGroup.setId(rs.getInt(1));
            vehicleClassGroup.setName(rs.getString(2));
            vehicleClassGroup.setCode(rs.getString(3));
            vehicleClassGroup.setCreatedBy(rs.getInt(4));
            vehicleClassGroup.setCreatedOn(rs.getTimestamp(5));
            vehicleClassGroup.setLastUpdatedBy(rs.getInt(6));
            vehicleClassGroup.setLastUpdatedOn(rs.getTimestamp(7));
            vehicleClassGroup.setVersion(rs.getInt(8));

            return vehicleClassGroup;
        };

        return runner.executeQuery(VehicleReadSql.queryGetVehicleClassGroupById, mapper, id);
    }

    @Override
    public BodyType getBodyTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<BodyType> mapper = rs -> {
            BodyType bodyType = new BodyType();

            bodyType.setId(rs.getInt(1));
            bodyType.setName(rs.getString(2));
            bodyType.setCode(rs.getString(3));
            bodyType.setDisplayOrder(rs.getInt(4));
            bodyType.setCreatedBy(rs.getInt(5));
            bodyType.setCreatedOn(rs.getTimestamp(6));
            bodyType.setLastUpdatedBy(rs.getInt(7));
            bodyType.setLastUpdatedOn(rs.getTimestamp(8));
            bodyType.setVersion(rs.getInt(9));

            return bodyType;
        };

        return runner.executeQuery(VehicleReadSql.queryGetBodyTypeById, mapper, id);
    }

    @Override
    public BodyType getBodyTypeByCode(String code) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<BodyType> mapper = new BodyTypeByCodeRowMapper();

        return runner.executeQuery(DvlaVehicleReadSql.selectBodyTypeByCode, mapper, code);
    }

    @Override
    public FuelType getFuelTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<FuelType> mapper = rs -> {
            FuelType fuelType = new FuelType();

            fuelType.setId(rs.getInt(1));
            fuelType.setName(rs.getString(2));
            fuelType.setCode(rs.getString(3));
            fuelType.setDvlaPropulsionCode(rs.getString(4));
            fuelType.setDisplayOrder(rs.getInt(5));
            fuelType.setCreatedBy(rs.getInt(6));
            fuelType.setCreatedOn(rs.getTimestamp(7));
            fuelType.setLastUpdatedBy(rs.getInt(8));
            fuelType.setLastUpdatedOn(rs.getTimestamp(9));
            fuelType.setVersion(rs.getInt(10));

            return fuelType;
        };

        return runner.executeQuery(VehicleReadSql.queryGetFuelTypeById, mapper, id);
    }

    @Override
    public TransmissionType getTransmissionTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<TransmissionType> mapper = rs -> {
            TransmissionType transmissionType = new TransmissionType();

            transmissionType.setId(rs.getInt(1));
            transmissionType.setName(rs.getString(2));
            transmissionType.setCode(rs.getString(3));
            transmissionType.setDisplayOrder(rs.getInt(4));
            transmissionType.setCreatedBy(rs.getInt(5));
            transmissionType.setCreatedOn(rs.getTimestamp(6));
            transmissionType.setLastUpdatedBy(rs.getInt(7));
            transmissionType.setLastUpdatedOn(rs.getTimestamp(8));
            transmissionType.setVersion(rs.getInt(9));

            return transmissionType;
        };

        return runner.executeQuery(VehicleReadSql.queryGetTransmissionTypeById, mapper, id);
    }

    @Override
    public WheelplanType getWheelplanTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<WheelplanType> mapper = rs -> {
            WheelplanType wheelplanType = new WheelplanType();

            wheelplanType.setId(rs.getInt(1));
            wheelplanType.setCode(rs.getString(2));
            wheelplanType.setName(rs.getString(3));
            wheelplanType.setDescription(rs.getString(4));
            wheelplanType.setDisplayOrder(rs.getInt(5));
            wheelplanType.setCreatedBy(rs.getInt(6));
            wheelplanType.setCreatedOn(rs.getTimestamp(7));
            wheelplanType.setLastUpdatedBy(rs.getInt(8));
            wheelplanType.setLastUpdatedOn(rs.getTimestamp(9));
            wheelplanType.setVersion(rs.getInt(10));

            return wheelplanType;
        };

        return runner.executeQuery(VehicleReadSql.queryGetWheelplanTypeById, mapper, id);
    }

    @Override
    public ColourLookup getColourLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<ColourLookup> mapper = rs -> {
            ColourLookup colourLookup = new ColourLookup();

            colourLookup.setId(rs.getInt(1));
            colourLookup.setCode(rs.getString(2));
            colourLookup.setName(rs.getString(3));
            colourLookup.setDisplayOrder(rs.getInt(4));
            colourLookup.setCreatedBy(rs.getInt(5));
            colourLookup.setCreatedOn(rs.getTimestamp(6));
            colourLookup.setLastUpdatedBy(rs.getInt(7));
            colourLookup.setLastUpdatedOn(rs.getTimestamp(8));
            colourLookup.setVersion(rs.getInt(9));

            return colourLookup;
        };

        return runner.executeQuery(VehicleReadSql.queryGetColourLookupById, mapper, id);
    }

    @Override
    public ColourLookup getColourLookupByCode(String code) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<ColourLookup> mapper = new ColourLookupByCodeRowMapper();

        return runner.executeQuery(DvlaVehicleReadSql.selectColourLookupByCode, mapper, code);
    }

    @Override
    public WeightSourceLookup getWeightSourceLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<WeightSourceLookup> mapper = rs -> {
            WeightSourceLookup weightSourceLookup = new WeightSourceLookup();

            weightSourceLookup.setId(rs.getInt(1));
            weightSourceLookup.setCode(rs.getString(2));
            weightSourceLookup.setName(rs.getString(3));
            weightSourceLookup.setDescription(rs.getString(4));
            weightSourceLookup.setDisplayOrder(rs.getInt(5));
            weightSourceLookup.setCreatedBy(rs.getInt(6));
            weightSourceLookup.setCreatedOn(rs.getTimestamp(7));
            weightSourceLookup.setLastUpdatedBy(rs.getInt(8));
            weightSourceLookup.setLastUpdatedOn(rs.getTimestamp(9));
            weightSourceLookup.setVersion(rs.getInt(10));

            return weightSourceLookup;
        };

        return runner.executeQuery(VehicleReadSql.queryGetWeightSourceLookupById, mapper, id);
    }

    @Override
    public CountryOfRegistrationLookup getCountryOfRegistrationLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<CountryOfRegistrationLookup> mapper = rs -> {
            CountryOfRegistrationLookup countryOfRegistrationLookup = new CountryOfRegistrationLookup();

            countryOfRegistrationLookup.setId(rs.getInt(1));
            countryOfRegistrationLookup.setCountryLookup(getCountryLookupById(rs.getInt(2)));
            countryOfRegistrationLookup.setName(rs.getString(3));
            countryOfRegistrationLookup.setCode(rs.getString(4));
            countryOfRegistrationLookup.setLicensingCopy(rs.getString(5));
            countryOfRegistrationLookup.setCreatedBy(rs.getInt(6));
            countryOfRegistrationLookup.setCreatedOn(rs.getTimestamp(7));
            countryOfRegistrationLookup.setLastUpdatedBy(rs.getInt(8));
            countryOfRegistrationLookup.setLastUpdatedOn(rs.getTimestamp(9));
            countryOfRegistrationLookup.setVersion(rs.getInt(10));

            return countryOfRegistrationLookup;
        };

        return runner.executeQuery(VehicleReadSql.queryGetCountryOfRegistrationLookupById, mapper, id);
    }

    @Override
    public CountryLookup getCountryLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<CountryLookup> mapper = rs -> {
            CountryLookup countryLookup = new CountryLookup();

            countryLookup.setId(rs.getInt(1));
            countryLookup.setName(rs.getString(2));
            countryLookup.setCode(rs.getString(3));
            countryLookup.setIsoCode(rs.getString(4));
            countryLookup.setDisplayOrder(rs.getInt(5));
            countryLookup.setCreatedBy(rs.getInt(6));
            countryLookup.setCreatedOn(rs.getTimestamp(7));
            countryLookup.setLastUpdatedBy(rs.getInt(8));
            countryLookup.setLastUpdatedOn(rs.getTimestamp(9));
            countryLookup.setVersion(rs.getInt(10));

            return countryLookup;
        };

        return runner.executeQuery(VehicleReadSql.queryGetCountryLookupById, mapper, id);
    }

    private class VehicleRowMapper implements ResultSetRowMapper<Vehicle> {

        @Override
        public Vehicle mapRow(ResultSet rs) throws SQLException {

            Vehicle vehicle = new Vehicle();

            vehicle.setId(rs.getInt(1));
            vehicle.setRegistration(rs.getString(2));
            vehicle.setRegistrationCollapsed(rs.getString(3));
            vehicle.setVin(rs.getString(4));
            vehicle.setVinCollapsed(rs.getString(5));
            vehicle.setModelDetail(getModelDetailById(rs.getInt(6)));
            vehicle.setYear(rs.getDate(7));
            vehicle.setManufactureDate(rs.getDate(8));
            vehicle.setFirstRegistrationDate(rs.getDate(9));
            vehicle.setFirstUsedDate(rs.getDate(10));
            vehicle.setPrimaryColour(getColourLookupById(rs.getInt(11)));
            vehicle.setSecondaryColour(getColourLookupById(rs.getInt(12)));
            vehicle.setWeight(rs.getInt(13));
            vehicle.setWeightSourceLookup(getWeightSourceLookupById(rs.getInt(14)));
            vehicle.setCountryOfRegistrationLookup(getCountryOfRegistrationLookupById(rs.getInt(15)));
            vehicle.setEngineNumber(rs.getString(16));
            vehicle.setChassisNumber(rs.getString(17));
            vehicle.setIsNewAtFirstReg(rs.getBoolean(18));
            vehicle.setDvlaVehicleId(rs.getInt(19));
            vehicle.setIsDamaged(rs.getBoolean(20));
            vehicle.setIsDestroyed(rs.getBoolean(21));
            vehicle.setIsIncognito(rs.getBoolean(22));
            vehicle.setCreatedBy(rs.getInt(23));
            vehicle.setCreatedOn(rs.getTimestamp(24));
            vehicle.setLastUpdatedBy(rs.getInt(25));
            vehicle.setLastUpdatedOn(rs.getTimestamp(26));
            vehicle.setVersion(rs.getInt(27));

            vehicle.setEmptyReasonMap(getEmptyReasonMapByVehicle(vehicle));

            return vehicle;
        }
    }

    private class DvlaVehicleRowMapper implements ResultSetRowMapper<DvlaVehicle> {

        @Override
        public DvlaVehicle mapRow(ResultSet rs) throws SQLException {

            DvlaVehicle dvlaVehicle = new DvlaVehicle();

            dvlaVehicle.setId(rs.getInt(1));
            dvlaVehicle.setDvlaVehicleId(rs.getInt(2));
            dvlaVehicle.setRegistration(rs.getString(3));

            dvlaVehicle.setModelDetail(getDvlaModelDetailByCode(rs.getString(4), rs.getString(5)));

            dvlaVehicle.setMakeDetail(getDvlaMakeDetailByCode(rs.getString(5)));

            dvlaVehicle.setColour1(getColourLookupByCode(rs.getString(6)));
            if (rs.getString(7) != null) {
                dvlaVehicle.setColour2(getColourLookupByCode(rs.getString(7)));
            } else {
                ColourLookup notStated = new ColourLookup();
                notStated.setCode("W");
                dvlaVehicle.setColour2(notStated);
            }
            dvlaVehicle.setManufactureDate(rs.getDate(8));
            if (rs.getDate(9) != null) {
                dvlaVehicle.setFirstRegistrationDate(rs.getDate(9));
            }
            if (rs.getString(10) != null) {
                dvlaVehicle.setEuClassification(rs.getString(10));
            }
            if (rs.getString(11) != null) {
                dvlaVehicle.setBodyTypeCode(rs.getString(11));
            }
            dvlaVehicle.setLastUpdatedOn(rs.getDate(12));

            return dvlaVehicle;
        }
    }

    private class MakeRowMapper implements ResultSetRowMapper<Make> {

        @Override
        public Make mapRow(ResultSet rs) throws SQLException {

            Make make = new Make();

            make.setId(rs.getInt(1));
            make.setName(rs.getString(2));
            make.setCode(rs.getString(3));
            make.setIsVerified(rs.getBoolean(4));
            // make.setIsSelectable( rs.getBoolean( 5 ) ) ;
            make.setCreatedBy(rs.getInt(5));
            make.setCreatedOn(rs.getTimestamp(6));
            make.setLastUpdatedBy(rs.getInt(7));
            make.setLastUpdatedOn(rs.getTimestamp(8));
            make.setVersion(rs.getInt(9));

            return make;
        }
    }

    private class ModelDetailRowMapper implements ResultSetRowMapper<ModelDetail> {

        @Override
        public ModelDetail mapRow(ResultSet rs) throws SQLException {

            ModelDetail modelDetail = new ModelDetail();

            modelDetail.setId(rs.getInt(1));
            modelDetail.setModel(getModelById(rs.getInt(2)));
            modelDetail.setIsVerified(rs.getBoolean(3));
            modelDetail.setVehicleClass(getVehicleClassById(rs.getInt(4)));
            modelDetail.setBodyType(getBodyTypeById(rs.getInt(5)));
            modelDetail.setFuelType(getFuelTypeById(rs.getInt(6)));
            modelDetail.setWheelplanType(getWheelplanTypeById(rs.getInt(7)));
            modelDetail.setTransmissionType(getTransmissionTypeById(rs.getInt(8)));
            modelDetail.setEuClassification(rs.getString(9));
            modelDetail.setCylinderCapacity(rs.getInt(10));
            modelDetail.setSha1ConcatWsChksum(rs.getString(11));
            modelDetail.setCreatedBy(rs.getInt(12));
            modelDetail.setCreatedOn(rs.getTimestamp(13));
            modelDetail.setLastUpdatedBy(rs.getInt(14));
            modelDetail.setLastUpdatedOn(rs.getTimestamp(15));
            modelDetail.setVersion(rs.getInt(16));

            return modelDetail;
        }
    }

    private class DvlaModelDetailRowMapper implements ResultSetRowMapper<DvlaModel> {

        @Override
        public DvlaModel mapRow(ResultSet rs) throws SQLException {

            DvlaModel dvlaModel = new DvlaModel();

            dvlaModel.setId(rs.getInt(1));
            dvlaModel.setName(rs.getString(2));
            dvlaModel.setCode(rs.getString(3));

            return dvlaModel;
        }
    }

    private class DvlaMakeDetailRowMapper implements ResultSetRowMapper<DvlaMake> {

        @Override
        public DvlaMake mapRow(ResultSet rs) throws SQLException {

            DvlaMake dvlaMake = new DvlaMake();

            dvlaMake.setId(rs.getInt(1));
            dvlaMake.setName(rs.getString(2));
            dvlaMake.setCode(rs.getString(3));

            return dvlaMake;
        }
    }

    private class ColourLookupByCodeRowMapper implements ResultSetRowMapper<ColourLookup> {

        @Override
        public ColourLookup mapRow(ResultSet rs) throws SQLException {

            ColourLookup colourLookup = new ColourLookup();

            colourLookup.setId(rs.getInt(1));
            colourLookup.setName(rs.getString(2));

            return colourLookup;
        }
    }

    private class BodyTypeByCodeRowMapper implements ResultSetRowMapper<BodyType> {

        @Override
        public BodyType mapRow(ResultSet rs) throws SQLException {

            BodyType bodyType = new BodyType();

            bodyType.setId(rs.getInt(1));
            bodyType.setName(rs.getString(2));

            return bodyType;
        }
    }
}
