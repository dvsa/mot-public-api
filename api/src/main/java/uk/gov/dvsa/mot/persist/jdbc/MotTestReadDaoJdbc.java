package uk.gov.dvsa.mot.persist.jdbc;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.mottest.read.core.ConnectionManager;
import uk.gov.dvsa.mot.persist.MotTestReadDao;
import uk.gov.dvsa.mot.persist.jdbc.queries.GetLatestMotTestCurrentByVehicleId;
import uk.gov.dvsa.mot.persist.jdbc.queries.GetMotTestCurrentByVehicleId;
import uk.gov.dvsa.mot.persist.jdbc.queries.GetMotTestHistoryByVehicleId;
import uk.gov.dvsa.mot.persist.model.BusinessRule;
import uk.gov.dvsa.mot.persist.model.BusinessRuleType;
import uk.gov.dvsa.mot.persist.model.Comment;
import uk.gov.dvsa.mot.persist.model.EmergencyLog;
import uk.gov.dvsa.mot.persist.model.EmergencyReasonLookup;
import uk.gov.dvsa.mot.persist.model.JasperDocument;
import uk.gov.dvsa.mot.persist.model.LanguageType;
import uk.gov.dvsa.mot.persist.model.MotTest;
import uk.gov.dvsa.mot.persist.model.MotTestAddressComment;
import uk.gov.dvsa.mot.persist.model.MotTestCancelled;
import uk.gov.dvsa.mot.persist.model.MotTestComplaintRef;
import uk.gov.dvsa.mot.persist.model.MotTestEmergencyReason;
import uk.gov.dvsa.mot.persist.model.MotTestReasonForCancelLookup;
import uk.gov.dvsa.mot.persist.model.MotTestRfrLocationType;
import uk.gov.dvsa.mot.persist.model.MotTestRfrMap;
import uk.gov.dvsa.mot.persist.model.MotTestRfrMapComment;
import uk.gov.dvsa.mot.persist.model.MotTestRfrMapCustomDescription;
import uk.gov.dvsa.mot.persist.model.MotTestStatus;
import uk.gov.dvsa.mot.persist.model.MotTestType;
import uk.gov.dvsa.mot.persist.model.Organisation;
import uk.gov.dvsa.mot.persist.model.Person;
import uk.gov.dvsa.mot.persist.model.ReasonForRejection;
import uk.gov.dvsa.mot.persist.model.ReasonForRejectionType;
import uk.gov.dvsa.mot.persist.model.RfrLanguageContentMap;
import uk.gov.dvsa.mot.persist.model.Site;
import uk.gov.dvsa.mot.persist.model.TestItemCategory;
import uk.gov.dvsa.mot.persist.model.TiCategoryLanguageContentMap;
import uk.gov.dvsa.mot.persist.model.WeightSourceLookup;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.InternalException;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MotTestReadDao implementation for JDBC connections to a MySQL database.
 */
@SuppressWarnings("unused")
public class MotTestReadDaoJdbc implements MotTestReadDao {
    private static final Logger logger = Logger.getLogger(MotTestReadDaoJdbc.class);
    private final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy.MM.dd");
    private final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private final Map<Long, MotTest> motTestMap = new HashMap<>();

