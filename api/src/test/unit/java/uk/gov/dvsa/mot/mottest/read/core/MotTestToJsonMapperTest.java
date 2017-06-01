package uk.gov.dvsa.mot.mottest.read.core;

import org.junit.Test;

import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.mottest.api.MotTestRfrLocation;
import uk.gov.dvsa.mot.mottest.api.MotTestRfrMap;
import uk.gov.dvsa.mot.persist.model.Comment;
import uk.gov.dvsa.mot.persist.model.EmergencyLog;
import uk.gov.dvsa.mot.persist.model.EmergencyReasonLookup;
import uk.gov.dvsa.mot.persist.model.JasperDocument;
import uk.gov.dvsa.mot.persist.model.LanguageType;
import uk.gov.dvsa.mot.persist.model.MotTestAddressComment;
import uk.gov.dvsa.mot.persist.model.MotTestCancelled;
import uk.gov.dvsa.mot.persist.model.MotTestComplaintRef;
import uk.gov.dvsa.mot.persist.model.MotTestEmergencyReason;
import uk.gov.dvsa.mot.persist.model.MotTestHistory;
import uk.gov.dvsa.mot.persist.model.MotTestHistoryRfrMap;
import uk.gov.dvsa.mot.persist.model.MotTestReasonForCancelLookup;
import uk.gov.dvsa.mot.persist.model.MotTestRfrLocationType;
import uk.gov.dvsa.mot.persist.model.MotTestRfrMapComment;
import uk.gov.dvsa.mot.persist.model.MotTestRfrMapCustomDescription;
import uk.gov.dvsa.mot.persist.model.MotTestStatus;
import uk.gov.dvsa.mot.persist.model.MotTestType;
import uk.gov.dvsa.mot.persist.model.Person;
import uk.gov.dvsa.mot.persist.model.ReasonForRejection;
import uk.gov.dvsa.mot.persist.model.ReasonForRejectionType;
import uk.gov.dvsa.mot.persist.model.RfrLanguageContentMap;
import uk.gov.dvsa.mot.persist.model.Site;
import uk.gov.dvsa.mot.persist.model.TestItemCategory;
import uk.gov.dvsa.mot.persist.model.TiCategoryLanguageContentMap;
import uk.gov.dvsa.mot.persist.model.WeightSourceLookup;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static uk.gov.dvsa.mot.test.utility.Matchers.hasSize;



/**
 * Unit tests for the MotTest -> JSON source object mapping methods
 */
