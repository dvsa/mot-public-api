package uk.gov.dvsa.mot.persist.jdbc;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.mottest.read.core.ConnectionManager;
import uk.gov.dvsa.mot.persist.VehicleReadDao;
import uk.gov.dvsa.mot.persist.model.BodyType;
import uk.gov.dvsa.mot.persist.model.ColourLookup;
import uk.gov.dvsa.mot.persist.model.CountryLookup;
import uk.gov.dvsa.mot.persist.model.CountryOfRegistrationLookup;
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
import uk.gov.dvsa.mot.trade.api.InternalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

        Vehicle vehicle = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehicleById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        vehicle = mapResultSetToVehicle(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return vehicle;
    }

    @Override
    public Vehicle getVehicleByIdAndVersion(int id, int version) {

        Vehicle vehicle;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehicleByIdAndVersion)) {
                stmt.setInt(1, id);
                stmt.setInt(2, version);

                try (ResultSet resultSet = stmt.executeQuery()) {

                    if (resultSet.next()) {
                        vehicle = mapResultSetToVehicle(resultSet);
                    } else {
                        vehicle = getVehicleHistByIdAndVersion(id, version);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicle;
    }

    private Vehicle getVehicleHistByIdAndVersion(int id, int version) {

        Vehicle vehicle = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehicleHistByIdAndVersion)) {
                stmt.setInt(1, id);
                stmt.setInt(2, version);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        vehicle = mapResultSetToVehicle(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicle;
    }

    @Override
    public DvlaVehicle getDvlaVehicleById(int id) {

        DvlaVehicle dvlaVehicle = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetDvlaVehicleById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        dvlaVehicle = mapResultSetToDvlaVehicle(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return dvlaVehicle;
    }

    @Override
    public List<Vehicle> getVehiclesById(int startid, int endid) {

        logger.debug("Entry getVehiclesById " + startid + " - " + endid);
        List<Vehicle> vehicles = new ArrayList<>();

        logger.debug("Prepare getVehiclesById " + startid + " - " + endid);

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehiclesById)) {
                stmt.setInt(1, startid);
                stmt.setInt(2, startid);

                logger.debug("Resultset getVehiclesById " + startid + " - " + endid);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        logger.debug("Mapped getVehiclesById " + vehicle.getId());
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {
            logger.debug("SQLException getVehiclesById " + startid + " - " + endid, e);
            throw new InternalException(e);
        }

        logger.debug("Exit getVehiclesById " + startid + " - " + endid + " found " + vehicles.size());
        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesByPage(int offset, int limit) {

        logger.debug("Entry getVehiclesByPage " + offset + " - " + limit);
        List<Vehicle> vehicles = new ArrayList<>();

        logger.debug("Prepare getVehiclesByPage " + offset + " - " + limit);

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehiclesByPage)) {
                stmt.setInt(1, offset);
                stmt.setInt(2, offset + limit);

                logger.debug("Resultset getVehiclesByPage " + offset + " - " + limit);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        vehicles.add(vehicle);
                        logger.debug("Mapped getVehiclesByPage vehicle " + vehicle.getId());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getVehiclesByPage " + offset + " - " + limit, e);
            throw new InternalException(e);
        }

        logger.debug("Exit getVehiclesByPage " + offset + " - " + limit + " found " + vehicles.size());
        return vehicles;
    }

    @Override
    public Vehicle getVehicleByFullRegAndMake(String registration, String make) {

        Vehicle vehicle = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehicleByFullRegAndMake)) {
                String wildmake = "%" + make + "%";
                stmt.setString(1, registration);
                stmt.setString(2, wildmake);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        vehicle = mapResultSetToVehicle(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicle;
    }

    @Override
    public List<Vehicle> getVehicleByFullRegistration(String registration) {

        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehicleByFullRegistration)) {
                stmt.setString(1, registration);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesByRegistrationOrVin(String registration, String vin) {

        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehiclesByRegistrationOrVin)) {
                stmt.setString(1, registration);
                stmt.setString(2, vin);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber) {

        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt =
                         connection.prepareStatement(VehicleReadSql.queryGetVehiclesByMotTestNumberWithSameRegistrationAndVin)) {
                stmt.setLong(1, motTestNumber);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesByFullRegAndFullVin(String registration, String vin, boolean includeDvla) {

        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehiclesByFullRegAndFullVin)) {
                stmt.setString(1, registration);
                stmt.setString(2, vin);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesByFullRegAndPartialVin(String registration, String vin, boolean includeDvla) {

        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection
                .prepareStatement(VehicleReadSql.queryGetVehiclesByFullRegAndPartialVin)) {
                stmt.setString(1, registration);
                stmt.setString(2, vin);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesByFullRegAndNullVin(String registration, boolean includeDvla) {

        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehiclesByFullRegAndNullVin)) {
                stmt.setString(1, registration);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesByNullRegAndFullVin(String vin, boolean includeDvla) {

        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehiclesByNullRegAndFullVin)) {
                stmt.setString(1, vin);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Vehicle vehicle = mapResultSetToVehicle(resultSet);
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicles;
    }

    @Override
    public Model getModelFromDvlaVehicle(DvlaVehicle vehicle) {

        Model model = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetModelFromDvlaVehicle)) {
                ResultSet resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    model = mapResultSetToModel(resultSet);
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return model;
    }

    @Override
    public List<Make> getMakes() {

        List<Make> makes = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetMakes)) {
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        makes.add(mapResultSetToMake(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return makes;
    }

    @Override
    public ModelDetail getModelDetailById(int id) {

        ModelDetail modelDetail = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetModelDetailById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        modelDetail = mapResultSetToModelDetail(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return modelDetail;
    }

    @Override
    public EmptyReasonMap getEmptyReasonMapByVehicle(Vehicle parent) {

        EmptyReasonMap emptyReasonMap = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetEmptyReasonMapByVehicle)) {
                stmt.setInt(1, parent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        emptyReasonMap = mapResultSetToEmptyReasonMap(parent, resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return emptyReasonMap;
    }

    @Override
    public EmptyVinReasonLookup getEmptyVinReasonLookupById(int id) {

        EmptyVinReasonLookup emptyVinReasonLookup = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetEmptyVinReasonLookupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        emptyVinReasonLookup = mapResultSetToEmptyVinReasonLookup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return emptyVinReasonLookup;
    }

    @Override
    public EmptyVrmReasonLookup getEmptyVrmReasonLookupById(int id) {

        EmptyVrmReasonLookup emptyVrmReasonLookup = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetEmptyVrmReasonLookupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        emptyVrmReasonLookup = mapResultSetToEmptyVrmReasonLookup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return emptyVrmReasonLookup;
    }

    @Override
    public Make getMakeById(int id) {

        Make make = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetMakeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        make = mapResultSetToMake(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return make;
    }

    @Override
    public Model getModelById(int id) {

        Model model = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetModelById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        model = mapResultSetToModel(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return model;
    }

    @Override
    public VehicleClass getVehicleClassById(int id) {

        VehicleClass vehicleClass = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehicleClassById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        vehicleClass = mapResultSetToVehicleClass(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return vehicleClass;
    }

    @Override
    public VehicleClassGroup getVehicleClassGroupById(int id) {

        VehicleClassGroup vehicleClassGroup = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetVehicleClassGroupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        vehicleClassGroup = mapResultSetToVehicleClassGroup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

            throw new InternalException(e);
        }

        return vehicleClassGroup;
    }

    @Override
    public BodyType getBodyTypeById(int id) {

        BodyType bodyType = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetBodyTypeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        bodyType = mapResultSetToBodyType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return bodyType;
    }

    @Override
    public FuelType getFuelTypeById(int id) {

        FuelType fuelType = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetFuelTypeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        fuelType = mapResultSetToFuelType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return fuelType;
    }

    @Override
    public TransmissionType getTransmissionTypeById(int id) {

        TransmissionType transmissionType = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetTransmissionTypeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        transmissionType = mapResultSetToTransmissionType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return transmissionType;
    }

    @Override
    public WheelplanType getWheelplanTypeById(int id) {

        WheelplanType wheelplanType = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetWheelplanTypeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        wheelplanType = mapResultSetToWheelplanType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return wheelplanType;
    }

    @Override
    public ColourLookup getColourLookupById(int id) {

        ColourLookup colourLookup = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetColourLookupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        colourLookup = mapResultSetToColourLookup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return colourLookup;
    }

    @Override
    public WeightSourceLookup getWeightSourceLookupById(int id) {

        WeightSourceLookup weightSourceLookup = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetWeightSourceLookupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        weightSourceLookup = mapResultSetToWeightSourceLookup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return weightSourceLookup;
    }

    @Override
    public CountryOfRegistrationLookup getCountryOfRegistrationLookupById(int id) {

        CountryOfRegistrationLookup countryOfRegistrationLookup = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection
                .prepareStatement(VehicleReadSql.queryGetCountryOfRegistrationLookupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        countryOfRegistrationLookup = mapResultSetToCountryOfRegistrationLookup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return countryOfRegistrationLookup;
    }

    @Override
    public CountryLookup getCountryLookupById(int id) {

        CountryLookup countryLookup = null;

        try {
            Connection connection = connectionManager.getConnection();;

            try (PreparedStatement stmt = connection.prepareStatement(VehicleReadSql.queryGetCountryLookupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        countryLookup = mapResultSetToCountryLookup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {

            throw new InternalException(e);
        }

        return countryLookup;
    }

    private Vehicle mapResultSetToVehicle(ResultSet resultSet) {

        try {
            Vehicle vehicle = new Vehicle();

            vehicle.setId(resultSet.getInt(1));
            vehicle.setRegistration(resultSet.getString(2));
            vehicle.setRegistrationCollapsed(resultSet.getString(3));
            vehicle.setVin(resultSet.getString(4));
            vehicle.setVinCollapsed(resultSet.getString(5));
            vehicle.setModelDetail(getModelDetailById(resultSet.getInt(6)));
            vehicle.setYear(resultSet.getDate(7));
            vehicle.setManufactureDate(resultSet.getDate(8));
            vehicle.setFirstRegistrationDate(resultSet.getDate(9));
            vehicle.setFirstUsedDate(resultSet.getDate(10));
            vehicle.setPrimaryColour(getColourLookupById(resultSet.getInt(11)));
            vehicle.setSecondaryColour(getColourLookupById(resultSet.getInt(12)));
            vehicle.setWeight(resultSet.getInt(13));
            vehicle.setWeightSourceLookup(getWeightSourceLookupById(resultSet.getInt(14)));
            vehicle.setCountryOfRegistrationLookup(getCountryOfRegistrationLookupById(resultSet.getInt(15)));
            vehicle.setEngineNumber(resultSet.getString(16));
            vehicle.setChassisNumber(resultSet.getString(17));
            vehicle.setIsNewAtFirstReg(resultSet.getBoolean(18));
            vehicle.setDvlaVehicleId(resultSet.getInt(19));
            vehicle.setIsDamaged(resultSet.getBoolean(20));
            vehicle.setIsDestroyed(resultSet.getBoolean(21));
            vehicle.setIsIncognito(resultSet.getBoolean(22));
            vehicle.setCreatedBy(resultSet.getInt(23));
            vehicle.setCreatedOn(resultSet.getTimestamp(24));
            vehicle.setLastUpdatedBy(resultSet.getInt(25));
            vehicle.setLastUpdatedOn(resultSet.getTimestamp(26));
            vehicle.setVersion(resultSet.getInt(27));

            vehicle.setEmptyReasonMap(getEmptyReasonMapByVehicle(vehicle));

            return vehicle;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private DvlaVehicle mapResultSetToDvlaVehicle(ResultSet resultSet) {

        DvlaVehicle dvlaVehicle = new DvlaVehicle();

        // TODO

        return dvlaVehicle;
    }

    private Make mapResultSetToMake(ResultSet resultSet) {

        try {
            Make make = new Make();

            make.setId(resultSet.getInt(1));
            make.setName(resultSet.getString(2));
            make.setCode(resultSet.getString(3));
            make.setIsVerified(resultSet.getBoolean(4));
            // make.setIsSelectable( resultSet.getBoolean( 5 ) ) ;
            make.setCreatedBy(resultSet.getInt(5));
            make.setCreatedOn(resultSet.getTimestamp(6));
            make.setLastUpdatedBy(resultSet.getInt(7));
            make.setLastUpdatedOn(resultSet.getTimestamp(8));
            make.setVersion(resultSet.getInt(9));

            return make;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private Model mapResultSetToModel(ResultSet resultSet) {

        try {
            Model model = new Model();

            model.setId(resultSet.getInt(1));
            model.setMake(getMakeById(resultSet.getInt(2)));
            model.setCode(resultSet.getString(3));
            model.setName(resultSet.getString(4));
            model.setIsVerified(resultSet.getBoolean(5));
            model.setCreatedBy(resultSet.getInt(6));
            model.setCreatedOn(resultSet.getTimestamp(7));
            model.setLastUpdatedBy(resultSet.getInt(8));
            model.setLastUpdatedOn(resultSet.getTimestamp(9));
            model.setVersion(resultSet.getInt(10));

            return model;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private BodyType mapResultSetToBodyType(ResultSet resultSet) {

        try {
            BodyType bodyType = new BodyType();

            bodyType.setId(resultSet.getInt(1));
            bodyType.setName(resultSet.getString(2));
            bodyType.setCode(resultSet.getString(3));
            bodyType.setDisplayOrder(resultSet.getInt(4));
            bodyType.setCreatedBy(resultSet.getInt(5));
            bodyType.setCreatedOn(resultSet.getTimestamp(6));
            bodyType.setLastUpdatedBy(resultSet.getInt(7));
            bodyType.setLastUpdatedOn(resultSet.getTimestamp(8));
            bodyType.setVersion(resultSet.getInt(9));

            return bodyType;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private VehicleClass mapResultSetToVehicleClass(ResultSet resultSet) {

        try {
            VehicleClass vehicleClass = new VehicleClass();

            vehicleClass.setId(resultSet.getInt(1));
            vehicleClass.setName(resultSet.getString(2));
            vehicleClass.setCode(resultSet.getString(3));
            vehicleClass.setVehicleClassGroup(getVehicleClassGroupById(resultSet.getInt(4)));
            vehicleClass.setCreatedBy(resultSet.getInt(5));
            vehicleClass.setCreatedOn(resultSet.getTimestamp(6));
            vehicleClass.setLastUpdatedBy(resultSet.getInt(7));
            vehicleClass.setLastUpdatedOn(resultSet.getTimestamp(8));
            vehicleClass.setVersion(resultSet.getInt(9));

            return vehicleClass;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private VehicleClassGroup mapResultSetToVehicleClassGroup(ResultSet resultSet) {

        try {
            VehicleClassGroup vehicleClassGroup = new VehicleClassGroup();

            vehicleClassGroup.setId(resultSet.getInt(1));
            vehicleClassGroup.setName(resultSet.getString(2));
            vehicleClassGroup.setCode(resultSet.getString(3));
            vehicleClassGroup.setCreatedBy(resultSet.getInt(4));
            vehicleClassGroup.setCreatedOn(resultSet.getTimestamp(5));
            vehicleClassGroup.setLastUpdatedBy(resultSet.getInt(6));
            vehicleClassGroup.setLastUpdatedOn(resultSet.getTimestamp(7));
            vehicleClassGroup.setVersion(resultSet.getInt(8));

            return vehicleClassGroup;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private FuelType mapResultSetToFuelType(ResultSet resultSet) {

        try {
            FuelType fuelType = new FuelType();

            fuelType.setId(resultSet.getInt(1));
            fuelType.setName(resultSet.getString(2));
            fuelType.setCode(resultSet.getString(3));
            fuelType.setDvlaPropulsionCode(resultSet.getString(4));
            fuelType.setDisplayOrder(resultSet.getInt(5));
            fuelType.setCreatedBy(resultSet.getInt(6));
            fuelType.setCreatedOn(resultSet.getTimestamp(7));
            fuelType.setLastUpdatedBy(resultSet.getInt(8));
            fuelType.setLastUpdatedOn(resultSet.getTimestamp(9));
            fuelType.setVersion(resultSet.getInt(10));

            return fuelType;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private TransmissionType mapResultSetToTransmissionType(ResultSet resultSet) {

        try {
            TransmissionType transmissionType = new TransmissionType();

            transmissionType.setId(resultSet.getInt(1));
            transmissionType.setName(resultSet.getString(2));
            transmissionType.setCode(resultSet.getString(3));
            transmissionType.setDisplayOrder(resultSet.getInt(4));
            transmissionType.setCreatedBy(resultSet.getInt(5));
            transmissionType.setCreatedOn(resultSet.getTimestamp(6));
            transmissionType.setLastUpdatedBy(resultSet.getInt(7));
            transmissionType.setLastUpdatedOn(resultSet.getTimestamp(8));
            transmissionType.setVersion(resultSet.getInt(9));

            return transmissionType;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private WheelplanType mapResultSetToWheelplanType(ResultSet resultSet) {

        try {
            WheelplanType wheelplanType = new WheelplanType();

            wheelplanType.setId(resultSet.getInt(1));
            wheelplanType.setCode(resultSet.getString(2));
            wheelplanType.setName(resultSet.getString(3));
            wheelplanType.setDescription(resultSet.getString(4));
            wheelplanType.setDisplayOrder(resultSet.getInt(5));
            wheelplanType.setCreatedBy(resultSet.getInt(6));
            wheelplanType.setCreatedOn(resultSet.getTimestamp(7));
            wheelplanType.setLastUpdatedBy(resultSet.getInt(8));
            wheelplanType.setLastUpdatedOn(resultSet.getTimestamp(9));
            wheelplanType.setVersion(resultSet.getInt(10));

            return wheelplanType;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private ModelDetail mapResultSetToModelDetail(ResultSet resultSet) {

        try {
            ModelDetail modelDetail = new ModelDetail();

            modelDetail.setId(resultSet.getInt(1));
            modelDetail.setModel(getModelById(resultSet.getInt(2)));
            modelDetail.setIsVerified(resultSet.getBoolean(3));
            modelDetail.setVehicleClass(getVehicleClassById(resultSet.getInt(4)));
            modelDetail.setBodyType(getBodyTypeById(resultSet.getInt(5)));
            modelDetail.setFuelType(getFuelTypeById(resultSet.getInt(6)));
            modelDetail.setWheelplanType(getWheelplanTypeById(resultSet.getInt(7)));
            modelDetail.setTransmissionType(getTransmissionTypeById(resultSet.getInt(8)));
            modelDetail.setEuClassification(resultSet.getString(9));
            modelDetail.setCylinderCapacity(resultSet.getInt(10));
            modelDetail.setSha1ConcatWsChksum(resultSet.getString(11));
            modelDetail.setCreatedBy(resultSet.getInt(12));
            modelDetail.setCreatedOn(resultSet.getTimestamp(13));
            modelDetail.setLastUpdatedBy(resultSet.getInt(14));
            modelDetail.setLastUpdatedOn(resultSet.getTimestamp(15));
            modelDetail.setVersion(resultSet.getInt(16));

            return modelDetail;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private ColourLookup mapResultSetToColourLookup(ResultSet resultSet) {

        try {
            ColourLookup colourLookup = new ColourLookup();

            colourLookup.setId(resultSet.getInt(1));
            colourLookup.setCode(resultSet.getString(2));
            colourLookup.setName(resultSet.getString(3));
            colourLookup.setDisplayOrder(resultSet.getInt(4));
            colourLookup.setCreatedBy(resultSet.getInt(5));
            colourLookup.setCreatedOn(resultSet.getTimestamp(6));
            colourLookup.setLastUpdatedBy(resultSet.getInt(7));
            colourLookup.setLastUpdatedOn(resultSet.getTimestamp(8));
            colourLookup.setVersion(resultSet.getInt(9));

            return colourLookup;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private WeightSourceLookup mapResultSetToWeightSourceLookup(ResultSet resultSet) {

        try {
            WeightSourceLookup weightSourceLookup = new WeightSourceLookup();

            weightSourceLookup.setId(resultSet.getInt(1));
            weightSourceLookup.setCode(resultSet.getString(2));
            weightSourceLookup.setName(resultSet.getString(3));
            weightSourceLookup.setDescription(resultSet.getString(4));
            weightSourceLookup.setDisplayOrder(resultSet.getInt(5));
            weightSourceLookup.setCreatedBy(resultSet.getInt(6));
            weightSourceLookup.setCreatedOn(resultSet.getTimestamp(7));
            weightSourceLookup.setLastUpdatedBy(resultSet.getInt(8));
            weightSourceLookup.setLastUpdatedOn(resultSet.getTimestamp(9));
            weightSourceLookup.setVersion(resultSet.getInt(10));

            return weightSourceLookup;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private CountryOfRegistrationLookup mapResultSetToCountryOfRegistrationLookup(ResultSet resultSet) {

        try {
            CountryOfRegistrationLookup countryOfRegistrationLookup = new CountryOfRegistrationLookup();

            countryOfRegistrationLookup.setId(resultSet.getInt(1));
            countryOfRegistrationLookup.setCountryLookup(getCountryLookupById(resultSet.getInt(2)));
            countryOfRegistrationLookup.setName(resultSet.getString(3));
            countryOfRegistrationLookup.setCode(resultSet.getString(4));
            countryOfRegistrationLookup.setLicensingCopy(resultSet.getString(5));
            countryOfRegistrationLookup.setCreatedBy(resultSet.getInt(6));
            countryOfRegistrationLookup.setCreatedOn(resultSet.getTimestamp(7));
            countryOfRegistrationLookup.setLastUpdatedBy(resultSet.getInt(8));
            countryOfRegistrationLookup.setLastUpdatedOn(resultSet.getTimestamp(9));
            countryOfRegistrationLookup.setVersion(resultSet.getInt(10));

            return countryOfRegistrationLookup;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private CountryLookup mapResultSetToCountryLookup(ResultSet resultSet) {

        try {
            CountryLookup countryLookup = new CountryLookup();

            countryLookup.setId(resultSet.getInt(1));
            countryLookup.setName(resultSet.getString(2));
            countryLookup.setCode(resultSet.getString(3));
            countryLookup.setIsoCode(resultSet.getString(4));
            countryLookup.setDisplayOrder(resultSet.getInt(5));
            countryLookup.setCreatedBy(resultSet.getInt(6));
            countryLookup.setCreatedOn(resultSet.getTimestamp(7));
            countryLookup.setLastUpdatedBy(resultSet.getInt(8));
            countryLookup.setLastUpdatedOn(resultSet.getTimestamp(9));
            countryLookup.setVersion(resultSet.getInt(10));

            return countryLookup;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private EmptyReasonMap mapResultSetToEmptyReasonMap(Vehicle parent, ResultSet resultSet) {

        try {
            EmptyReasonMap emptyReasonMap = new EmptyReasonMap();

            emptyReasonMap.setId(resultSet.getInt(1));
            emptyReasonMap.setVehicle(parent);
            emptyReasonMap.setEmptyVinReasonLookup(getEmptyVinReasonLookupById(resultSet.getInt(3)));
            emptyReasonMap.setEmptyVrmReasonLookup(getEmptyVrmReasonLookupById(resultSet.getInt(4)));
            emptyReasonMap.setCreatedBy(resultSet.getInt(5));
            emptyReasonMap.setCreatedOn(resultSet.getTimestamp(6));
            emptyReasonMap.setLastUpdatedBy(resultSet.getInt(7));
            emptyReasonMap.setLastUpdatedOn(resultSet.getTimestamp(8));
            emptyReasonMap.setVersion(resultSet.getInt(9));

            return emptyReasonMap;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private EmptyVinReasonLookup mapResultSetToEmptyVinReasonLookup(ResultSet resultSet) {

        try {
            EmptyVinReasonLookup emptyVinReasonLookup = new EmptyVinReasonLookup();

            emptyVinReasonLookup.setId(resultSet.getInt(1));
            emptyVinReasonLookup.setCode(resultSet.getString(2));
            emptyVinReasonLookup.setName(resultSet.getString(3));
            emptyVinReasonLookup.setCreatedBy(resultSet.getInt(4));
            emptyVinReasonLookup.setCreatedOn(resultSet.getTimestamp(5));
            emptyVinReasonLookup.setLastUpdatedBy(resultSet.getInt(6));
            emptyVinReasonLookup.setLastUpdatedOn(resultSet.getTimestamp(7));
            emptyVinReasonLookup.setVersion(resultSet.getInt(8));

            return emptyVinReasonLookup;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }

    private EmptyVrmReasonLookup mapResultSetToEmptyVrmReasonLookup(ResultSet resultSet) {

        try {
            EmptyVrmReasonLookup emptyVrmReasonLookup = new EmptyVrmReasonLookup();

            emptyVrmReasonLookup.setId(resultSet.getInt(1));
            emptyVrmReasonLookup.setCode(resultSet.getString(2));
            emptyVrmReasonLookup.setName(resultSet.getString(3));
            emptyVrmReasonLookup.setCreatedBy(resultSet.getInt(4));
            emptyVrmReasonLookup.setCreatedOn(resultSet.getTimestamp(5));
            emptyVrmReasonLookup.setLastUpdatedBy(resultSet.getInt(6));
            emptyVrmReasonLookup.setLastUpdatedOn(resultSet.getTimestamp(7));
            emptyVrmReasonLookup.setVersion(resultSet.getInt(8));

            return emptyVrmReasonLookup;
        } catch (Exception e) {

            throw new InternalException(e);
        }
    }
}