    private ConnectionManager connectionManager;

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager) {

        this.connectionManager = connectionManager;
    }

    @Override
    public MotTest getMotTestById(long id) {

        logger.debug("Entry getMotTestById " + id);
        if (motTestMap.containsKey(id)) {
            logger.debug("Exit getMotTestById Returning Cached MotTest id " + id);
            return motTestMap.get(id);
        } else {
            MotTest motTest = getMotTestCurrentById(id);

            if (motTest == null) {
                motTest = getMotTestHistoryById(id);
            }

            logger.debug("Exit getMotTestById Returning Noncached MotTest id " + id);
            return motTest;
        }
    }

    @Override
    public MotTest getMotTestCurrentById(long id) {

        logger.debug("Entry getMotTestCurrentById " + id);
        MotTest motTest = null;

        logger.debug("Prepare getMotTestCurrentById " + id);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestCurrentById)) {
                stmt.setLong(1, id);

                logger.debug("Resultset getMotTestCurrentById " + id);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        logger.debug("Map getMotTestCurrentById " + id);
                        motTest = mapResultSetToMotTestCurrent(resultSet);
                        this.motTestMap.put(id, motTest);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        logger.debug("Return getMotTestCurrentById " + id);
        return motTest;
    }

    @Override
    public MotTest getMotTestHistoryById(long id) {

        logger.debug("Entry getMotTestHistoryById " + id);
        MotTest motTest = null;

        logger.debug("Prepare getMotTestHistoryById " + id);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestHistoryById)) {
                stmt.setLong(1, id);

                logger.debug("Resultset getMotTestHistoryById " + id);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        logger.debug("Map getMotTestHistoryById " + id);
                        motTest = mapResultSetToMotTestHistory(resultSet);
                        this.motTestMap.put(id, motTest);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        logger.debug("Return getMotTestHistoryById " + id);
        return motTest;
    }

    @Override
    public MotTest getMotTestByNumber(long number) {

        MotTest motTest = getMotTestCurrentByNumber(number);

        if (motTest == null) {
            motTest = getMotTestHistoryByNumber(number);
        }

        return motTest;
    }

    @Override
    public MotTest getMotTestCurrentByNumber(long number) {

        MotTest motTest = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestCurrentByNumber)) {
                stmt.setLong(1, number);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTest = mapResultSetToMotTestCurrent(resultSet);

                        return motTest;
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTest;
    }

    @Override
    public MotTest getMotTestHistoryByNumber(long number) {

        MotTest motTest = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestHistoryByNumber)) {
                stmt.setLong(1, number);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTest = mapResultSetToMotTestHistory(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTest;
    }

    @Override
    public MotTest getLatestMotTestByVehicleId(int vehicleId) {

        logger.debug("Entry getMotTestsByVehicleId : " + vehicleId);
        MotTest motTest = getLatestMotTestCurrentByVehicleId(vehicleId);

        logger.debug("Exit getMotTestsByVehicleId : " + vehicleId + " found ");
        return motTest;
    }

    @Override
    public List<MotTest> getMotTestsByVehicleId(int vehicleId) {

        logger.debug("Entry getMotTestsByVehicleId : " + vehicleId);
        List<MotTest> motTest = getMotTestCurrentsByVehicleId(vehicleId);
        motTest.addAll(getMotTestHistorysByVehicleId(vehicleId));

        logger.debug("Exit getMotTestsByVehicleId : " + vehicleId + " found " + motTest.size());
        return motTest;
    }

    @Override
    public MotTest getLatestMotTestCurrentByVehicleId(int vehicleId) {

        logger.debug("Entry getMotTestCurrentsByVehicleId : " + vehicleId);

        MotTest motTest = null;

        logger.debug("Prepare getMotTestCurrentsByVehicleId : " + vehicleId);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(new GetLatestMotTestCurrentByVehicleId().buildQuery())) {
                stmt.setInt(1, vehicleId);

                logger.debug("Resultset getMotTestCurrentsByVehicleId : " + vehicleId);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    logger.debug("Map getMotTestCurrentsByVehicleId : " + vehicleId);
                    if (resultSet.next()) {
                        motTest = mapResultSetToMotTestCurrent(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Map getMotTestCurrentsByVehicleId : " + vehicleId, e);
            throw new InternalException(e);
        }

        return motTest;
    }

    @Override
    public List<MotTest> getMotTestCurrentsByVehicleId(int vehicleId) {

        logger.debug("Entry getMotTestCurrentsByVehicleId : " + vehicleId);
        List<MotTest> motTests = new ArrayList<>();

        logger.debug("Prepare getMotTestCurrentsByVehicleId : " + vehicleId);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(new GetMotTestCurrentByVehicleId().buildQuery())) {
                stmt.setInt(1, vehicleId);

                logger.debug("Resultset getMotTestCurrentsByVehicleId : " + vehicleId);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        logger.debug("Map getMotTestCurrentsByVehicleId : " + vehicleId);
                        MotTest motTest = mapResultSetToMotTestCurrent(resultSet);
                        motTests.add(motTest);

                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Map getMotTestCurrentsByVehicleId : " + vehicleId, e);
            throw new InternalException(e);
        }

        return motTests;
    }

    @Override
    public List<MotTest> getMotTestHistorysByVehicleId(int vehicleId) {

        logger.debug("Entry getMotTestHistorysByVehicleId : " + vehicleId);
        List<MotTest> motTests = new ArrayList<>();

        logger.debug("Prepare getMotTestHistorysByVehicleId : " + vehicleId);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(new GetMotTestHistoryByVehicleId().buildQuery())) {
                stmt.setInt(1, vehicleId);

                logger.debug("Resultset getMotTestHistorysByVehicleId : " + vehicleId);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        logger.debug("Map getMotTestHistorysByVehicleId : " + vehicleId);
                        MotTest motTest = mapResultSetToMotTestHistory(resultSet);
                        motTests.add(motTest);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error getMotTestHistorysByVehicleId : " + vehicleId, e);
            throw new InternalException(e);
        }

        return motTests;
    }

    @Override
    public List<MotTest> getMotTestsByDateRange(Date startDate, Date endDate) {

        logger.debug("Entry getMotTestsByDateRange : " + startDate + " - " + endDate);
        List<MotTest> motTests = getMotTestCurrentsByDateRange(startDate, endDate);
        motTests.addAll(getMotTestHistorysByDateRange(startDate, endDate));

        logger.debug("Exit getMotTestsByDateRange : " + startDate + " - " + endDate + " found " + motTests.size());
        return motTests;
    }

    @Override
    public List<MotTest> getMotTestCurrentsByDateRange(Date startDate, Date endDate) {

        logger.debug("Entry getMotTestCurrentsByDateRange : " + startDate + " - " + endDate);
        List<MotTest> motTests = new ArrayList<>();

        logger.debug("Prepare getMotTestCurrentsByDateRange : " + startDate + " - " + endDate);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestCurrentsByDateRange)) {
                stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
                stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

                logger.debug("Resultset getMotTestCurrentsByDateRange : " + startDate + " - " + endDate);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        MotTest motTest = mapResultSetToMotTestCurrent(resultSet);
                        motTests.add(motTest);
                        logger.debug("Mapped getMotTestCurrentsByDateRange : " + motTest.getId());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getMotTestCurrentsByDateRange : " + startDate + " - " + endDate, e);
            throw new InternalException(e);
        }

        logger
                .debug("Exit getMotTestCurrentsByDateRange : " + startDate + " - " + endDate + " found " + motTests.size());
        return motTests;
    }

    @Override
    public List<MotTest> getMotTestHistorysByDateRange(Date startDate, Date endDate) {

        logger.debug("Entry getMotTestHistorysByDateRange : " + startDate + " - " + endDate);
        List<MotTest> motTests = new ArrayList<>();

        logger.debug("Prepare getMotTestHistorysByDateRange : " + startDate + " - " + endDate);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestHistorysByDateRange)) {
                stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
                stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

                logger.debug("Resultset getMotTestHistorysByDateRange : " + startDate + " - " + endDate);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        MotTest motTest = mapResultSetToMotTestHistory(resultSet);
                        motTests.add(motTest);
                        logger.debug("Map getMotTestHistorysByDateRange : " + motTest.getId());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getMotTestHistorysByDateRange : " + startDate + " - " + endDate, e);
            throw new InternalException(e);
        }

        logger
                .debug("Exit getMotTestHistorysByDateRange : " + startDate + " - " + endDate + " found " + motTests.size());
        return motTests;
    }

    @Override
    public List<DisplayMotTestItem> getMotHistoryByDateRange(Date startDate, Date endDate) {

        logger.debug("Entry getMotHistoryByDateRange : " + startDate + " - " + endDate);
        List<DisplayMotTestItem> motHistories = getMotHistoryCurrentByDateRange(startDate, endDate);
        motHistories.addAll(getMotHistoryHistoryByDateRange(startDate, endDate));

        logger.debug("Exit getMotHistoryByDateRange : " + startDate + " - " + endDate + " found " + motHistories.size());
        return motHistories;
    }

    @Override
    public List<DisplayMotTestItem> getMotHistoryCurrentByDateRange(Date startDate, Date endDate) {

        logger.debug("Entry getMotHistoryCurrentByDateRange : " + startDate + " - " + endDate);
        List<DisplayMotTestItem> motHistories;

        logger.debug("Prepare getMotHistoryCurrentByDateRange : " + startDate + " - " + endDate);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.QueryGetMotHistoryCurrentVehicleByDateRange)) {
                stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
                stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

                logger.debug("Resultset getMotHistoryCurrentByDateRange : " + startDate + " - " + endDate);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    motHistories = mapResultSetToDisplayMotTestItem(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getMotHistoryCurrentByDateRange : " + startDate + " - " + endDate, e);
            throw new InternalException(e);
        }

        logger.debug("Exit getMotHistoryCurrentByDateRange : " + startDate + " - " + endDate + " found " + motHistories.size());
        return motHistories;
    }

    @Override
    public List<DisplayMotTestItem> getMotHistoryHistoryByDateRange(Date startDate, Date endDate) {

        logger.debug("Entry getMotHistoryHistoryByDateRange : " + startDate + " - " + endDate);
        List<DisplayMotTestItem> motHistories;

        logger.debug("Prepare getMotHistoryHistoryByDateRange : " + startDate + " - " + endDate);

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.QueryGetMotHistoryHistoryVehicleByDateRange)) {
                stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
                stmt.setTimestamp(2, new Timestamp(endDate.getTime()));

                logger.debug("Resultset getMotHistoryHistoryByDateRange : " + startDate + " - " + endDate);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    motHistories = mapResultSetToDisplayMotTestItem(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException getMotTestHistorysByDateRange : " + startDate + " - " + endDate, e);
            throw new InternalException(e);
        }

        logger.debug("Exit getMotTestHistorysByDateRange : " + startDate + " - " + endDate + " found " + motHistories.size());
        return motHistories;
    }

    @Override
    public List<MotTest> getMotTestsByPage(long offset, long limit) {

        List<MotTest> motTestCurrent = getMotTestCurrentsByPage(offset, limit);
        List<MotTest> motTestHistory = getMotTestHistorysByIdRange(offset, limit);

        motTestCurrent.addAll(motTestHistory);

        return motTestCurrent;
    }

    @Override
    public List<MotTest> getMotTestCurrentsByPage(long offset, long limit) {

        List<MotTest> motTestsCurrent = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestCurrentsByIdRange)) {
                stmt.setLong(1, offset);
                stmt.setLong(2, limit);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        MotTest motTestCurrent = mapResultSetToMotTestCurrent(resultSet);
                        motTestsCurrent.add(motTestCurrent);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestsCurrent;
    }

    @Override
    public List<MotTest> getMotTestHistorysByIdRange(long offset, long limit) {

        List<MotTest> motTestsCurrent = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestHistorysByIdRange)) {
                stmt.setLong(1, offset);
                stmt.setLong(2, limit);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        MotTest motTestCurrent = mapResultSetToMotTestHistory(resultSet);
                        motTestsCurrent.add(motTestCurrent);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestsCurrent;
    }

    @Override
    public MotTestAddressComment getMotTestCurrentAddressCommentByMotTest(MotTest parent) {

        MotTestAddressComment motTestAddressComment = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetMotTestAddressCommentByMotTestId)) {
                stmt.setLong(1, parent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestAddressComment = mapResultSetToMotTestAddressComment(parent, resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestAddressComment;
    }

    @Override
    public MotTestCancelled getMotTestCancelledByMotTest(MotTest parent) {

        MotTestCancelled motTestCancelled = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestCancelledByMotTestId)) {
                stmt.setLong(1, parent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestCancelled = mapResultSetToMotTestCancelled(parent, resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestCancelled;
    }

    @Override
    public MotTestComplaintRef getMotTestComplaintRefByMotTest(MotTest parent) {

        MotTestComplaintRef motTestComplaintRef = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetMotTestComplaintRefByMotTestId)) {
                stmt.setLong(1, parent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestComplaintRef = mapResultSetToMotTestComplaintRef(parent, resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestComplaintRef;
    }

    @Override
    public MotTestEmergencyReason getMotTestEmergencyReasonByMotTest(MotTest parent) {

        MotTestEmergencyReason motTestEmergencyReason = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetMotTestEmergencyReasonByMotTestId)) {
                stmt.setLong(1, parent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestEmergencyReason = mapResultSetToMotTestEmergencyReason(parent, resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestEmergencyReason;
    }

    @Override
    public Comment getCommentById(int id) {

        Comment comment = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetCommentById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        comment = mapResultSetToComment(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return comment;
    }

    @Override
    public MotTestReasonForCancelLookup getMotTestReasonForCancelLookupById(int id) {

        MotTestReasonForCancelLookup motTestReasonForCancelLookup = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetMotTestReasonForCancelLookupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestReasonForCancelLookup = mapResultSetToMotTestReasonForCancelLookup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestReasonForCancelLookup;
    }

    @Override
    public EmergencyLog getEmergencyLogById(int id) {

        EmergencyLog emergencyLog = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetEmergencyLogById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        emergencyLog = mapResultSetToEmergencyLog(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return emergencyLog;
    }

    @Override
    public EmergencyReasonLookup getEmergencyReasonLookupById(int id) {

        EmergencyReasonLookup emergencyReasonLookup = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetEmergencyReasonLookupById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        emergencyReasonLookup = mapResultSetToEmergencyReasonLookup(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return emergencyReasonLookup;
    }

    @Override
    public List<MotTestRfrMap> getMotTestCurrentRfrMapsByMotTest(MotTest motTestCurrent) {

        List<MotTestRfrMap> motTestsCurrentRfrMap = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetMotTestCurrentRfrMapsByMotTestId)) {
                stmt.setLong(1, motTestCurrent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        MotTestRfrMap motTestCurrentRfrMap = mapResultSetToMotTestCurrentRfrMap(motTestCurrent, resultSet);
                        motTestsCurrentRfrMap.add(motTestCurrentRfrMap);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestsCurrentRfrMap;
    }

    @Override
    public List<MotTestRfrMap> getMotTestHistoryRfrMapsByMotTest(MotTest motTestCurrent) {

        List<MotTestRfrMap> motTestsHistoryRfrMap = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetMotTestHistoryRfrMapsByMotTestId)) {
                stmt.setLong(1, motTestCurrent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        MotTestRfrMap motTestCurrentRfrMap = mapResultSetToMotTestCurrentRfrMap(motTestCurrent, resultSet);
                        motTestsHistoryRfrMap.add(motTestCurrentRfrMap);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestsHistoryRfrMap;
    }

    @Override
    public MotTestRfrMapComment getMotTestRfrMapCommentByMotTestRfrMap(MotTestRfrMap parent) {

        MotTestRfrMapComment motTestRfrMapComment = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetMotTestRfrMapCommentByMotTestRfrMapId)) {
                stmt.setLong(1, parent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestRfrMapComment = mapResultSetToMotTestRfrMapComment(parent, resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestRfrMapComment;
    }

    @Override
    public MotTestRfrMapCustomDescription getMotTestRfrMapCustomDescriptionByMotTestRfrMap(MotTestRfrMap parent) {

        MotTestRfrMapCustomDescription motTestRfrMapCustomDescription = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetMotTestRfrMapCustomDescriptionByMotTestRfrMapId)) {
                stmt.setLong(1, parent.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestRfrMapCustomDescription = mapResultSetToMotTestRfrMapCustomDescription(parent, resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestRfrMapCustomDescription;
    }

    @Override
    public ReasonForRejection getReasonForRejectionById(int id) {

        ReasonForRejection reasonForRejection = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetReasonForRejectionById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        reasonForRejection = mapResultSetToReasonForRejection(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return reasonForRejection;
    }

    @Override
    public List<RfrLanguageContentMap> getRfrLanguageContentMapByReasonForRejection(
            ReasonForRejection reasonForRejection) {

        List<RfrLanguageContentMap> rfrLanguageContentMaps = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetRfrLanguageContentMapByReasonForRejection)) {
                stmt.setInt(1, reasonForRejection.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        RfrLanguageContentMap rfrLanguageContentMap = mapResultSetToRfrLanguageContentMap(reasonForRejection,
                                resultSet);
                        rfrLanguageContentMaps.add(rfrLanguageContentMap);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return rfrLanguageContentMaps;
    }

    @Override
    public ReasonForRejectionType getReasonForRejectionTypeById(int id) {

        ReasonForRejectionType reasonForRejectionType = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetReasonForRejectionTypeById)) {

                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        reasonForRejectionType = mapResultSetToReasonForRejectionType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return reasonForRejectionType;
    }

    @Override
    public MotTestRfrLocationType getMotTestRfrLocationTypeById(int id) {

        MotTestRfrLocationType motTestRfrLocationType = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestRfrLocationTypeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestRfrLocationType = mapResultSetToMotTestRfrLocationType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestRfrLocationType;
    }

    @Override
    public TestItemCategory getTestItemCategoryById(int id) {

        TestItemCategory testItemCategory = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetTestItemCategoryById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        testItemCategory = mapResultSetToTestItemCategory(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return testItemCategory;
    }

    @Override
    public List<TiCategoryLanguageContentMap> getTiCategoryLanguageContentMapByTestItemCategory(
            TestItemCategory testItemCategory) {

        List<TiCategoryLanguageContentMap> tiCategoryLanguageContentMaps = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection
                .prepareStatement(MotTestReadSql.queryGetTiCategoryLanguageContentMapByTestItemCategory)) {
                stmt.setInt(1, testItemCategory.getId());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        TiCategoryLanguageContentMap tiCategoryLanguageContentMap = mapResultSetToTiCategoryLanguageContentMap(
                                testItemCategory, resultSet);
                        tiCategoryLanguageContentMaps.add(tiCategoryLanguageContentMap);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return tiCategoryLanguageContentMaps;
    }

    @Override
    public LanguageType getLanguageTypeById(int id) {

        LanguageType languageType = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetLanguageTypeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        languageType = mapResultSetToLanguageType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return languageType;
    }

    @Override
    public BusinessRule getBusinessRuleById(int id) {

        BusinessRule businessRule = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetBusinessRuleById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        businessRule = mapResultSetToBusinessRule(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return businessRule;
    }

    @Override
    public BusinessRuleType getBusinessRuleTypeById(int id) {

        BusinessRuleType businessRuleType = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetBusinessRuleTypeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        businessRuleType = mapResultSetToBusinessRuleType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return businessRuleType;
    }

    @Override
    public Person getPersonById(int id) {

        Person person = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetPersonById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        person = mapResultSetToPerson(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return person;
    }

    @Override
    public Organisation getOrganisationById(int id) {

        Organisation organisation = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetOrganisationById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        organisation = mapResultSetToOrganisation(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return organisation;
    }

    @Override
    public Site getSiteById(int id) {

        Site site = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetSiteById)) {
                stmt.setInt(1, id);

                ResultSet resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    site = mapResultSetToSite(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return site;
    }

    @Override
    public JasperDocument getJasperDocumentById(long id) {

        JasperDocument jasperDocument = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetJasperDocumentById)) {
                stmt.setLong(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        jasperDocument = mapResultSetToJasperDocument(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return jasperDocument;
    }

    @Override
    public MotTestType getMotTestTypeById(int id) {

        MotTestType motTestType = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestTypeById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestType = mapResultSetToMotTestType(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestType;
    }

    @Override
    public MotTestStatus getMotTestStatusById(int id) {

        MotTestStatus motTestStatus = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestStatusById)) {
                stmt.setInt(1, id);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        motTestStatus = mapResultSetToMotTestStatus(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return motTestStatus;
    }

    @Override
    public WeightSourceLookup getWeightSourceLookupById(int id) {

        WeightSourceLookup weightSourceLookup = null;

        try {
            Connection connection = connectionManager.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(MotTestReadSql.queryGetMotTestTypeById)) {
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
    public ReasonForRejectionType getReasonForRejectionType(String name) {

        String szQuery = "select r from ReasonForRejectionType r where r.name = :name ";

        // TODO

        return null;
    }

    @Override
    public MotTestStatus getMotTestStatus(String name) {

        String szQuery = "select s from MotTestStatus s where s.name = :name ";

        // TODO

        return null;
    }

    @Override
    public MotTestType getMotTestType(String name) {

        String szQuery = "select t from MotTestType t where t.name = :name ";

        // TODO

        return null;
    }

    @Override
    public WeightSourceLookup getWeightSourceLookup(String name) {

        String szQuery = "select w from WeightSourceLookup w where w.name = :name ";

        // TODO

        return null;
    }

    @Override
    public MotTestReasonForCancelLookup getMotTestReasonForCancelLookup(String name) {

        String szQuery = "select r from MotTestReasonForCancelLookup r where r.name = :name ";

        // TODO

        return null;
    }

    @Override
    public EmergencyReasonLookup getEmergencyReasonLookup(String name) {

        String szQuery = "select e from EmergencyReasonLookup e where e.name = :name ";

        // TODO

        return null;
    }

    @Override
    public MotTestRfrLocationType getMotTestRfrLocationType(String lateral, String longitudinal, String vertical) {

        String szQuery = "select l from MotTestRfrLocationType l where l.lateral = :lateral and l.longitudinal = :longitudinal and l" +
                ".vertical = :vertical ";

        // TODO

        return null;
    }

    @Override
    public ReasonForRejection getReasonForRejection(String type, String text) {

        String szQuery = null;

        if ("ADVISORY".equals(type)) {
            szQuery = "select l.reasonForRejection " + "from   RfrLanguageContentMap l " + "where  advisoryText = :text " +
                    "and    languageType.code = :language ";
        } else {
            szQuery = "select l.reasonForRejection " + "from   RfrLanguageContentMap l " + "where  name = :text " +
                    "and    languageType.code = :language ";
        }

        // TODO

        return null;
    }

    /**
     * Convert a ResultSet to an object representing an MOT test in a displayable
     * manner.
     *
     * @param resultSet the ResultSet with the right columns in from an MOT test query
     * @return A list of MOTs suitable for display, one for each row of the result set which has a unique test ID. If multiple MOT tests
     * have the same ID in the result set, only the first is returned.
     */
    private List<DisplayMotTestItem> mapResultSetToDisplayMotTestItem(ResultSet resultSet) {

        List<DisplayMotTestItem> displayMotTestItems = new ArrayList<>();

        DisplayMotTestItem displayMotTestItem;
        List<RfrAndAdvisoryItem> rfrAndAdvisoryItems = null;

        long motTestId = 0;

        try {
            while (resultSet.next()) {
                // vehilce.id not used
                long currMotTestId = resultSet.getLong(2);
                long currMotTestRfrMapId = resultSet.getLong(3);

                if (currMotTestId != motTestId) {
                    motTestId = currMotTestId;
                    displayMotTestItem = new DisplayMotTestItem();
                    rfrAndAdvisoryItems = new ArrayList<>();
                    displayMotTestItem.setRfrAndComments(rfrAndAdvisoryItems);
                    displayMotTestItems.add(displayMotTestItem);

                    displayMotTestItem.setRegistration(resultSet.getString(4));
                    displayMotTestItem.setMakeName(resultSet.getString(5));
                    displayMotTestItem.setModelName(resultSet.getString(6));
                    if (resultSet.getDate(7) != null) {
                        displayMotTestItem.setFirstUsedDate(sdfDate.format(resultSet.getTimestamp(7)));
                    }
                    displayMotTestItem.setFuelType(resultSet.getString(8));
                    displayMotTestItem.setPrimaryColour(resultSet.getString(9));
                    // secondary colour not used
                    // started date not used
                    if (resultSet.getDate(12) != null) {
                        displayMotTestItem.setCompletedDate(sdfDateTime.format(resultSet.getTimestamp(12)));
                    }
                    displayMotTestItem.setTestResult(resultSet.getString(13));
                    if (resultSet.getDate(14) != null) {
                        displayMotTestItem.setExpiryDate(sdfDate.format(resultSet.getTimestamp(14)));
                    }
                    displayMotTestItem.setOdometerValue(String.valueOf(resultSet.getInt(15)));
                    displayMotTestItem.setOdometerUnit(resultSet.getString(16));
                    // odometer result type not used
                    displayMotTestItem.setMotTestNumber(String.valueOf(resultSet.getBigDecimal(18)));
                }

                if (currMotTestRfrMapId != 0) {
                    RfrAndAdvisoryItem rfrAndAdvisoryItem = new RfrAndAdvisoryItem();
                    rfrAndAdvisoryItem.setType(resultSet.getString(19));
                    rfrAndAdvisoryItem.setText(resultSet.getString(20));
                    rfrAndAdvisoryItems.add(rfrAndAdvisoryItem);
                }
            }

            return displayMotTestItems;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTest mapResultSetToMotTestCurrent(ResultSet resultSet) {

        try {
            MotTest motTestCurrent = new MotTest();

            motTestCurrent.setId(resultSet.getLong(1));
            motTestCurrent.setPerson(getPersonById(resultSet.getInt(2)));
            motTestCurrent.setVehicleId(resultSet.getInt(3));
            motTestCurrent.setVehicleVersion(resultSet.getInt(4));
            motTestCurrent.setDocument(getJasperDocumentById(resultSet.getLong(5)));
            motTestCurrent.setOrganisation(getOrganisationById(resultSet.getInt(6)));
            motTestCurrent.setSite(getSiteById(resultSet.getInt(7)));
            motTestCurrent.setHasRegistration(resultSet.getBoolean(8));
            motTestCurrent.setMotTestType(getMotTestTypeById(resultSet.getInt(9)));
            motTestCurrent.setStartedDate(resultSet.getDate(10));
            motTestCurrent.setCompletedDate(resultSet.getDate(11));
            motTestCurrent.setSubmittedDate(resultSet.getDate(12));
            motTestCurrent.setMotTestStatus(getMotTestStatusById(resultSet.getInt(13)));
            motTestCurrent.setIssuedDate(resultSet.getDate(14));
            motTestCurrent.setExpiryDate(resultSet.getDate(15));
            // TODO causing recursive gets because of stupid dual linking
            // motTestCurrent.setMotTestOriginal( getMotTestCurrentById(
            // resultSet.getLong( 16 ) ) );
            // motTestCurrent.setMotTestPrs( getMotTestCurrentById( resultSet.getLong(
            // 17 ) ) );
            motTestCurrent.setNumber(resultSet.getBigDecimal(18));
            motTestCurrent.setWeightSourceLookup(getWeightSourceLookupById(resultSet.getInt(19)));
            motTestCurrent.setVehicleWeight(resultSet.getInt(20));
            motTestCurrent.setOdometerReadingValue(resultSet.getInt(21));
            motTestCurrent.setOdometerReadingUnit(resultSet.getString(22));
            motTestCurrent.setOdometerReadingType(resultSet.getString(23));
            motTestCurrent.setCreatedBy(resultSet.getInt(24));
            motTestCurrent.setCreatedOn(resultSet.getTimestamp(25));
            motTestCurrent.setLastUpdatedBy(resultSet.getInt(26));
            motTestCurrent.setLastUpdatedOn(resultSet.getTimestamp(27));
            motTestCurrent.setVersion(resultSet.getInt(28));
            motTestCurrent.setClientIp(resultSet.getString(29));

            motTestCurrent.setMotTestAddressComment(getMotTestCurrentAddressCommentByMotTest(motTestCurrent));
            motTestCurrent.setMotTestCancelled(getMotTestCancelledByMotTest(motTestCurrent));
            motTestCurrent.setMotTestComplaintRef(getMotTestComplaintRefByMotTest(motTestCurrent));
            motTestCurrent.setMotTestEmergencyReason(getMotTestEmergencyReasonByMotTest(motTestCurrent));

            List<MotTestRfrMap> motTestCurrentRfrMaps = getMotTestCurrentRfrMapsByMotTest(motTestCurrent);
            motTestCurrent.setMotTestCurrentRfrMaps(motTestCurrentRfrMaps);

            return motTestCurrent;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTest mapResultSetToMotTestHistory(ResultSet resultSet) {

        try {
            MotTest motTestCurrent = new MotTest();

            motTestCurrent.setId(resultSet.getLong(1));
            motTestCurrent.setPerson(getPersonById(resultSet.getInt(2)));
            motTestCurrent.setVehicleId(resultSet.getInt(3));
            motTestCurrent.setVehicleVersion(resultSet.getInt(4));
            motTestCurrent.setDocument(getJasperDocumentById(resultSet.getLong(5)));
            motTestCurrent.setOrganisation(getOrganisationById(resultSet.getInt(6)));
            motTestCurrent.setSite(getSiteById(resultSet.getInt(7)));
            motTestCurrent.setHasRegistration(resultSet.getBoolean(8));
            motTestCurrent.setMotTestType(getMotTestTypeById(resultSet.getInt(9)));
            motTestCurrent.setStartedDate(resultSet.getDate(10));
            motTestCurrent.setCompletedDate(resultSet.getDate(11));
            motTestCurrent.setSubmittedDate(resultSet.getDate(12));
            motTestCurrent.setMotTestStatus(getMotTestStatusById(resultSet.getInt(13)));
            motTestCurrent.setIssuedDate(resultSet.getDate(14));
            motTestCurrent.setExpiryDate(resultSet.getDate(15));
            // TODO causing recursive gets because of stupid dual linking
            // motTestCurrent.setMotTestOriginal( getMotTestCurrentById(
            // resultSet.getLong( 16 ) ) );
            // motTestCurrent.setMotTestPrs( getMotTestCurrentById( resultSet.getLong(
            // 17 ) ) );
            motTestCurrent.setNumber(resultSet.getBigDecimal(18));
            motTestCurrent.setWeightSourceLookup(getWeightSourceLookupById(resultSet.getInt(19)));
            motTestCurrent.setVehicleWeight(resultSet.getInt(20));
            motTestCurrent.setOdometerReadingValue(resultSet.getInt(21));
            motTestCurrent.setOdometerReadingUnit(resultSet.getString(22));
            motTestCurrent.setOdometerReadingType(resultSet.getString(23));
            motTestCurrent.setCreatedBy(resultSet.getInt(24));
            motTestCurrent.setCreatedOn(resultSet.getTimestamp(25));
            motTestCurrent.setLastUpdatedBy(resultSet.getInt(26));
            motTestCurrent.setLastUpdatedOn(resultSet.getTimestamp(27));
            motTestCurrent.setVersion(resultSet.getInt(28));
            motTestCurrent.setClientIp(resultSet.getString(29));

            motTestCurrent.setMotTestAddressComment(getMotTestCurrentAddressCommentByMotTest(motTestCurrent));
            motTestCurrent.setMotTestCancelled(getMotTestCancelledByMotTest(motTestCurrent));
            motTestCurrent.setMotTestComplaintRef(getMotTestComplaintRefByMotTest(motTestCurrent));
            motTestCurrent.setMotTestEmergencyReason(getMotTestEmergencyReasonByMotTest(motTestCurrent));

            List<MotTestRfrMap> motTestCurrentRfrMaps = getMotTestHistoryRfrMapsByMotTest(motTestCurrent);
            motTestCurrent.setMotTestCurrentRfrMaps(motTestCurrentRfrMaps);

            return motTestCurrent;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestAddressComment mapResultSetToMotTestAddressComment(MotTest parent, ResultSet resultSet) {

        try {
            MotTestAddressComment motTestAddressComment = new MotTestAddressComment();

            motTestAddressComment.setId(resultSet.getLong(1));
            motTestAddressComment.setMotTestCurrent(parent);
            motTestAddressComment.setComment(getCommentById(resultSet.getInt(2)));
            motTestAddressComment.setCreatedBy(resultSet.getInt(3));
            motTestAddressComment.setCreatedOn(resultSet.getTimestamp(4));
            motTestAddressComment.setLastUpdatedBy(resultSet.getInt(5));
            motTestAddressComment.setLastUpdatedOn(resultSet.getTimestamp(6));
            motTestAddressComment.setVersion(resultSet.getInt(7));

            return motTestAddressComment;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestCancelled mapResultSetToMotTestCancelled(MotTest parent, ResultSet resultSet) {

        try {
            MotTestCancelled motTestCancelled = new MotTestCancelled();

            motTestCancelled.setId(resultSet.getLong(1));
            motTestCancelled.setMotTestCurrent(parent);
            motTestCancelled.setMotTestReasonForCancelLookup(getMotTestReasonForCancelLookupById(resultSet.getInt(2)));
            motTestCancelled.setComment(getCommentById(resultSet.getInt(3)));
            motTestCancelled.setCreatedBy(resultSet.getInt(4));
            motTestCancelled.setCreatedOn(resultSet.getTimestamp(5));
            motTestCancelled.setLastUpdatedBy(resultSet.getInt(6));
            motTestCancelled.setLastUpdatedOn(resultSet.getTimestamp(7));
            motTestCancelled.setVersion(resultSet.getInt(8));

            return motTestCancelled;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestComplaintRef mapResultSetToMotTestComplaintRef(MotTest parent, ResultSet resultSet) {

        try {
            MotTestComplaintRef motTestComplaintRef = new MotTestComplaintRef();

            motTestComplaintRef.setId(resultSet.getLong(1));
            motTestComplaintRef.setMotTestCurrent(parent);
            motTestComplaintRef.setComplaintRef(resultSet.getString(2));
            motTestComplaintRef.setCreatedBy(resultSet.getInt(3));
            motTestComplaintRef.setCreatedOn(resultSet.getTimestamp(4));
            motTestComplaintRef.setLastUpdatedBy(resultSet.getInt(5));
            motTestComplaintRef.setLastUpdatedOn(resultSet.getTimestamp(6));
            motTestComplaintRef.setVersion(resultSet.getInt(7));

            return motTestComplaintRef;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestEmergencyReason mapResultSetToMotTestEmergencyReason(MotTest parent, ResultSet resultSet) {

        try {
            MotTestEmergencyReason motTestEmergencyReason = new MotTestEmergencyReason();

            motTestEmergencyReason.setId(resultSet.getLong(1));
            motTestEmergencyReason.setMotTestCurrent(parent);
            motTestEmergencyReason.setEmergencyLog(getEmergencyLogById(resultSet.getInt(2)));
            motTestEmergencyReason.setEmergencyReasonLookup(getEmergencyReasonLookupById(resultSet.getInt(3)));
            motTestEmergencyReason.setComment(getCommentById(resultSet.getInt(4)));
            motTestEmergencyReason.setCreatedBy(resultSet.getInt(5));
            motTestEmergencyReason.setCreatedOn(resultSet.getTimestamp(6));
            motTestEmergencyReason.setLastUpdatedBy(resultSet.getInt(7));
            motTestEmergencyReason.setLastUpdatedOn(resultSet.getTimestamp(8));
            motTestEmergencyReason.setVersion(resultSet.getInt(9));

            return motTestEmergencyReason;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private Comment mapResultSetToComment(ResultSet resultSet) {

        try {
            Comment comment = new Comment();

            comment.setId(resultSet.getLong(1));
            comment.setComment(resultSet.getString(2));
            comment.setPerson(getPersonById(resultSet.getInt(3)));
            comment.setCreatedBy(resultSet.getInt(4));
            comment.setCreatedOn(resultSet.getTimestamp(5));
            comment.setLastUpdatedBy(resultSet.getInt(6));
            comment.setLastUpdatedOn(resultSet.getTimestamp(7));
            comment.setVersion(resultSet.getInt(8));

            return comment;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestReasonForCancelLookup mapResultSetToMotTestReasonForCancelLookup(ResultSet resultSet) {

        try {
            MotTestReasonForCancelLookup motTestReasonForCancelLookup = new MotTestReasonForCancelLookup();

            motTestReasonForCancelLookup.setId(resultSet.getInt(1));
            motTestReasonForCancelLookup.setCode(resultSet.getString(2));
            motTestReasonForCancelLookup.setReason(resultSet.getString(3));
            motTestReasonForCancelLookup.setReasonCy(resultSet.getString(4));
            motTestReasonForCancelLookup.setIsSystemGenerated(resultSet.getBoolean(5));
            motTestReasonForCancelLookup.setIsDisplayable(resultSet.getBoolean(6));
            motTestReasonForCancelLookup.setIsAbandoned(resultSet.getBoolean(7));
            motTestReasonForCancelLookup.setCreatedBy(resultSet.getInt(8));
            motTestReasonForCancelLookup.setCreatedOn(resultSet.getTimestamp(9));
            motTestReasonForCancelLookup.setLastUpdatedBy(resultSet.getInt(10));
            motTestReasonForCancelLookup.setLastUpdatedOn(resultSet.getTimestamp(11));
            motTestReasonForCancelLookup.setVersion(resultSet.getInt(12));

            return motTestReasonForCancelLookup;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private EmergencyLog mapResultSetToEmergencyLog(ResultSet resultSet) {

        try {
            EmergencyLog emergencyLog = new EmergencyLog();

            emergencyLog.setId(resultSet.getInt(1));
            emergencyLog.setNumber(resultSet.getString(2));
            emergencyLog.setDescription(resultSet.getString(3));
            emergencyLog.setStartDate(resultSet.getDate(4));
            emergencyLog.setEndDate(resultSet.getDate(5));
            emergencyLog.setCreatedBy(resultSet.getInt(6));
            emergencyLog.setCreatedOn(resultSet.getTimestamp(7));
            emergencyLog.setLastUpdatedBy(resultSet.getInt(8));
            emergencyLog.setLastUpdatedOn(resultSet.getTimestamp(9));
            emergencyLog.setVersion(resultSet.getInt(10));

            return emergencyLog;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private EmergencyReasonLookup mapResultSetToEmergencyReasonLookup(ResultSet resultSet) {

        try {
            EmergencyReasonLookup emergencyReasonLookup = new EmergencyReasonLookup();

            emergencyReasonLookup.setId(resultSet.getInt(1));
            emergencyReasonLookup.setName(resultSet.getString(2));
            emergencyReasonLookup.setCode(resultSet.getString(3));
            emergencyReasonLookup.setDescription(resultSet.getString(4));
            emergencyReasonLookup.setDisplayOrder(resultSet.getInt(5));
            emergencyReasonLookup.setCreatedBy(resultSet.getInt(6));
            emergencyReasonLookup.setCreatedOn(resultSet.getTimestamp(7));
            emergencyReasonLookup.setLastUpdatedBy(resultSet.getInt(8));
            emergencyReasonLookup.setLastUpdatedOn(resultSet.getTimestamp(9));
            emergencyReasonLookup.setVersion(resultSet.getInt(10));

            return emergencyReasonLookup;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestRfrMap mapResultSetToMotTestCurrentRfrMap(MotTest parent, ResultSet resultSet) {

        try {
            MotTestRfrMap motTestCurrentRfrMap = new MotTestRfrMap();

            motTestCurrentRfrMap.setId(resultSet.getLong(1));
            motTestCurrentRfrMap.setMotTestCurrent(parent);
            motTestCurrentRfrMap.setReasonForRejection(getReasonForRejectionById(resultSet.getInt(2)));
            motTestCurrentRfrMap.setReasonForRejectionType(getReasonForRejectionTypeById(resultSet.getInt(3)));
            motTestCurrentRfrMap.setMotTestRfrLocationType(getMotTestRfrLocationTypeById(resultSet.getInt(4)));
            motTestCurrentRfrMap.setFailureDangerous(resultSet.getBoolean(5));
            motTestCurrentRfrMap.setGenerated(resultSet.getBoolean(6));
            motTestCurrentRfrMap.setOnOriginalTest(resultSet.getBoolean(7));
            motTestCurrentRfrMap.setCreatedBy(resultSet.getInt(8));
            motTestCurrentRfrMap.setCreatedOn(resultSet.getTimestamp(9));
            motTestCurrentRfrMap.setLastUpdatedBy(resultSet.getInt(10));
            motTestCurrentRfrMap.setLastUpdatedOn(resultSet.getTimestamp(11));
            motTestCurrentRfrMap.setVersion(resultSet.getInt(12));

            motTestCurrentRfrMap.setMotTestRfrMapComment(getMotTestRfrMapCommentByMotTestRfrMap(motTestCurrentRfrMap));
            motTestCurrentRfrMap.setMotTestRfrMapCustomDescription(
                    getMotTestRfrMapCustomDescriptionByMotTestRfrMap(motTestCurrentRfrMap));

            return motTestCurrentRfrMap;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestRfrMapComment mapResultSetToMotTestRfrMapComment(MotTestRfrMap parent, ResultSet resultSet) {

        try {
            MotTestRfrMapComment motTestRfrMapComment = new MotTestRfrMapComment();

            motTestRfrMapComment.setId(resultSet.getInt(1));
            motTestRfrMapComment.setMotTestCurrentRfrMap(parent);
            motTestRfrMapComment.setComment(resultSet.getString(2));
            motTestRfrMapComment.setCreatedBy(resultSet.getInt(3));
            motTestRfrMapComment.setCreatedOn(resultSet.getTimestamp(4));
            motTestRfrMapComment.setLastUpdatedBy(resultSet.getInt(5));
            motTestRfrMapComment.setLastUpdatedOn(resultSet.getTimestamp(6));
            motTestRfrMapComment.setVersion(resultSet.getInt(7));

            return motTestRfrMapComment;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestRfrMapCustomDescription mapResultSetToMotTestRfrMapCustomDescription(MotTestRfrMap parent, ResultSet resultSet) {

        try {
            MotTestRfrMapCustomDescription motTestRfrMapCustomDescription = new MotTestRfrMapCustomDescription();

            motTestRfrMapCustomDescription.setId(resultSet.getInt(1));
            motTestRfrMapCustomDescription.setMotTestCurrentRfrMap(parent);
            motTestRfrMapCustomDescription.setCustomDescription(resultSet.getString(2));
            motTestRfrMapCustomDescription.setCreatedBy(resultSet.getInt(3));
            motTestRfrMapCustomDescription.setCreatedOn(resultSet.getTimestamp(4));
            motTestRfrMapCustomDescription.setLastUpdatedBy(resultSet.getInt(5));
            motTestRfrMapCustomDescription.setLastUpdatedOn(resultSet.getTimestamp(6));
            motTestRfrMapCustomDescription.setVersion(resultSet.getInt(7));

            return motTestRfrMapCustomDescription;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private ReasonForRejection mapResultSetToReasonForRejection(ResultSet resultSet) {

        try {
            ReasonForRejection reasonForRejection = new ReasonForRejection();

            reasonForRejection.setId(resultSet.getInt(1));
            reasonForRejection.setTestItemCategory(getTestItemCategoryById(resultSet.getInt(2)));
            reasonForRejection.setTestItemSelectorName(resultSet.getString(3));
            reasonForRejection.setTestItemSelectorNameCy(resultSet.getString(4));
            reasonForRejection.setInspectionManualReference(resultSet.getString(5));
            reasonForRejection.setMinorItem(resultSet.getBoolean(6));
            reasonForRejection.setLocationMarker(resultSet.getBoolean(7));
            reasonForRejection.setQtMarker(resultSet.getBoolean(8));
            reasonForRejection.setNote(resultSet.getBoolean(9));
            reasonForRejection.setManual(resultSet.getString(10));
            reasonForRejection.setSpecProc(resultSet.getBoolean(11));
            reasonForRejection.setIsAdvisory(resultSet.getBoolean(12));
            reasonForRejection.setIsPrsFail(resultSet.getBoolean(13));
            reasonForRejection.setSectionTestItemCategory(getTestItemCategoryById(resultSet.getInt(14)));
            reasonForRejection.setCanBeDangerous(resultSet.getBoolean(15));
            reasonForRejection.setDateFirstUsed(resultSet.getDate(16));
            reasonForRejection.setAudience(resultSet.getString(17));
            reasonForRejection.setEndDate(resultSet.getDate(18));
            reasonForRejection.setCreatedBy(resultSet.getInt(19));
            reasonForRejection.setCreatedOn(resultSet.getTimestamp(20));
            reasonForRejection.setLastUpdatedBy(resultSet.getInt(21));
            reasonForRejection.setLastUpdatedOn(resultSet.getTimestamp(22));
            reasonForRejection.setVersion(resultSet.getInt(23));

            List<RfrLanguageContentMap> rfrLanguageContentMap = getRfrLanguageContentMapByReasonForRejection(
                    reasonForRejection);
            reasonForRejection.setTiRfrLanguageContentMaps(rfrLanguageContentMap);

            return reasonForRejection;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private ReasonForRejectionType mapResultSetToReasonForRejectionType(ResultSet resultSet) {

        try {
            ReasonForRejectionType reasonForRejectionType = new ReasonForRejectionType();

            reasonForRejectionType.setId(resultSet.getInt(1));
            reasonForRejectionType.setName(resultSet.getString(2));
            reasonForRejectionType.setCode(resultSet.getString(3));
            reasonForRejectionType.setDescription(resultSet.getString(4));
            reasonForRejectionType.setCreatedBy(resultSet.getInt(5));
            reasonForRejectionType.setCreatedOn(resultSet.getTimestamp(6));
            reasonForRejectionType.setLastUpdatedBy(resultSet.getInt(7));
            reasonForRejectionType.setLastUpdatedOn(resultSet.getTimestamp(8));
            reasonForRejectionType.setVersion(resultSet.getInt(9));

            return reasonForRejectionType;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestRfrLocationType mapResultSetToMotTestRfrLocationType(ResultSet resultSet) {

        try {
            MotTestRfrLocationType motTestRfrLocationType = new MotTestRfrLocationType();

            motTestRfrLocationType.setId(resultSet.getInt(1));
            motTestRfrLocationType.setLateral(resultSet.getString(2));
            motTestRfrLocationType.setLongitudinal(resultSet.getString(3));
            motTestRfrLocationType.setVertical(resultSet.getString(4));
            motTestRfrLocationType.setCreatedBy(resultSet.getInt(5));
            motTestRfrLocationType.setCreatedOn(resultSet.getTimestamp(6));
            motTestRfrLocationType.setLastUpdatedBy(resultSet.getInt(7));
            motTestRfrLocationType.setLastUpdatedOn(resultSet.getTimestamp(8));
            motTestRfrLocationType.setVersion(resultSet.getInt(9));
            motTestRfrLocationType.setSha1ConcatWsChksum(resultSet.getString(10));

            return motTestRfrLocationType;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private RfrLanguageContentMap mapResultSetToRfrLanguageContentMap(ReasonForRejection parent, ResultSet resultSet) {

        try {
            RfrLanguageContentMap rfrLanguageContentMap = new RfrLanguageContentMap();

            rfrLanguageContentMap.setId(resultSet.getInt(1));
            rfrLanguageContentMap.setReasonForRejection(parent);
            rfrLanguageContentMap.setLanguageType(getLanguageTypeById(resultSet.getInt(3)));
            rfrLanguageContentMap.setName(resultSet.getString(4));
            rfrLanguageContentMap.setInspectionManualDescription(resultSet.getString(5));
            rfrLanguageContentMap.setAdvisoryText(resultSet.getString(6));
            rfrLanguageContentMap.setTestItemSelectorName(resultSet.getString(7));
            rfrLanguageContentMap.setCreatedBy(resultSet.getInt(8));
            rfrLanguageContentMap.setCreatedOn(resultSet.getTimestamp(9));
            rfrLanguageContentMap.setLastUpdatedBy(resultSet.getInt(10));
            rfrLanguageContentMap.setLastUpdatedOn(resultSet.getTimestamp(11));
            rfrLanguageContentMap.setVersion(resultSet.getInt(12));

            return rfrLanguageContentMap;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private TestItemCategory mapResultSetToTestItemCategory(ResultSet resultSet) {

        try {
            TestItemCategory testItemCategory = new TestItemCategory();

            testItemCategory.setId(resultSet.getInt(1));
            // testItemCategory.setParentTestItemCategory( getTestItemCategoryById(
            // resultSet.getInt( 2 ) ) );
            testItemCategory.setSectionTestItemCategoryId(resultSet.getInt(3));
            testItemCategory.setBusinessRule(getBusinessRuleById(resultSet.getInt(4)));
            testItemCategory.setCreatedBy(resultSet.getInt(5));
            testItemCategory.setCreatedOn(resultSet.getTimestamp(6));
            testItemCategory.setLastUpdatedBy(resultSet.getInt(7));
            testItemCategory.setLastUpdatedOn(resultSet.getTimestamp(8));
            testItemCategory.setVersion(resultSet.getInt(9));

            List<TiCategoryLanguageContentMap> tiCategoryLanguageMap = getTiCategoryLanguageContentMapByTestItemCategory(
                    testItemCategory);
            testItemCategory.setTiCategoryLanguageContentMaps(tiCategoryLanguageMap);

            return testItemCategory;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private TiCategoryLanguageContentMap mapResultSetToTiCategoryLanguageContentMap(TestItemCategory parent, ResultSet resultSet) {

        try {
            TiCategoryLanguageContentMap tiCategoryLanguageContentMap = new TiCategoryLanguageContentMap();

            tiCategoryLanguageContentMap.setId(resultSet.getInt(1));
            tiCategoryLanguageContentMap.setTestItemCategory(parent);
            tiCategoryLanguageContentMap.setLanguageType(getLanguageTypeById(resultSet.getInt(3)));
            tiCategoryLanguageContentMap.setName(resultSet.getString(4));
            tiCategoryLanguageContentMap.setDescription(resultSet.getString(5));
            tiCategoryLanguageContentMap.setDisplayOrder(resultSet.getInt(6));
            tiCategoryLanguageContentMap.setCreatedBy(resultSet.getInt(7));
            tiCategoryLanguageContentMap.setCreatedOn(resultSet.getTimestamp(8));
            tiCategoryLanguageContentMap.setLastUpdatedBy(resultSet.getInt(9));
            tiCategoryLanguageContentMap.setLastUpdatedOn(resultSet.getTimestamp(10));
            tiCategoryLanguageContentMap.setVersion(resultSet.getInt(11));

            return tiCategoryLanguageContentMap;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private LanguageType mapResultSetToLanguageType(ResultSet resultSet) {

        try {
            LanguageType languageType = new LanguageType();

            languageType.setId(resultSet.getInt(1));
            languageType.setName(resultSet.getString(2));
            languageType.setCode(resultSet.getString(3));
            languageType.setCreatedBy(resultSet.getInt(4));
            languageType.setCreatedOn(resultSet.getTimestamp(5));
            languageType.setLastUpdatedBy(resultSet.getInt(6));
            languageType.setLastUpdatedOn(resultSet.getTimestamp(7));
            languageType.setVersion(resultSet.getInt(8));

            return languageType;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private BusinessRule mapResultSetToBusinessRule(ResultSet resultSet) {

        try {
            BusinessRule businessRule = new BusinessRule();

            businessRule.setId(resultSet.getInt(1));
            businessRule.setName(resultSet.getString(2));
            businessRule.setDefinition(resultSet.getString(3));
            businessRule.setBusinessRuleType(getBusinessRuleTypeById(resultSet.getInt(4)));
            businessRule.setComparison(resultSet.getString(5));
            businessRule.setDateValue(resultSet.getDate(6));
            businessRule.setCreatedBy(resultSet.getInt(7));
            businessRule.setCreatedOn(resultSet.getTimestamp(8));
            businessRule.setLastUpdatedBy(resultSet.getInt(9));
            businessRule.setLastUpdatedOn(resultSet.getTimestamp(10));
            businessRule.setVersion(resultSet.getInt(11));

            return businessRule;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private BusinessRuleType mapResultSetToBusinessRuleType(ResultSet resultSet) {

        try {
            BusinessRuleType businessRuleType = new BusinessRuleType();

            businessRuleType.setId(resultSet.getInt(1));
            businessRuleType.setName(resultSet.getString(2));
            businessRuleType.setCode(resultSet.getString(3));
            businessRuleType.setCreatedBy(resultSet.getInt(4));
            businessRuleType.setCreatedOn(resultSet.getTimestamp(5));
            businessRuleType.setLastUpdatedBy(resultSet.getInt(6));
            businessRuleType.setLastUpdatedOn(resultSet.getTimestamp(7));
            businessRuleType.setVersion(resultSet.getInt(8));

            return businessRuleType;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private Person mapResultSetToPerson(ResultSet resultSet) {

        try {
            Person person = new Person();

            person.setId(resultSet.getInt(1));

            return person;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private Organisation mapResultSetToOrganisation(ResultSet resultSet) {

        try {
            Organisation organisation = new Organisation();

            organisation.setId(resultSet.getInt(1));

            return organisation;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private Site mapResultSetToSite(ResultSet resultSet) {

        try {
            Site site = new Site();

            site.setId(resultSet.getInt(1));

            return site;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private JasperDocument mapResultSetToJasperDocument(ResultSet resultSet) {

        try {
            JasperDocument jasperDocument = new JasperDocument();

            jasperDocument.setId(resultSet.getLong(1));

            return jasperDocument;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestType mapResultSetToMotTestType(ResultSet resultSet) {

        try {
            MotTestType motTestType = new MotTestType();

            motTestType.setId(resultSet.getInt(1));
            motTestType.setCode(resultSet.getString(2));
            motTestType.setDescription(resultSet.getString(3));
            motTestType.setDisplayOrder(resultSet.getInt(4));
            motTestType.setIsDemo(resultSet.getBoolean(5));
            motTestType.setIsReinspection(resultSet.getBoolean(6));
            motTestType.setIsSlotConsuming(resultSet.getBoolean(7));
            motTestType.setCreatedBy(resultSet.getInt(8));
            motTestType.setCreatedOn(resultSet.getTimestamp(9));
            motTestType.setLastUpdatedBy(resultSet.getInt(10));
            motTestType.setLastUpdatedOn(resultSet.getTimestamp(11));
            motTestType.setVersion(resultSet.getInt(12));

            return motTestType;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private MotTestStatus mapResultSetToMotTestStatus(ResultSet resultSet) {

        try {
            MotTestStatus motTestStatus = new MotTestStatus();

            motTestStatus.setId(resultSet.getInt(1));
            motTestStatus.setCode(resultSet.getString(2));
            motTestStatus.setName(resultSet.getString(3));
            motTestStatus.setDescription(resultSet.getString(4));
            motTestStatus.setCreatedBy(resultSet.getInt(5));
            motTestStatus.setCreatedOn(resultSet.getTimestamp(6));
            motTestStatus.setLastUpdatedBy(resultSet.getInt(7));
            motTestStatus.setLastUpdatedOn(resultSet.getTimestamp(8));
            motTestStatus.setVersion(resultSet.getInt(9));

            return motTestStatus;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private WeightSourceLookup mapResultSetToWeightSourceLookup(ResultSet resultSet) {

        try {
            WeightSourceLookup weightSourceLookup = new WeightSourceLookup();

            weightSourceLookup.setId(resultSet.getInt(1));

            return weightSourceLookup;
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }
}