public class MotTestToJsonMapperTest {
    private final String inspectionManualReference = "REF-REF";
    private final LanguageType english = new LanguageType();
    private final LanguageType german = new LanguageType();
    private final String testItemCategoryDescription = "English description";
    private final String reasonForRejection = "A good reason to reject an MOT";
    private final String advisoryReasonForRejection = "An advisory notice";
    private final String rfrTypeName = "NAME";
    private final ReasonForRejectionType rfrType = new ReasonForRejectionType();
    private final String lateral = "LAT";
    private final String vertical = "UP";
    private final String longitudinal = "LONG";
    private final MotTestRfrLocationType location = new MotTestRfrLocationType();
    private final boolean onOriginalTest = true;
    private final boolean failureDangerous = true;
    private final boolean generated = true;
    private final String comment = "comment";
    private final String customDescription = "custom description";
    private final int vehicleId = 782139084;
    private final int vehicleVersion = 23000;
    private final MotTestStatus motTestStatus = new MotTestStatus();
    private final MotTestType motTestType = new MotTestType();
    private final String motTestStatusName = "SUPERPOSITIONED";
    private final String motTestTypeDescription = "Wavefunction test";
    private final Date motStartedDate;
    private final Date motCompletedDate;
    private final Date motIssuedDate;
    private final Date motExpiryDate;
    private final int vehicleWeight = 123789;
    private final boolean hasRegistration = true;
    private final int odometerReading = 437892;
    private final int createdBy = 54;
    private final int lastUpdatedBy = 22;
    private final Timestamp createdOn;
    private final Timestamp lastUpdatedOn;
    private final int motTestVersion = 2;
    private final String clientIp = "10.2.3.2";
    private final BigDecimal motTestNumber = BigDecimal.valueOf(2334429993800293L);
    private final String weightSourceLookupName = "big scales";
    private final WeightSourceLookup weightSourceLookup = new WeightSourceLookup();
    private final String motTestAddressCommentComment = "A comment";
    private final MotTestAddressComment motTestAddressComment = new MotTestAddressComment();
    private final MotTestComplaintRef motTestComplaintRef = new MotTestComplaintRef();
    private final String motTestComplaintRefComplaintRef = "REF REF REF";
    private final JasperDocument document = new JasperDocument();
    private final long documentId = 2340982309L;
    private final MotTestCancelled motTestCancelled = new MotTestCancelled();
    private final String motTestCancelReason = "Reason for cancellation";
    private final MotTestReasonForCancelLookup motTestReasonForCancelLookup = new MotTestReasonForCancelLookup();
    private final String motTestCancelReasonCommentComment = "Comment";
    private final MotTestEmergencyReason motTestEmergencyReason = new MotTestEmergencyReason();
    private final MotTestEmergencyReason motTestEmergencyReasonWithComment = new MotTestEmergencyReason();
    private final EmergencyLog motTestEmergencyLog = new EmergencyLog();
    private final int motTestEmergencyLogId = 823;
    private final EmergencyReasonLookup emergencyReasonLookup = new EmergencyReasonLookup();
    private final String emergencyReasonLookupDescription = "emergency reason lookup description";
    private final Comment emergencyReasonComment = new Comment();
    private final String emergencyReasonCommentComment = "Another comment, but this one is different";
    private final MotTestHistory motTestOriginal = new MotTestHistory();
    private final MotTestHistory motTestPrs = new MotTestHistory();
    private final Site motTestSite = new Site();
    private final int motTestSiteId = 78123987;
    private final Person motPerson = new Person();
    private final int personId = 23478;
    private final ReasonForRejectionType reasonForRejectionType = new ReasonForRejectionType();
    private final String reasonForRejectionTypeName = "RFR type name";
    private final MotTestRfrMapComment motTestRfrMapComment = new MotTestRfrMapComment();
    private final String motTestRfrMapCommentComment = "This is a comment";
    private final MotTestRfrMapCustomDescription motTestRfrMapCustomDescription = new MotTestRfrMapCustomDescription();
    private final String motTestRfrMapCustomDescriptionCustomDescription = "I am Groot";

