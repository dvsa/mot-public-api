package uk.gov.dvsa.mot.persist;

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

import java.util.Date;
import java.util.List;

public interface MotTestReadDao {

    MotTest getMotTestById(long id);

    MotTest getMotTestCurrentById(long id);

    MotTest getMotTestHistoryById(long id);

    MotTest getMotTestByNumber(long number);

    MotTest getMotTestCurrentByNumber(long number);

    MotTest getMotTestHistoryByNumber(long number);

    List<MotTest> getMotTestsByVehicleId(int vehicleId);

    MotTest getLatestMotTestCurrentByVehicleId(int vehicleId);

    MotTest getLatestMotTestByVehicleId(int vehicleId);

    List<MotTest> getMotTestCurrentsByVehicleId(int vehicleId);

    List<MotTest> getMotTestHistorysByVehicleId(int vehicleId);

    List<MotTest> getMotTestsByDateRange(Date startDate, Date endDate);

    List<MotTest> getMotTestCurrentsByDateRange(Date startDate, Date endDate);

    List<MotTest> getMotTestHistorysByDateRange(Date startDate, Date endDate);

    List<DisplayMotTestItem> getMotHistoryByDateRange(Date startDate, Date endDate);

    List<DisplayMotTestItem> getMotHistoryCurrentByDateRange(Date startDate, Date endDate);

    List<DisplayMotTestItem> getMotHistoryHistoryByDateRange(Date startDate, Date endDate);

    List<MotTest> getMotTestsByPage(long offset, long limit);

    List<MotTest> getMotTestCurrentsByPage(long offset, long limit);

    List<MotTest> getMotTestHistorysByIdRange(long offset, long limit);

    MotTestAddressComment getMotTestCurrentAddressCommentByMotTest(MotTest parent);

    MotTestCancelled getMotTestCancelledByMotTest(MotTest parent);

    MotTestComplaintRef getMotTestComplaintRefByMotTest(MotTest parent);

    MotTestEmergencyReason getMotTestEmergencyReasonByMotTest(MotTest parent);

    Comment getCommentById(int id);

    MotTestReasonForCancelLookup getMotTestReasonForCancelLookupById(int id);

    EmergencyLog getEmergencyLogById(int id);

    EmergencyReasonLookup getEmergencyReasonLookupById(int id);

    List<MotTestRfrMap> getMotTestCurrentRfrMapsByMotTest(MotTest motTestCurrent);

    List<MotTestRfrMap> getMotTestHistoryRfrMapsByMotTest(MotTest motTestCurrent);

    MotTestRfrMapComment getMotTestRfrMapCommentByMotTestRfrMap(MotTestRfrMap parent);

    MotTestRfrMapCustomDescription getMotTestRfrMapCustomDescriptionByMotTestRfrMap(MotTestRfrMap parent);

    ReasonForRejection getReasonForRejectionById(int id);

    List<RfrLanguageContentMap> getRfrLanguageContentMapByReasonForRejection(ReasonForRejection reasonForRejection);

    ReasonForRejectionType getReasonForRejectionTypeById(int id);

    MotTestRfrLocationType getMotTestRfrLocationTypeById(int id);

    TestItemCategory getTestItemCategoryById(int id);

    List<TiCategoryLanguageContentMap> getTiCategoryLanguageContentMapByTestItemCategory(
            TestItemCategory testItemCategory);

    LanguageType getLanguageTypeById(int id);

    BusinessRule getBusinessRuleById(int id);

    BusinessRuleType getBusinessRuleTypeById(int id);

    // TODO This doesn't belong here, it belongs in a core service...
    Person getPersonById(int id);

    // TODO This doesn't belong here, it belongs in a core service...
    Organisation getOrganisationById(int id);

    // TODO This doesn't belong here, it belongs in a core service...
    Site getSiteById(int id);

    JasperDocument getJasperDocumentById(long id);

    MotTestType getMotTestTypeById(int id);

    MotTestStatus getMotTestStatusById(int id);

    WeightSourceLookup getWeightSourceLookupById(int id);

    ReasonForRejectionType getReasonForRejectionType(String name);

    MotTestStatus getMotTestStatus(String name);

    MotTestType getMotTestType(String name);

    // TODO belongs in vehicle service or reference service?
    WeightSourceLookup getWeightSourceLookup(String name);

    MotTestReasonForCancelLookup getMotTestReasonForCancelLookup(String name);

    EmergencyReasonLookup getEmergencyReasonLookup(String name);

    MotTestRfrLocationType getMotTestRfrLocationType(String lateral, String longitudinal, String vertical);

    ReasonForRejection getReasonForRejection(String type, String text);

}