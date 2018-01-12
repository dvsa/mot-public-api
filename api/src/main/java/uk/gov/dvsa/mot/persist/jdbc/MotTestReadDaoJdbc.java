package uk.gov.dvsa.mot.persist.jdbc;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.persist.ConnectionManager;
import uk.gov.dvsa.mot.persist.MotTestReadDao;
import uk.gov.dvsa.mot.persist.jdbc.queries.GetLatestMotTestCurrentByVehicleId;
import uk.gov.dvsa.mot.persist.jdbc.queries.GetMotTestCurrentByVehicleId;
import uk.gov.dvsa.mot.persist.jdbc.queries.GetMotTestHistoryByVehicleId;
import uk.gov.dvsa.mot.persist.jdbc.util.DbQueryRunner;
import uk.gov.dvsa.mot.persist.jdbc.util.DbQueryRunnerImpl;
import uk.gov.dvsa.mot.persist.jdbc.util.ResultSetExtractor;
import uk.gov.dvsa.mot.persist.jdbc.util.ResultSetRowMapper;
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
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * MotTestReadDao implementation for JDBC connections to a MySQL database.
 */
public class MotTestReadDaoJdbc implements MotTestReadDao {
    private static final Logger logger = Logger.getLogger(MotTestReadDaoJdbc.class);
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private ConnectionManager connectionManager;

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager) {

        this.connectionManager = connectionManager;
    }

    @Override
    public MotTest getMotTestById(long id) {

        MotTest motTest = getMotTestCurrentById(id);

        if (motTest == null) {
            motTest = getMotTestHistoryById(id);
        }

        return motTest;
    }

    @Override
    public MotTest getMotTestCurrentById(long id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestCurrentRowMapper();
        return runner.executeQuery(MotTestReadSql.queryGetMotTestCurrentById, mapper, id);
    }

    @Override
    public MotTest getMotTestHistoryById(long id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestHistoryRowMapper();

        return runner.executeQuery(MotTestReadSql.queryGetMotTestHistoryById, mapper, id);
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

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestCurrentRowMapper();

        return runner.executeQuery(MotTestReadSql.queryGetMotTestCurrentByNumber, mapper, number);
    }

    @Override
    public MotTest getMotTestHistoryByNumber(long number) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestHistoryRowMapper();

        return runner.executeQuery(MotTestReadSql.queryGetMotTestHistoryByNumber, mapper, number);
    }

    @Override
    public MotTest getLatestMotTestByVehicleId(int vehicleId) {

        return getLatestMotTestCurrentByVehicleId(vehicleId);
    }

    @Override
    public List<MotTest> getMotTestsByVehicleId(int vehicleId) {

        List<MotTest> motTests = getMotTestCurrentsByVehicleId(vehicleId);
        motTests.addAll(getMotTestHistorysByVehicleId(vehicleId));

        return motTests;
    }

    @Override
    public MotTest getLatestMotTestCurrentByVehicleId(int vehicleId) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestCurrentRowMapper();

        return runner.executeQuery(new GetLatestMotTestCurrentByVehicleId().buildQuery(), mapper, vehicleId);
    }

    @Override
    public List<MotTest> getMotTestCurrentsByVehicleId(int vehicleId) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestCurrentRowMapper();

        return runner.executeQueryForList(new GetMotTestCurrentByVehicleId().buildQuery(), mapper, vehicleId);
    }

    @Override
    public List<MotTest> getMotTestHistorysByVehicleId(int vehicleId) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestHistoryRowMapper();

        return runner.executeQueryForList(new GetMotTestHistoryByVehicleId().buildQuery(), mapper, vehicleId);
    }

    @Override
    public List<MotTest> getMotTestsByDateRange(Date startDate, Date endDate) {

        List<MotTest> motTests = getMotTestCurrentsByDateRange(startDate, endDate);
        motTests.addAll(getMotTestHistorysByDateRange(startDate, endDate));

        return motTests;
    }

    @Override
    public List<MotTest> getMotTestCurrentsByDateRange(Date startDate, Date endDate) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestCurrentRowMapper();

        return runner.executeQueryForList(MotTestReadSql.queryGetMotTestCurrentsByDateRange, mapper, startDate, endDate);
    }

    @Override
    public List<MotTest> getMotTestHistorysByDateRange(Date startDate, Date endDate) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestHistoryRowMapper();

        return runner.executeQueryForList(MotTestReadSql.queryGetMotTestHistorysByDateRange, mapper, startDate, endDate);
    }

    @Override
    public List<DisplayMotTestItem> getMotHistoryByDateRange(Date startDate, Date endDate) {

        List<DisplayMotTestItem> motHistories = getMotHistoryCurrentByDateRange(startDate, endDate);
        motHistories.addAll(getMotHistoryHistoryByDateRange(startDate, endDate));

        return motHistories;
    }

    @Override
    public List<DisplayMotTestItem> getMotHistoryCurrentByDateRange(Date startDate, Date endDate) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetExtractor<List<DisplayMotTestItem>> mapper = new DisplayMotTestItemsExtractor();

        List<DisplayMotTestItem> results =
                runner.executeQuery(MotTestReadSql.QueryGetMotHistoryCurrentVehicleByDateRange, mapper, startDate, endDate);

        if (results == null) {
            return Collections.emptyList();
        }

        return results;
    }

    @Override
    public List<DisplayMotTestItem> getMotHistoryHistoryByDateRange(Date startDate, Date endDate) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetExtractor<List<DisplayMotTestItem>> mapper = new DisplayMotTestItemsExtractor();

        List<DisplayMotTestItem> results =
                runner.executeQuery(MotTestReadSql.QueryGetMotHistoryHistoryVehicleByDateRange, mapper, startDate, endDate);

        if (results == null) {
            return Collections.emptyList();
        }

        return results;
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

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestCurrentRowMapper();

        return runner.executeQueryForList(MotTestReadSql.queryGetMotTestCurrentsByIdRange, mapper, offset, limit);
    }

    @Override
    public List<MotTest> getMotTestHistorysByIdRange(long offset, long limit) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTest> mapper = new MotTestHistoryRowMapper();

        return runner.executeQueryForList(MotTestReadSql.queryGetMotTestHistorysByIdRange, mapper, offset, limit);
    }

    @Override
    public MotTestAddressComment getMotTestCurrentAddressCommentByMotTest(MotTest parent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestAddressComment> mapper = rs -> {
            MotTestAddressComment motTestAddressComment = new MotTestAddressComment();

            motTestAddressComment.setId(rs.getLong(1));
            motTestAddressComment.setMotTestCurrent(parent);
            motTestAddressComment.setComment(getCommentById(rs.getInt(2)));
            motTestAddressComment.setCreatedBy(rs.getInt(3));
            motTestAddressComment.setCreatedOn(rs.getTimestamp(4));
            motTestAddressComment.setLastUpdatedBy(rs.getInt(5));
            motTestAddressComment.setLastUpdatedOn(rs.getTimestamp(6));
            motTestAddressComment.setVersion(rs.getInt(7));

            return motTestAddressComment;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestAddressCommentByMotTestId, mapper, parent.getId());
    }

    @Override
    public MotTestCancelled getMotTestCancelledByMotTest(MotTest parent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestCancelled> mapper = rs -> {
            MotTestCancelled motTestCancelled = new MotTestCancelled();

            motTestCancelled.setId(rs.getLong(1));
            motTestCancelled.setMotTestCurrent(parent);
            motTestCancelled.setMotTestReasonForCancelLookup(getMotTestReasonForCancelLookupById(rs.getInt(2)));
            motTestCancelled.setComment(getCommentById(rs.getInt(3)));
            motTestCancelled.setCreatedBy(rs.getInt(4));
            motTestCancelled.setCreatedOn(rs.getTimestamp(5));
            motTestCancelled.setLastUpdatedBy(rs.getInt(6));
            motTestCancelled.setLastUpdatedOn(rs.getTimestamp(7));
            motTestCancelled.setVersion(rs.getInt(8));

            return motTestCancelled;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestCancelledByMotTestId, mapper, parent.getId());
    }

    @Override
    public MotTestComplaintRef getMotTestComplaintRefByMotTest(MotTest parent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestComplaintRef> mapper = rs -> {
            MotTestComplaintRef motTestComplaintRef = new MotTestComplaintRef();

            motTestComplaintRef.setId(rs.getLong(1));
            motTestComplaintRef.setMotTestCurrent(parent);
            motTestComplaintRef.setComplaintRef(rs.getString(2));
            motTestComplaintRef.setCreatedBy(rs.getInt(3));
            motTestComplaintRef.setCreatedOn(rs.getTimestamp(4));
            motTestComplaintRef.setLastUpdatedBy(rs.getInt(5));
            motTestComplaintRef.setLastUpdatedOn(rs.getTimestamp(6));
            motTestComplaintRef.setVersion(rs.getInt(7));

            return motTestComplaintRef;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestComplaintRefByMotTestId, mapper, parent.getId());
    }

    @Override
    public MotTestEmergencyReason getMotTestEmergencyReasonByMotTest(MotTest parent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestEmergencyReason> mapper = rs -> {
            MotTestEmergencyReason motTestEmergencyReason = new MotTestEmergencyReason();

            motTestEmergencyReason.setId(rs.getLong(1));
            motTestEmergencyReason.setMotTestCurrent(parent);
            motTestEmergencyReason.setEmergencyLog(getEmergencyLogById(rs.getInt(2)));
            motTestEmergencyReason.setEmergencyReasonLookup(getEmergencyReasonLookupById(rs.getInt(3)));
            motTestEmergencyReason.setComment(getCommentById(rs.getInt(4)));
            motTestEmergencyReason.setCreatedBy(rs.getInt(5));
            motTestEmergencyReason.setCreatedOn(rs.getTimestamp(6));
            motTestEmergencyReason.setLastUpdatedBy(rs.getInt(7));
            motTestEmergencyReason.setLastUpdatedOn(rs.getTimestamp(8));
            motTestEmergencyReason.setVersion(rs.getInt(9));

            return motTestEmergencyReason;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestEmergencyReasonByMotTestId, mapper, parent.getId());
    }

    @Override
    public Comment getCommentById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Comment> mapper = rs -> {
            Comment comment = new Comment();

            comment.setId(rs.getLong(1));
            comment.setComment(rs.getString(2));
            comment.setPerson(getPersonById(rs.getInt(3)));
            comment.setCreatedBy(rs.getInt(4));
            comment.setCreatedOn(rs.getTimestamp(5));
            comment.setLastUpdatedBy(rs.getInt(6));
            comment.setLastUpdatedOn(rs.getTimestamp(7));
            comment.setVersion(rs.getInt(8));

            return comment;
        };

        return runner.executeQuery(MotTestReadSql.queryGetCommentById, mapper, id);
    }

    @Override
    public MotTestReasonForCancelLookup getMotTestReasonForCancelLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestReasonForCancelLookup> mapper = rs -> {
            MotTestReasonForCancelLookup motTestReasonForCancelLookup = new MotTestReasonForCancelLookup();

            motTestReasonForCancelLookup.setId(rs.getInt(1));
            motTestReasonForCancelLookup.setCode(rs.getString(2));
            motTestReasonForCancelLookup.setReason(rs.getString(3));
            motTestReasonForCancelLookup.setReasonCy(rs.getString(4));
            motTestReasonForCancelLookup.setIsSystemGenerated(rs.getBoolean(5));
            motTestReasonForCancelLookup.setIsDisplayable(rs.getBoolean(6));
            motTestReasonForCancelLookup.setIsAbandoned(rs.getBoolean(7));
            motTestReasonForCancelLookup.setCreatedBy(rs.getInt(8));
            motTestReasonForCancelLookup.setCreatedOn(rs.getTimestamp(9));
            motTestReasonForCancelLookup.setLastUpdatedBy(rs.getInt(10));
            motTestReasonForCancelLookup.setLastUpdatedOn(rs.getTimestamp(11));
            motTestReasonForCancelLookup.setVersion(rs.getInt(12));

            return motTestReasonForCancelLookup;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestReasonForCancelLookupById, mapper, id);
    }

    @Override
    public EmergencyLog getEmergencyLogById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<EmergencyLog> mapper = rs -> {
            EmergencyLog emergencyLog = new EmergencyLog();

            emergencyLog.setId(rs.getInt(1));
            emergencyLog.setNumber(rs.getString(2));
            emergencyLog.setDescription(rs.getString(3));
            emergencyLog.setStartDate(rs.getDate(4));
            emergencyLog.setEndDate(rs.getDate(5));
            emergencyLog.setCreatedBy(rs.getInt(6));
            emergencyLog.setCreatedOn(rs.getTimestamp(7));
            emergencyLog.setLastUpdatedBy(rs.getInt(8));
            emergencyLog.setLastUpdatedOn(rs.getTimestamp(9));
            emergencyLog.setVersion(rs.getInt(10));

            return emergencyLog;
        };

        return runner.executeQuery(MotTestReadSql.queryGetEmergencyLogById, mapper, id);
    }

    @Override
    public EmergencyReasonLookup getEmergencyReasonLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<EmergencyReasonLookup> mapper = rs -> {
            EmergencyReasonLookup emergencyReasonLookup = new EmergencyReasonLookup();

            emergencyReasonLookup.setId(rs.getInt(1));
            emergencyReasonLookup.setName(rs.getString(2));
            emergencyReasonLookup.setCode(rs.getString(3));
            emergencyReasonLookup.setDescription(rs.getString(4));
            emergencyReasonLookup.setDisplayOrder(rs.getInt(5));
            emergencyReasonLookup.setCreatedBy(rs.getInt(6));
            emergencyReasonLookup.setCreatedOn(rs.getTimestamp(7));
            emergencyReasonLookup.setLastUpdatedBy(rs.getInt(8));
            emergencyReasonLookup.setLastUpdatedOn(rs.getTimestamp(9));
            emergencyReasonLookup.setVersion(rs.getInt(10));

            return emergencyReasonLookup;
        };

        return runner.executeQuery(MotTestReadSql.queryGetEmergencyReasonLookupById, mapper, id);
    }

    @Override
    public List<MotTestRfrMap> getMotTestCurrentRfrMapsByMotTest(MotTest motTestCurrent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestRfrMap> mapper = new MotTestRfrMapRowMapper(motTestCurrent);
        return runner.executeQueryForList(MotTestReadSql.queryGetMotTestCurrentRfrMapsByMotTestId, mapper, motTestCurrent.getId());
    }

    @Override
    public List<MotTestRfrMap> getMotTestHistoryRfrMapsByMotTest(MotTest motTestCurrent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestRfrMap> mapper = new MotTestRfrMapRowMapper(motTestCurrent);

        return runner.executeQueryForList(MotTestReadSql.queryGetMotTestHistoryRfrMapsByMotTestId, mapper, motTestCurrent.getId());
    }

    @Override
    public MotTestRfrMapComment getMotTestRfrMapCommentByMotTestRfrMap(MotTestRfrMap parent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestRfrMapComment> mapper = rs -> {
            MotTestRfrMapComment motTestRfrMapComment = new MotTestRfrMapComment();

            motTestRfrMapComment.setId(rs.getLong(1));
            motTestRfrMapComment.setMotTestCurrentRfrMap(parent);
            motTestRfrMapComment.setComment(rs.getString(2));
            motTestRfrMapComment.setCreatedBy(rs.getInt(3));
            motTestRfrMapComment.setCreatedOn(rs.getTimestamp(4));
            motTestRfrMapComment.setLastUpdatedBy(rs.getInt(5));
            motTestRfrMapComment.setLastUpdatedOn(rs.getTimestamp(6));
            motTestRfrMapComment.setVersion(rs.getInt(7));

            return motTestRfrMapComment;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestRfrMapCommentByMotTestRfrMapId, mapper, parent.getId());
    }

    @Override
    public MotTestRfrMapCustomDescription getMotTestRfrMapCustomDescriptionByMotTestRfrMap(MotTestRfrMap parent) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestRfrMapCustomDescription> mapper = rs -> {
            MotTestRfrMapCustomDescription motTestRfrMapCustomDescription = new MotTestRfrMapCustomDescription();

            motTestRfrMapCustomDescription.setId(rs.getLong(1));
            motTestRfrMapCustomDescription.setMotTestCurrentRfrMap(parent);
            motTestRfrMapCustomDescription.setCustomDescription(rs.getString(2));
            motTestRfrMapCustomDescription.setCreatedBy(rs.getInt(3));
            motTestRfrMapCustomDescription.setCreatedOn(rs.getTimestamp(4));
            motTestRfrMapCustomDescription.setLastUpdatedBy(rs.getInt(5));
            motTestRfrMapCustomDescription.setLastUpdatedOn(rs.getTimestamp(6));
            motTestRfrMapCustomDescription.setVersion(rs.getInt(7));

            return motTestRfrMapCustomDescription;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestRfrMapCustomDescriptionByMotTestRfrMapId, mapper, parent.getId());
    }

    @Override
    public ReasonForRejection getReasonForRejectionById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<ReasonForRejection> mapper = rs -> {
            ReasonForRejection reasonForRejection = new ReasonForRejection();

            reasonForRejection.setId(rs.getInt(1));
            reasonForRejection.setTestItemCategory(getTestItemCategoryById(rs.getInt(2)));
            reasonForRejection.setTestItemSelectorName(rs.getString(3));
            reasonForRejection.setTestItemSelectorNameCy(rs.getString(4));
            reasonForRejection.setInspectionManualReference(rs.getString(5));
            reasonForRejection.setMinorItem(rs.getBoolean(6));
            reasonForRejection.setLocationMarker(rs.getBoolean(7));
            reasonForRejection.setQtMarker(rs.getBoolean(8));
            reasonForRejection.setNote(rs.getBoolean(9));
            reasonForRejection.setManual(rs.getString(10));
            reasonForRejection.setSpecProc(rs.getBoolean(11));
            reasonForRejection.setIsAdvisory(rs.getBoolean(12));
            reasonForRejection.setIsPrsFail(rs.getBoolean(13));
            reasonForRejection.setSectionTestItemCategory(getTestItemCategoryById(rs.getInt(14)));
            reasonForRejection.setCanBeDangerous(rs.getBoolean(15));
            reasonForRejection.setDateFirstUsed(rs.getDate(16));
            reasonForRejection.setAudience(rs.getString(17));
            reasonForRejection.setEndDate(rs.getDate(18));
            reasonForRejection.setCreatedBy(rs.getInt(19));
            reasonForRejection.setCreatedOn(rs.getTimestamp(20));
            reasonForRejection.setLastUpdatedBy(rs.getInt(21));
            reasonForRejection.setLastUpdatedOn(rs.getTimestamp(22));
            reasonForRejection.setVersion(rs.getInt(23));

            List<RfrLanguageContentMap> rfrLanguageContentMap = getRfrLanguageContentMapByReasonForRejection(
                    reasonForRejection);
            reasonForRejection.setTiRfrLanguageContentMaps(rfrLanguageContentMap);

            return reasonForRejection;
        };

        return runner.executeQuery(MotTestReadSql.queryGetReasonForRejectionById, mapper, id);
    }

    @Override
    public List<RfrLanguageContentMap> getRfrLanguageContentMapByReasonForRejection(
            ReasonForRejection reasonForRejection) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<RfrLanguageContentMap> mapper = rs -> {
            RfrLanguageContentMap rfrLanguageContentMap = new RfrLanguageContentMap();

            rfrLanguageContentMap.setId(rs.getInt(1));
            rfrLanguageContentMap.setReasonForRejection(reasonForRejection);
            rfrLanguageContentMap.setLanguageType(getLanguageTypeById(rs.getInt(3)));
            rfrLanguageContentMap.setName(rs.getString(4));
            rfrLanguageContentMap.setInspectionManualDescription(rs.getString(5));
            rfrLanguageContentMap.setAdvisoryText(rs.getString(6));
            rfrLanguageContentMap.setTestItemSelectorName(rs.getString(7));
            rfrLanguageContentMap.setCreatedBy(rs.getInt(8));
            rfrLanguageContentMap.setCreatedOn(rs.getTimestamp(9));
            rfrLanguageContentMap.setLastUpdatedBy(rs.getInt(10));
            rfrLanguageContentMap.setLastUpdatedOn(rs.getTimestamp(11));
            rfrLanguageContentMap.setVersion(rs.getInt(12));

            return rfrLanguageContentMap;
        };

        return runner.executeQueryForList(MotTestReadSql.queryGetRfrLanguageContentMapByReasonForRejection, mapper, reasonForRejection
                .getId());
    }

    @Override
    public ReasonForRejectionType getReasonForRejectionTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<ReasonForRejectionType> mapper = rs -> {
            ReasonForRejectionType reasonForRejectionType = new ReasonForRejectionType();

            reasonForRejectionType.setId(rs.getInt(1));
            reasonForRejectionType.setName(rs.getString(2));
            reasonForRejectionType.setCode(rs.getString(3));
            reasonForRejectionType.setDescription(rs.getString(4));
            reasonForRejectionType.setCreatedBy(rs.getInt(5));
            reasonForRejectionType.setCreatedOn(rs.getTimestamp(6));
            reasonForRejectionType.setLastUpdatedBy(rs.getInt(7));
            reasonForRejectionType.setLastUpdatedOn(rs.getTimestamp(8));
            reasonForRejectionType.setVersion(rs.getInt(9));

            return reasonForRejectionType;
        };

        return runner.executeQuery(MotTestReadSql.queryGetReasonForRejectionTypeById, mapper, id);
    }

    @Override
    public MotTestRfrLocationType getMotTestRfrLocationTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestRfrLocationType> mapper = rs -> {
            MotTestRfrLocationType motTestRfrLocationType = new MotTestRfrLocationType();

            motTestRfrLocationType.setId(rs.getInt(1));
            motTestRfrLocationType.setLateral(rs.getString(2));
            motTestRfrLocationType.setLongitudinal(rs.getString(3));
            motTestRfrLocationType.setVertical(rs.getString(4));
            motTestRfrLocationType.setCreatedBy(rs.getInt(5));
            motTestRfrLocationType.setCreatedOn(rs.getTimestamp(6));
            motTestRfrLocationType.setLastUpdatedBy(rs.getInt(7));
            motTestRfrLocationType.setLastUpdatedOn(rs.getTimestamp(8));
            motTestRfrLocationType.setVersion(rs.getInt(9));
            motTestRfrLocationType.setSha1ConcatWsChksum(rs.getString(10));

            return motTestRfrLocationType;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestRfrLocationTypeById, mapper, id);
    }

    @Override
    public TestItemCategory getTestItemCategoryById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<TestItemCategory> mapper = rs -> {
            TestItemCategory testItemCategory = new TestItemCategory();

            testItemCategory.setId(rs.getInt(1));
            testItemCategory.setSectionTestItemCategoryId(rs.getInt(3));
            testItemCategory.setBusinessRule(getBusinessRuleById(rs.getInt(4)));
            testItemCategory.setCreatedBy(rs.getInt(5));
            testItemCategory.setCreatedOn(rs.getTimestamp(6));
            testItemCategory.setLastUpdatedBy(rs.getInt(7));
            testItemCategory.setLastUpdatedOn(rs.getTimestamp(8));
            testItemCategory.setVersion(rs.getInt(9));

            List<TiCategoryLanguageContentMap> tiCategoryLanguageMap = getTiCategoryLanguageContentMapByTestItemCategory(
                    testItemCategory);
            testItemCategory.setTiCategoryLanguageContentMaps(tiCategoryLanguageMap);

            return testItemCategory;
        };

        return runner.executeQuery(MotTestReadSql.queryGetTestItemCategoryById, mapper, id);
    }

    @Override
    public List<TiCategoryLanguageContentMap> getTiCategoryLanguageContentMapByTestItemCategory(
            TestItemCategory testItemCategory) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<TiCategoryLanguageContentMap> mapper = rs -> {
            TiCategoryLanguageContentMap tiCategoryLanguageContentMap = new TiCategoryLanguageContentMap();

            tiCategoryLanguageContentMap.setId(rs.getInt(1));
            tiCategoryLanguageContentMap.setTestItemCategory(testItemCategory);
            tiCategoryLanguageContentMap.setLanguageType(getLanguageTypeById(rs.getInt(3)));
            tiCategoryLanguageContentMap.setName(rs.getString(4));
            tiCategoryLanguageContentMap.setDescription(rs.getString(5));
            tiCategoryLanguageContentMap.setDisplayOrder(rs.getInt(6));
            tiCategoryLanguageContentMap.setCreatedBy(rs.getInt(7));
            tiCategoryLanguageContentMap.setCreatedOn(rs.getTimestamp(8));
            tiCategoryLanguageContentMap.setLastUpdatedBy(rs.getInt(9));
            tiCategoryLanguageContentMap.setLastUpdatedOn(rs.getTimestamp(10));
            tiCategoryLanguageContentMap.setVersion(rs.getInt(11));

            return tiCategoryLanguageContentMap;
        };

        return runner.executeQueryForList(MotTestReadSql.queryGetTiCategoryLanguageContentMapByTestItemCategory, mapper, testItemCategory
                .getId()
        );
    }

    @Override
    public LanguageType getLanguageTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<LanguageType> mapper = rs -> {
            LanguageType languageType = new LanguageType();

            languageType.setId(rs.getInt(1));
            languageType.setName(rs.getString(2));
            languageType.setCode(rs.getString(3));
            languageType.setCreatedBy(rs.getInt(4));
            languageType.setCreatedOn(rs.getTimestamp(5));
            languageType.setLastUpdatedBy(rs.getInt(6));
            languageType.setLastUpdatedOn(rs.getTimestamp(7));
            languageType.setVersion(rs.getInt(8));

            return languageType;
        };

        return runner.executeQuery(MotTestReadSql.queryGetLanguageTypeById, mapper, id);
    }

    @Override
    public BusinessRule getBusinessRuleById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<BusinessRule> mapper = rs -> {
            BusinessRule businessRule = new BusinessRule();

            businessRule.setId(rs.getInt(1));
            businessRule.setName(rs.getString(2));
            businessRule.setDefinition(rs.getString(3));
            businessRule.setBusinessRuleType(getBusinessRuleTypeById(rs.getInt(4)));
            businessRule.setComparison(rs.getString(5));
            businessRule.setDateValue(rs.getDate(6));
            businessRule.setCreatedBy(rs.getInt(7));
            businessRule.setCreatedOn(rs.getTimestamp(8));
            businessRule.setLastUpdatedBy(rs.getInt(9));
            businessRule.setLastUpdatedOn(rs.getTimestamp(10));
            businessRule.setVersion(rs.getInt(11));

            return businessRule;
        };

        return runner.executeQuery(MotTestReadSql.queryGetBusinessRuleById, mapper, id);
    }

    @Override
    public BusinessRuleType getBusinessRuleTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<BusinessRuleType> mapper = rs -> {
            BusinessRuleType businessRuleType = new BusinessRuleType();

            businessRuleType.setId(rs.getInt(1));
            businessRuleType.setName(rs.getString(2));
            businessRuleType.setCode(rs.getString(3));
            businessRuleType.setCreatedBy(rs.getInt(4));
            businessRuleType.setCreatedOn(rs.getTimestamp(5));
            businessRuleType.setLastUpdatedBy(rs.getInt(6));
            businessRuleType.setLastUpdatedOn(rs.getTimestamp(7));
            businessRuleType.setVersion(rs.getInt(8));

            return businessRuleType;
        };

        return runner.executeQuery(MotTestReadSql.queryGetBusinessRuleTypeById, mapper, id);
    }

    @Override
    public Person getPersonById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Person> mapper = rs -> {
            Person person = new Person();

            person.setId(rs.getInt(1));

            return person;
        };

        return runner.executeQuery(MotTestReadSql.queryGetPersonById, mapper, id);
    }

    @Override
    public Organisation getOrganisationById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Organisation> mapper = rs -> {
            Organisation organisation = new Organisation();

            organisation.setId(rs.getInt(1));

            return organisation;
        };

        return runner.executeQuery(MotTestReadSql.queryGetOrganisationById, mapper, id);
    }

    @Override
    public Site getSiteById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<Site> mapper = rs -> {
            Site site = new Site();

            site.setId(rs.getInt(1));

            return site;
        };

        return runner.executeQuery(MotTestReadSql.queryGetSiteById, mapper, id);
    }

    @Override
    public JasperDocument getJasperDocumentById(long id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<JasperDocument> mapper = rs -> {
            JasperDocument jasperDocument = new JasperDocument();

            jasperDocument.setId(rs.getLong(1));

            return jasperDocument;
        };

        return runner.executeQuery(MotTestReadSql.queryGetJasperDocumentById, mapper, id);
    }

    @Override
    public MotTestType getMotTestTypeById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestType> mapper = rs -> {
            MotTestType motTestType = new MotTestType();

            motTestType.setId(rs.getInt(1));
            motTestType.setCode(rs.getString(2));
            motTestType.setDescription(rs.getString(3));
            motTestType.setDisplayOrder(rs.getInt(4));
            motTestType.setIsDemo(rs.getBoolean(5));
            motTestType.setIsReinspection(rs.getBoolean(6));
            motTestType.setIsSlotConsuming(rs.getBoolean(7));
            motTestType.setCreatedBy(rs.getInt(8));
            motTestType.setCreatedOn(rs.getTimestamp(9));
            motTestType.setLastUpdatedBy(rs.getInt(10));
            motTestType.setLastUpdatedOn(rs.getTimestamp(11));
            motTestType.setVersion(rs.getInt(12));

            return motTestType;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestTypeById, mapper, id);
    }

    @Override
    public MotTestStatus getMotTestStatusById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<MotTestStatus> mapper = rs -> {
            MotTestStatus motTestStatus = new MotTestStatus();

            motTestStatus.setId(rs.getInt(1));
            motTestStatus.setCode(rs.getString(2));
            motTestStatus.setName(rs.getString(3));
            motTestStatus.setDescription(rs.getString(4));
            motTestStatus.setCreatedBy(rs.getInt(5));
            motTestStatus.setCreatedOn(rs.getTimestamp(6));
            motTestStatus.setLastUpdatedBy(rs.getInt(7));
            motTestStatus.setLastUpdatedOn(rs.getTimestamp(8));
            motTestStatus.setVersion(rs.getInt(9));

            return motTestStatus;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestStatusById, mapper, id);
    }

    @Override
    public WeightSourceLookup getWeightSourceLookupById(int id) {

        DbQueryRunner runner = new DbQueryRunnerImpl(connectionManager.getConnection());
        ResultSetRowMapper<WeightSourceLookup> mapper = rs -> {
            WeightSourceLookup weightSourceLookup = new WeightSourceLookup();

            weightSourceLookup.setId(rs.getInt(1));

            return weightSourceLookup;
        };

        return runner.executeQuery(MotTestReadSql.queryGetMotTestTypeById, mapper, id);
    }

    private class DisplayMotTestItemsExtractor implements ResultSetExtractor<List<DisplayMotTestItem>> {

        @Override
        public List<DisplayMotTestItem> extractData(ResultSet rs) throws SQLException {

            List<DisplayMotTestItem> displayMotTestItems = new ArrayList<>();

            DisplayMotTestItem displayMotTestItem;
            List<RfrAndAdvisoryItem> rfrAndAdvisoryItems = new ArrayList<>();

            long motTestId = 0;

            do {
                // vehilce.id not used
                long currMotTestId = rs.getLong(2);
                long currMotTestRfrMapId = rs.getLong(3);

                if (currMotTestId != motTestId) {
                    motTestId = currMotTestId;
                    displayMotTestItem = new DisplayMotTestItem();
                    displayMotTestItem.setRfrAndComments(rfrAndAdvisoryItems);
                    displayMotTestItems.add(displayMotTestItem);

                    displayMotTestItem.setRegistration(rs.getString(4));
                    displayMotTestItem.setMakeName(rs.getString(5));
                    displayMotTestItem.setModelName(rs.getString(6));
                    if (rs.getDate(7) != null) {
                        displayMotTestItem.setFirstUsedDate(SDF_DATE.format(rs.getTimestamp(7)));
                    }
                    displayMotTestItem.setFuelType(rs.getString(8));
                    displayMotTestItem.setPrimaryColour(rs.getString(9));
                    // secondary colour not used
                    // started date not used
                    if (rs.getDate(12) != null) {
                        displayMotTestItem.setCompletedDate(SDF_DATE_TIME.format(rs.getTimestamp(12)));
                    }
                    displayMotTestItem.setTestResult(rs.getString(13));
                    if (rs.getDate(14) != null) {
                        displayMotTestItem.setExpiryDate(SDF_DATE.format(rs.getTimestamp(14)));
                    }
                    displayMotTestItem.setOdometerValue(String.valueOf(rs.getInt(15)));
                    displayMotTestItem.setOdometerUnit(rs.getString(16));
                    // odometer result type not used
                    displayMotTestItem.setMotTestNumber(String.valueOf(rs.getBigDecimal(18)));
                }

                if (currMotTestRfrMapId != 0) {
                    RfrAndAdvisoryItem rfrAndAdvisoryItem = new RfrAndAdvisoryItem();
                    rfrAndAdvisoryItem.setType(rs.getString(19));
                    rfrAndAdvisoryItem.setText(rs.getString(20));
                    rfrAndAdvisoryItems.add(rfrAndAdvisoryItem);
                }
            } while (rs.next());

            return displayMotTestItems;
        }
    }

    private class MotTestCurrentRowMapper implements ResultSetRowMapper<MotTest> {

        @Override
        public MotTest mapRow(ResultSet rs) throws SQLException {

            MotTest motTestCurrent = new MotTest();

            motTestCurrent.setId(rs.getLong(1));
            motTestCurrent.setPerson(getPersonById(rs.getInt(2)));
            motTestCurrent.setVehicleId(rs.getInt(3));
            motTestCurrent.setVehicleVersion(rs.getInt(4));
            motTestCurrent.setDocument(getJasperDocumentById(rs.getLong(5)));
            motTestCurrent.setOrganisation(getOrganisationById(rs.getInt(6)));
            motTestCurrent.setSite(getSiteById(rs.getInt(7)));
            motTestCurrent.setHasRegistration(rs.getBoolean(8));
            motTestCurrent.setMotTestType(getMotTestTypeById(rs.getInt(9)));
            motTestCurrent.setStartedDate(rs.getDate(10));
            motTestCurrent.setCompletedDate(rs.getDate(11));
            motTestCurrent.setSubmittedDate(rs.getDate(12));
            motTestCurrent.setMotTestStatus(getMotTestStatusById(rs.getInt(13)));
            motTestCurrent.setIssuedDate(rs.getDate(14));
            motTestCurrent.setExpiryDate(rs.getDate(15));
            motTestCurrent.setNumber(rs.getBigDecimal(18));
            motTestCurrent.setWeightSourceLookup(getWeightSourceLookupById(rs.getInt(19)));
            motTestCurrent.setVehicleWeight(rs.getInt(20));
            motTestCurrent.setOdometerReadingValue(rs.getInt(21));
            motTestCurrent.setOdometerReadingUnit(rs.getString(22));
            motTestCurrent.setOdometerReadingType(rs.getString(23));
            motTestCurrent.setCreatedBy(rs.getInt(24));
            motTestCurrent.setCreatedOn(rs.getTimestamp(25));
            motTestCurrent.setLastUpdatedBy(rs.getInt(26));
            motTestCurrent.setLastUpdatedOn(rs.getTimestamp(27));
            motTestCurrent.setVersion(rs.getInt(28));
            motTestCurrent.setClientIp(rs.getString(29));

            motTestCurrent.setMotTestAddressComment(getMotTestCurrentAddressCommentByMotTest(motTestCurrent));
            motTestCurrent.setMotTestCancelled(getMotTestCancelledByMotTest(motTestCurrent));
            motTestCurrent.setMotTestComplaintRef(getMotTestComplaintRefByMotTest(motTestCurrent));
            motTestCurrent.setMotTestEmergencyReason(getMotTestEmergencyReasonByMotTest(motTestCurrent));

            List<MotTestRfrMap> motTestCurrentRfrMaps = getMotTestCurrentRfrMapsByMotTest(motTestCurrent);
            motTestCurrent.setMotTestCurrentRfrMaps(motTestCurrentRfrMaps);

            return motTestCurrent;
        }
    }

    private class MotTestHistoryRowMapper implements ResultSetRowMapper<MotTest> {

        @Override
        public MotTest mapRow(ResultSet rs) throws SQLException {

            MotTest motTest = new MotTestCurrentRowMapper().mapRow(rs);

            List<MotTestRfrMap> motTestCurrentRfrMaps = getMotTestHistoryRfrMapsByMotTest(motTest);
            motTest.setMotTestCurrentRfrMaps(motTestCurrentRfrMaps);

            return motTest;
        }
    }

    private class MotTestRfrMapRowMapper implements ResultSetRowMapper<MotTestRfrMap> {

        private MotTest parent;

        public MotTestRfrMapRowMapper(MotTest parent) {

            this.parent = parent;
        }

        @Override
        public MotTestRfrMap mapRow(ResultSet rs) throws SQLException {

            MotTestRfrMap motTestCurrentRfrMap = new MotTestRfrMap();

            motTestCurrentRfrMap.setId(rs.getLong(1));
            motTestCurrentRfrMap.setMotTestCurrent(parent);
            motTestCurrentRfrMap.setReasonForRejection(getReasonForRejectionById(rs.getInt(2)));
            motTestCurrentRfrMap.setReasonForRejectionType(getReasonForRejectionTypeById(rs.getInt(3)));
            motTestCurrentRfrMap.setMotTestRfrLocationType(getMotTestRfrLocationTypeById(rs.getInt(4)));
            motTestCurrentRfrMap.setFailureDangerous(rs.getBoolean(5));
            motTestCurrentRfrMap.setGenerated(rs.getBoolean(6));
            motTestCurrentRfrMap.setOnOriginalTest(rs.getBoolean(7));
            motTestCurrentRfrMap.setCreatedBy(rs.getInt(8));
            motTestCurrentRfrMap.setCreatedOn(rs.getTimestamp(9));
            motTestCurrentRfrMap.setLastUpdatedBy(rs.getInt(10));
            motTestCurrentRfrMap.setLastUpdatedOn(rs.getTimestamp(11));
            motTestCurrentRfrMap.setVersion(rs.getInt(12));

            motTestCurrentRfrMap.setMotTestRfrMapComment(getMotTestRfrMapCommentByMotTestRfrMap(motTestCurrentRfrMap));
            motTestCurrentRfrMap.setMotTestRfrMapCustomDescription(
                    getMotTestRfrMapCustomDescriptionByMotTestRfrMap(motTestCurrentRfrMap));

            return motTestCurrentRfrMap;
        }
    }
}