    {
        english.setCode("EN");
        german.setCode("DE");
        rfrType.setName(rfrTypeName);
        location.setLateral(lateral);
        location.setLongitudinal(longitudinal);
        location.setVertical(vertical);
        motTestStatus.setName(motTestStatusName);
        motTestType.setDescription(motTestTypeDescription);

        LocalDateTime startedDate = LocalDateTime.of(2017, 5, 9, 14, 0);
        LocalDateTime completedDate = startedDate.plusMinutes(75);
        LocalDateTime issuedDate = completedDate.plusMinutes(15);
        LocalDateTime expiryDate = issuedDate.plusYears(1);
        LocalDateTime createdDate = LocalDateTime.of(2017, 5, 9, 0, 0);

        motStartedDate = Date.from(startedDate.toInstant(ZoneOffset.UTC));
        motCompletedDate = Date.from(completedDate.toInstant(ZoneOffset.UTC));
        motIssuedDate = Date.from(issuedDate.toInstant(ZoneOffset.UTC));
        motExpiryDate = Date.from(expiryDate.toInstant(ZoneOffset.UTC));

        lastUpdatedOn = Timestamp.from(issuedDate.plusSeconds(4).toInstant(ZoneOffset.UTC));
        createdOn = Timestamp.from(createdDate.toInstant(ZoneOffset.UTC));

        weightSourceLookup.setName(weightSourceLookupName);

        Comment comment = new Comment();
        comment.setComment(motTestAddressCommentComment);
        motTestAddressComment.setComment(comment);

        motTestComplaintRef.setComplaintRef(motTestComplaintRefComplaintRef);

        document.setId(documentId);

        motTestReasonForCancelLookup.setReason(motTestCancelReason);
        motTestCancelled.setMotTestReasonForCancelLookup(motTestReasonForCancelLookup);
        Comment comment2 = new Comment();
        comment2.setComment(motTestCancelReasonCommentComment);
        motTestCancelled.setComment(comment2);

        motTestEmergencyReason.setEmergencyLog(motTestEmergencyLog);
        motTestEmergencyReason.setEmergencyReasonLookup(emergencyReasonLookup);
        motTestEmergencyLog.setId(motTestEmergencyLogId);

        motTestEmergencyReasonWithComment.setEmergencyLog(motTestEmergencyLog);
        motTestEmergencyReasonWithComment.setComment(emergencyReasonComment);

        emergencyReasonComment.setComment(emergencyReasonCommentComment);

        emergencyReasonLookup.setDescription(emergencyReasonLookupDescription);

        motTestSite.setId(motTestSiteId);

        motPerson.setId(personId);

        reasonForRejectionType.setName(reasonForRejectionTypeName);

        motTestRfrMapComment.setComment(motTestRfrMapCommentComment);

        motTestRfrMapCustomDescription.setCustomDescription(motTestRfrMapCustomDescriptionCustomDescription);
    }

    @Test
    public void mapReasonForRejectionSqlToJson_MapsEverything() {

        ReasonForRejection storedReasonForRejection = makeReasonForRejection();

        {
            // check non-advisory
            MotTestRfrMap map = new MotTestRfrMap();

            MotTestRfrMap actual = MotTestToJsonMapper.mapReasonForRejectionSqlToJson(map, storedReasonForRejection);

            assertThat(actual, notNullValue());
            assertThat(actual.getInspectionManualReference(), equalTo(inspectionManualReference));
            assertThat(actual.getTestItemCategory(), equalTo(testItemCategoryDescription));
            assertThat(actual.getReasonForRejection(), equalTo(reasonForRejection));
        }

        {
            // check advisory
            MotTestRfrMap map = new MotTestRfrMap();
            map.setType("ADVISORY");

            MotTestRfrMap actual = MotTestToJsonMapper.mapReasonForRejectionSqlToJson(map, storedReasonForRejection);

            assertThat(actual, notNullValue());
            assertThat(actual.getInspectionManualReference(), equalTo(inspectionManualReference));
            assertThat(actual.getTestItemCategory(), equalTo(testItemCategoryDescription));
            assertThat(actual.getReasonForRejection(), equalTo(advisoryReasonForRejection));
        }
    }

    @Test
    public void mapMotTestRfrLocationSqltoJson_ThreeString() {

        final String lateral = "Lateral";
        final String longitudinal = "Longy";
        final String vertical = "Upness";

        MotTestRfrLocation actual = MotTestToJsonMapper.mapMotTestRfrLocationSqlToJson(lateral, longitudinal, vertical);

        assertThat(actual, notNullValue());
        assertThat(actual.getLateral(), equalTo(lateral));
        assertThat(actual.getLongitudinal(), equalTo(longitudinal));
        assertThat(actual.getVertical(), equalTo(vertical));
    }

    @Test
    public void mapMotTestRfrLocationSqltoJson_Object() {

        final String lateral = "Lateral";
        final String longitudinal = "Longy";
        final String vertical = "Upness";

        MotTestRfrLocationType location = new MotTestRfrLocationType();
        location.setLateral(lateral);
        location.setLongitudinal(longitudinal);
        location.setVertical(vertical);

        MotTestRfrLocation actual = MotTestToJsonMapper.mapMotTestRfrLocationSqlToJson(location);

        assertThat(actual, notNullValue());
        assertThat(actual.getLateral(), equalTo(lateral));
        assertThat(actual.getLongitudinal(), equalTo(longitudinal));
        assertThat(actual.getVertical(), equalTo(vertical));
    }

