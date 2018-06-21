package uk.gov.dvsa.mot.persist.jdbc;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.persist.ConnectionManager;
import uk.gov.dvsa.mot.persist.TradeReadDao;
import uk.gov.dvsa.mot.persist.jdbc.util.DbQueryRunner;
import uk.gov.dvsa.mot.persist.jdbc.util.DbQueryRunnerImpl;
import uk.gov.dvsa.mot.persist.jdbc.util.ResultSetExtractor;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TradeReadDaoJdbc implements TradeReadDao {
    private static final Logger logger = LogManager.getLogger(MotTestReadDaoJdbc.class);

    private ConnectionManager connectionManager;

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager) {

        this.connectionManager = connectionManager;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByVehicleId(int vehicleId) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetExtractor<List<Vehicle>> mapper = new VehicleResultSetExtractor();

        List<Vehicle> results =
                runner.executeQuery(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_VEHICLE_ID, mapper, vehicleId, vehicleId);

        if (results == null) {
            return Collections.emptyList();
        }

        return results;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByMotTestNumber(long number) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetExtractor<List<Vehicle>> mapper = new VehicleResultSetExtractor();

        List<Vehicle> results =
                runner.executeQuery(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_MOT_TEST_NUMBER, mapper, number, number);

        if (results == null) {
            return Collections.emptyList();
        }

        return results;
    }

    @Override
    @Deprecated
    public List<Vehicle> getVehiclesMotTestsByRegistrationAndMake(String registration, String make) {

        String wildMake = "%" + make + "%";

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetExtractor<List<Vehicle>> mapper = new VehicleResultSetExtractor();

        List<Vehicle> results =
                runner.executeQuery(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_REGISTRATION_AND_MAKE, mapper, registration, wildMake,
                        registration, wildMake);

        if (results == null) {
            return Collections.emptyList();
        }

        return results;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByRegistration(String registration) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetExtractor<List<Vehicle>> mapper = new VehicleResultSetExtractor();

        //passing registration thrice as there are no named params in runner and we need to use registration in three parts of query
        List<Vehicle> results = runner.executeQuery(
                TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_REGISTRATION,
                mapper,
                registration,
                registration,
                registration
        );

        if (results == null) {
            return Collections.emptyList();
        }

        return results;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByDateRange(Date startDate, Date endDate) {

        Timestamp startDateTs = new Timestamp(startDate.getTime());
        Timestamp endDateTs = new Timestamp(endDate.getTime());

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetExtractor<List<Vehicle>> mapper = new VehicleResultSetExtractor();
        
        List<Vehicle> results =
                runner.executeQuery(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_DATE_RANGE, mapper,
                        startDateTs, endDateTs, startDateTs, endDateTs, endDateTs,
                        startDateTs, endDateTs, startDateTs, endDateTs, endDateTs);

        if (results == null) {
            return Collections.emptyList();
        }

        return results;
    }

    @Override
    public List<Vehicle> getVehiclesMotTestsByRange(int startVehicleId, int endVehicleId) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetExtractor<List<Vehicle>> mapper = new VehicleResultSetExtractor();

        List<Vehicle> results = runner.executeQuery(TradeReadSql.QUERY_GET_VEHICLES_MOT_TESTS_BY_RANGE, mapper, startVehicleId,
                endVehicleId, startVehicleId, endVehicleId);

        if (results == null) {
            return Collections.emptyList();
        }

        return results;
    }
}