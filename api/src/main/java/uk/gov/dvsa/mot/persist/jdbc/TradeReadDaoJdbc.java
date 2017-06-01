package uk.gov.dvsa.mot.persist.jdbc;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.persist.ConnectionFactory;
import uk.gov.dvsa.mot.persist.TradeReadDao;
import uk.gov.dvsa.mot.trade.api.InternalException;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TradeReadDaoJdbc extends AbstractDaoJdbc implements TradeReadDao {
    private static final Logger logger = Logger.getLogger(MotTestReadDaoJdbc.class);

    @Inject
    public TradeReadDaoJdbc(ConnectionFactory connectionFactory) {

        super(connectionFactory);
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByVehicleId(int vehicleId) {

        logger.debug("Entry getMotHistoryByVehicleId : " + vehicleId);
        List<Vehicle> vehicles;

        logger.debug("Connect getMotHistoryByVehicleId : " + vehicleId);
        try (Connection connection = getConnection()) {
            logger.debug("Prepare getMotHistoryByVehicleId : " + vehicleId);
            try (PreparedStatement stmt = connection
                    .prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_VEHICLE_ID)) {
                stmt.setInt(1, vehicleId);
                stmt.setInt(2, vehicleId);

                logger.debug("Resultset getMotHistoryByVehicleId : " + vehicleId);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    vehicles = ResultSetToVehicleMapper.mapResultSetToVehicle(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getMotHistoryByVehicleId : " + vehicleId, e);
            throw new InternalException(e);
        }

        logger.debug("Exit getMotHistoryByVehicleId : " + vehicleId + " found " + vehicles.size());
        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByMotTestNumber(long number) {

        logger.debug("Entry getVehiclesMotTestsByMotTestNumber : " + number);
        List<Vehicle> vehicles;

        logger.debug("Connect getVehiclesMotTestsByMotTestNumber : " + number);
        try (Connection connection = getConnection()) {
            logger.debug("Prepare getVehiclesMotTestsByMotTestNumber : " + number);
            try (PreparedStatement stmt = connection
                    .prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_MOT_TEST_NUMBER)) {
                stmt.setLong(1, number);
                stmt.setLong(2, number);

                logger.debug("Resultset getVehiclesMotTestsByMotTestNumber : " + number);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    vehicles = ResultSetToVehicleMapper.mapResultSetToVehicle(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getVehiclesMotTestsByMotTestNumber : " + number, e);
            throw new InternalException(e);
        }

        logger.debug("Exit getVehiclesMotTestsByMotTestNumber : " + number + " found " + vehicles.size());
        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByRegistrationAndMake(String registration, String make) {

        logger.debug("Entry getVehiclesMotTestsByRegistrationAndMake : " + registration + " " + make);
        List<Vehicle> vehicles;

        logger.debug("Connect getVehiclesMotTestsByRegistrationAndMake : " + registration + " " + make);
        try (Connection connection = getConnection()) {
            logger.debug("Prepare getVehiclesMotTestsByRegistrationAndMake : " + registration + " " + make);
            try (PreparedStatement stmt = connection
                    .prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_REGISTRATION_AND_MAKE)) {
                String wildmake = "%" + make + "%";
                stmt.setString(1, registration);
                stmt.setString(2, wildmake);
                stmt.setString(3, registration);
                stmt.setString(4, wildmake);

                logger.debug("Resultset getVehiclesMotTestsByRegistrationAndMake : " + registration + " " + make);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    vehicles = ResultSetToVehicleMapper.mapResultSetToVehicle(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getVehiclesMotTestsByRegistrationAndMake : " + registration + " " + make, e);
            throw new InternalException(e);
        }

        logger.debug("Prepare getVehiclesMotTestsByRegistrationAndMake : " + registration + " " + make + " found " + vehicles.size());
        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByDateRange(Date startDate, Date endDate) {

        logger.debug("Entry getMotHistoryByDateRange1 : " + startDate + " - " + endDate);
        List<Vehicle> vehicles;

        logger.debug("Connect getMotHistoryByDateRange1 : " + startDate + " - " + endDate);
        try (Connection connection = getConnection()) {
            logger.debug("Prepare getMotHistoryByDateRange1 : " + startDate + " - " + endDate);
            try (PreparedStatement stmt = connection
                    .prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_DATE_RANGE)) {
                stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
                stmt.setTimestamp(2, new Timestamp(endDate.getTime()));
                stmt.setTimestamp(3, new Timestamp(startDate.getTime()));
                stmt.setTimestamp(4, new Timestamp(endDate.getTime()));
                stmt.setTimestamp(5, new Timestamp(endDate.getTime()));
                stmt.setTimestamp(6, new Timestamp(startDate.getTime()));
                stmt.setTimestamp(7, new Timestamp(endDate.getTime()));
                stmt.setTimestamp(8, new Timestamp(startDate.getTime()));
                stmt.setTimestamp(9, new Timestamp(endDate.getTime()));
                stmt.setTimestamp(10, new Timestamp(endDate.getTime()));

                logger.debug("Resultset getMotHistoryByDateRange1 : " + startDate + " - " + endDate);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    vehicles = ResultSetToVehicleMapper.mapResultSetToVehicle(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getMotHistoryByDateRange1 : " + startDate + " - " + endDate, e);
            throw new InternalException(e);
        }

        logger.debug("Exit getMotHistoryByDateRange1 : " + startDate + " - " + endDate + " found " + vehicles.size());
        return vehicles;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByRange(int startVehicleId, int endVehicleId) {

        logger.debug("Entry getMotHistoryByRange : " + startVehicleId + " - " + endVehicleId);
        List<Vehicle> vehicles;

        logger.debug("Connect getMotHistoryByRange : " + startVehicleId + " - " + endVehicleId);
        try (Connection connection = getConnection()) {
            logger.debug("Prepare getMotHistoryByRange : " + startVehicleId + " - " + endVehicleId);
            try (
                    PreparedStatement stmt = connection.prepareStatement(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_RANGE)) {
                stmt.setInt(1, startVehicleId);
                stmt.setInt(2, endVehicleId);
                stmt.setInt(3, startVehicleId);
                stmt.setInt(4, endVehicleId);

                logger.debug("Resultset getMotHistoryByRange : " + startVehicleId + " - " + endVehicleId);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    vehicles = ResultSetToVehicleMapper.mapResultSetToVehicle(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getMotHistoryByRange : " + startVehicleId + " - " + endVehicleId, e);
            throw new InternalException(e);
        }

        logger.debug(
                "Exit getMotHistoryByRange1 : " + startVehicleId + " - " + endVehicleId + " found " + vehicles.size());
        return vehicles;
    }
}