    @Test
    public void mapMotTestRfrLocationSqltoJson_Object_Null() {

        MotTestRfrLocation actual = MotTestToJsonMapper.mapMotTestRfrLocationSqlToJson(null);

        assertThat(actual, notNullValue());
        assertThat(actual.getLateral(), nullValue());
        assertThat(actual.getLongitudinal(), nullValue());
        assertThat(actual.getVertical(), nullValue());
    }

    @Test
    public void mapMotTestHistoryRfrMapSqlToJson_Minimal() {

        MotTestHistoryRfrMap map = makeMotTestRfrMap();

        MotTestRfrMap actual = MotTestToJsonMapper.mapMotTestHistoryRfrMapSqlToJson(map);

        assertThat(actual, notNullValue());
        assertThat(actual.isFailureDangerous(), equalTo(failureDangerous));
        assertThat(actual.isGenerated(), equalTo(generated));
        assertThat(actual.isOnOriginalTest(), equalTo(onOriginalTest));
        assertThat(actual.getType(), equalTo(rfrTypeName));
        assertThat(actual.getLocation().getLateral(), equalTo(lateral));
        assertThat(actual.getLocation().getLongitudinal(), equalTo(longitudinal));
        assertThat(actual.getLocation().getVertical(), equalTo(vertical));
    }

    @Test
    public void mapMotTestHistoryRfrMapSqlToJson_Maximal() {

        MotTestRfrMapComment motTestRfrMapComment = new MotTestRfrMapComment();
        motTestRfrMapComment.setComment(comment);

        MotTestRfrMapCustomDescription motTestRfrMapCustomDescription = new MotTestRfrMapCustomDescription();
        motTestRfrMapCustomDescription.setCustomDescription(customDescription);

        ReasonForRejection reasonForRejection = makeReasonForRejection();

        MotTestHistoryRfrMap map = makeMotTestRfrMap();
        map.setMotTestRfrMapComment(motTestRfrMapComment);
        map.setMotTestRfrMapCustomDescription(motTestRfrMapCustomDescription);
        map.setReasonForRejection(reasonForRejection);

        MotTestRfrMap actual = MotTestToJsonMapper.mapMotTestHistoryRfrMapSqlToJson(map);

        assertThat(actual, notNullValue());
        assertThat(actual.isFailureDangerous(), equalTo(failureDangerous));
        assertThat(actual.isGenerated(), equalTo(generated));
        assertThat(actual.isOnOriginalTest(), equalTo(onOriginalTest));
        assertThat(actual.getType(), equalTo(rfrTypeName));
        assertThat(actual.getLocation().getLateral(), equalTo(lateral));
        assertThat(actual.getLocation().getLongitudinal(), equalTo(longitudinal));
        assertThat(actual.getLocation().getVertical(), equalTo(vertical));
        assertThat(actual.getComment(), equalTo(comment));
        assertThat(actual.getCustomDescription(), equalTo(customDescription));
    }

    @Test
    public void mapMotTestHistorySqltoJson_Minimal() {

        MotTestHistory motTest = createCommonMotTestHistory();

        MotTest actual = MotTestToJsonMapper.mapMotTestHistorySqlToJson(motTest);

        assertCommonTestProperties(actual);
    }

    @Test
    public void mapMotTestHistorySqltoJson_Maximal() {

        final String odometerReadingType = "TYPE";
        final String odometerReadingUnit = "FURLONGS";
        final MotTestHistoryRfrMap rfrMap = makeMotTestRfrMap();
        final List<MotTestHistoryRfrMap> rfrMaps = new ArrayList<>();
        rfrMaps.add(rfrMap);

        MotTestHistory motTest = createCommonMotTestHistory();
        motTest.setNumber(motTestNumber);
        motTest.setWeightSourceLookup(weightSourceLookup);
        motTest.setMotTestAddressComment(motTestAddressComment);
        motTest.setMotTestComplaintRef(motTestComplaintRef);
        motTest.setDocument(document);
        motTest.setMotTestCancelled(motTestCancelled);
        motTest.setMotTestEmergencyReason(motTestEmergencyReason);
        motTest.setOdometerReadingValue(odometerReading);
        motTest.setMotTestHistoryRfrMaps(rfrMaps);

        {
            MotTest actual = MotTestToJsonMapper.mapMotTestHistorySqlToJson(motTest);

            assertCommonTestProperties(actual);
            assertThat(actual.getNumber(), equalTo(motTestNumber.longValue()));
            assertThat(actual.getWeightSourceLookup(), equalTo(weightSourceLookupName));
            assertThat(actual.getAddressComment(), equalTo(motTestAddressCommentComment));
            assertThat(actual.getComplaintRef(), equalTo(motTestComplaintRefComplaintRef));
            assertThat(actual.getDocumentId(), equalTo(documentId));
            assertThat(actual.getReasonForCancel(), equalTo(motTestCancelReason));
            assertThat(actual.getReasonForCancelComment(), equalTo(motTestCancelReasonCommentComment));
            assertThat(actual.getEmergencyReasonLogId(), equalTo(motTestEmergencyLogId));
            assertThat(actual.getEmergencyReason(), equalTo(emergencyReasonLookupDescription));
            assertThat(actual.getOdometerReadingValue(), equalTo(odometerReading));
            assertThat(actual.getMotTestRfrMaps(), hasSize(1));
        }

        // now change some things about the test object to follow more code paths
        // first an emergency reason with a comment instead of a
        // reason-for-cancelled
        motTest.setMotTestEmergencyReason(motTestEmergencyReasonWithComment);
        // and with some odometer data
        motTest.setOdometerReadingUnit(odometerReadingUnit);
        motTest.setOdometerReadingType(odometerReadingType);
        // and with an original MOT test
        motTest.setMotTestOriginal(motTestOriginal);
        // and with an MotTestPrs
        motTest.setMotTestPrs(motTestPrs);
        // and a site
        motTest.setSite(motTestSite);
        // and a person
        motTest.setPerson(motPerson);

        {
            MotTest actual = MotTestToJsonMapper.mapMotTestHistorySqlToJson(motTest);

            assertCommonTestProperties(actual);
            assertThat(actual.getNumber(), equalTo(motTestNumber.longValue()));
            assertThat(actual.getWeightSourceLookup(), equalTo(weightSourceLookupName));
            assertThat(actual.getAddressComment(), equalTo(motTestAddressCommentComment));
            assertThat(actual.getComplaintRef(), equalTo(motTestComplaintRefComplaintRef));
            assertThat(actual.getDocumentId(), equalTo(documentId));
            assertThat(actual.getReasonForCancel(), equalTo(motTestCancelReason));
            assertThat(actual.getReasonForCancelComment(), equalTo(motTestCancelReasonCommentComment));
            assertThat(actual.getEmergencyReasonLogId(), equalTo(motTestEmergencyLogId));
            assertThat(actual.getEmergencyReason(), equalTo(emergencyReasonCommentComment));
            assertThat(actual.getOdometerReadingUnit(), equalTo(odometerReadingUnit));
            assertThat(actual.getOdometerReadingType(), equalTo(odometerReadingType));
            assertThat(actual.getMotTestIdOriginal(), equalTo(motTestOriginal.getId()));
            assertThat(actual.getPrsMotTestId(), equalTo(motTestPrs.getId()));
            assertThat(actual.getSiteId(), equalTo(motTestSiteId));
            assertThat(actual.getPersonId(), equalTo(personId));
        }
    }

    @Test
    public void mapMotTestRfrMapSqltoJson() {

        uk.gov.dvsa.mot.persist.model.MotTestRfrMap map = new uk.gov.dvsa.mot.persist.model.MotTestRfrMap();

        map.setReasonForRejectionType(reasonForRejectionType);
        map.setFailureDangerous(failureDangerous);
        map.setGenerated(generated);
        map.setOnOriginalTest(onOriginalTest);

        {
            MotTestRfrMap actual = MotTestToJsonMapper.mapMotTestRfrMapSqlToJson(map);

            assertThat(actual, notNullValue());
            assertThat(actual.getType(), equalTo(reasonForRejectionTypeName));
            assertThat(actual.isFailureDangerous(), equalTo(failureDangerous));
            assertThat(actual.isGenerated(), equalTo(generated));
            assertThat(actual.isOnOriginalTest(), equalTo(onOriginalTest));
        }

        // now bump up the contents to check the presence of the optional stuff
        map.setMotTestRfrMapComment(motTestRfrMapComment);
        map.setMotTestRfrMapCustomDescription(motTestRfrMapCustomDescription);
        map.setReasonForRejection(makeReasonForRejection());

        {
            MotTestRfrMap actual = MotTestToJsonMapper.mapMotTestRfrMapSqlToJson(map);

            assertThat(actual, notNullValue());
            assertThat(actual.getType(), equalTo(reasonForRejectionTypeName));
            assertThat(actual.isFailureDangerous(), equalTo(failureDangerous));
            assertThat(actual.isGenerated(), equalTo(generated));
            assertThat(actual.isOnOriginalTest(), equalTo(onOriginalTest));
            assertThat(actual.getComment(), equalTo(motTestRfrMapCommentComment));
            assertThat(actual.getCustomDescription(), equalTo(motTestRfrMapCustomDescriptionCustomDescription));
        }
    }

    private MotTestHistory createCommonMotTestHistory() {

        MotTestHistory motTest = new MotTestHistory();
        motTest.setVehicleId(vehicleId);
        motTest.setVehicleVersion(vehicleVersion);
        motTest.setMotTestStatus(motTestStatus);
        motTest.setMotTestType(motTestType);
        motTest.setMotTestHistoryRfrMaps(new ArrayList<>());
        motTest.setStartedDate(motStartedDate);
        motTest.setCompletedDate(motCompletedDate);
        motTest.setIssuedDate(motIssuedDate);
        motTest.setExpiryDate(motExpiryDate);
        motTest.setVehicleWeight(vehicleWeight);
        motTest.setHasRegistration(hasRegistration);
        motTest.setCreatedBy(createdBy);
        motTest.setLastUpdatedBy(lastUpdatedBy);
        motTest.setLastUpdatedOn(lastUpdatedOn);
        motTest.setVersion(motTestVersion);
        motTest.setClientIp(clientIp);
        motTest.setCreatedOn(createdOn);
        motTest.setLastUpdatedOn(lastUpdatedOn);
        return motTest;
    }

    private void assertCommonTestProperties(MotTest actual) {

        assertThat(actual, notNullValue());
        assertThat(actual.getVehicleId(), equalTo(vehicleId));
        assertThat(actual.getVehicleVersion(), equalTo(vehicleVersion));
        assertThat(actual.getStatus(), equalTo(motTestStatusName));
        assertThat(actual.getMotTestType(), equalTo(motTestTypeDescription));
        assertThat(actual.getStartedDate(), equalTo(motStartedDate));
        assertThat(actual.getCompletedDate(), equalTo(motCompletedDate));
        assertThat(actual.getIssuedDate(), equalTo(motIssuedDate));
        assertThat(actual.getExpiryDate(), equalTo(motExpiryDate));
        assertThat(actual.getVehicleWeight(), equalTo(vehicleWeight));
        assertThat(actual.getHasRegistration(), equalTo(hasRegistration));
        assertThat(actual.getCreatedBy(), equalTo(String.valueOf(createdBy)));
        assertThat(actual.getLastUpdatedBy(), equalTo(String.valueOf(lastUpdatedBy)));
        assertThat(actual.getCreatedOn(), equalTo(createdOn));
        assertThat(actual.getLastUpdatedOn(), equalTo(lastUpdatedOn));
        assertThat(actual.getVersion(), equalTo(motTestVersion));
        assertThat(actual.getClientIp(), equalTo(clientIp));
    }

    /**
     * Create a ReasonForRejection which can be mapped through the mapper without
     * causing any exceptions.
     *
     * Values stored in it are based on fields above for ease of assertion later.
     *
     * @return a ReasonForRejection object which is populated sufficiently to avoid crashing the mapper.
     */
    private ReasonForRejection makeReasonForRejection() {

        TiCategoryLanguageContentMap tiCategoryLanguageContentMapEnglish = new TiCategoryLanguageContentMap();
        tiCategoryLanguageContentMapEnglish.setName("English");
        tiCategoryLanguageContentMapEnglish.setLanguageType(english);
        tiCategoryLanguageContentMapEnglish.setDescription(testItemCategoryDescription);

        // The German content map should be completely ignored in the conversion
        TiCategoryLanguageContentMap tiCategoryLanguageContentMapGerman = new TiCategoryLanguageContentMap();
        tiCategoryLanguageContentMapGerman.setName("Deutsch");
        tiCategoryLanguageContentMapGerman.setLanguageType(german);
        tiCategoryLanguageContentMapGerman.setDescription("Diese Woerter sind nicht auf Englisch");

        TestItemCategory testItemCategory = new TestItemCategory();
        testItemCategory.setTiCategoryLanguageContentMaps(new ArrayList<>());
        testItemCategory.addTiCategoryLanguageContentMap(tiCategoryLanguageContentMapGerman);
        testItemCategory.addTiCategoryLanguageContentMap(tiCategoryLanguageContentMapEnglish);

        RfrLanguageContentMap contentMapEnglish = new RfrLanguageContentMap();
        contentMapEnglish.setLanguageType(english);
        contentMapEnglish.setName(reasonForRejection);
        contentMapEnglish.setAdvisoryText(advisoryReasonForRejection);

        // the German content should be ignored, as we're only translating English
        // into the API responses
        RfrLanguageContentMap contentMapGerman = new RfrLanguageContentMap();
        contentMapGerman.setLanguageType(german);
        contentMapGerman.setName("Wichtige Name");
        contentMapGerman.setAdvisoryText("Nicht so wichtig, aber auch wichtig");

        List<RfrLanguageContentMap> rfrLanguageContentMaps = new ArrayList<>();
        rfrLanguageContentMaps.add(contentMapGerman);
        rfrLanguageContentMaps.add(contentMapEnglish);

        ReasonForRejection reasonForRejection = new ReasonForRejection();
        reasonForRejection.setTestItemCategory(testItemCategory);
        reasonForRejection.setInspectionManualReference(inspectionManualReference);
        reasonForRejection.setTiRfrLanguageContentMaps(rfrLanguageContentMaps);

        return reasonForRejection;
    }

    /**
     * Make an MotTestHistoryRfrMap with the minimal fields for the minimal test.
     * This is then shared to avoid the maximal test having to duplicate all the
     * code.
     *
     * @return a minimal MotTestHistoryRfrMap
     */
    private MotTestHistoryRfrMap makeMotTestRfrMap() {

        MotTestHistoryRfrMap map = new MotTestHistoryRfrMap();
        map.setReasonForRejectionType(rfrType);
        map.setMotTestRfrLocationType(location);
        map.setFailureDangerous(failureDangerous);
        map.setGenerated(generated);
        map.setOnOriginalTest(onOriginalTest);
        return map;
    }
}
