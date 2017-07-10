package uk.gov.dvsa.mot.mottest.read.core;

import com.google.inject.Inject;

import uk.gov.dvsa.mot.persist.MotTestReadDao;
import uk.gov.dvsa.mot.persist.ProvideDbConnection;
import uk.gov.dvsa.mot.persist.model.MotTest;
import uk.gov.dvsa.mot.persist.model.MotTestRfrMap;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@Resource
public class MotTestReadServiceDatabase implements MotTestReadService {
    private final MotTestReadDao motTestReadDao;

    @Inject
    public MotTestReadServiceDatabase(MotTestReadDao motTestReadDao) {

        this.motTestReadDao = motTestReadDao;
    }

    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.mottest.api.MotTest getMotTestById(long id) {

        MotTest motTest = motTestReadDao.getMotTestById(id);

        if (motTest != null) {
            return mapMotTestSqltoJson(motTest);
        } else {
            return null;
        }
    }

    @Override
    @ProvideDbConnection
    public List<DisplayMotTestItem> getMotHistoryByDateRange(Date startDate, Date endDate) {

        return motTestReadDao.getMotHistoryByDateRange(startDate, endDate);
    }

    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.mottest.api.MotTest getMotTestByNumber(long number) {

        MotTest motTest = motTestReadDao.getMotTestByNumber(number);

        if (motTest != null) {
            return mapMotTestSqltoJson(motTest);
        } else {
            return null;
        }
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.mottest.api.MotTest> getMotTestsByVehicleId(int vehicleId) {

        List<MotTest> motTests = motTestReadDao.getMotTestsByVehicleId(vehicleId);

        List<uk.gov.dvsa.mot.mottest.api.MotTest> result = new ArrayList<>();

        if (motTests != null) {
            for (MotTest mottest : motTests) {
                result.add(mapMotTestSqltoJson(mottest));
            }
        }

        return result;
    }
    
    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.mottest.api.MotTest getLatestMotTestPassByVehicle(Vehicle vehicle) {

        MotTest motTest = motTestReadDao.getLatestMotTestByVehicleId(vehicle.getId());

        if (motTest != null) {
            return mapMotTestSqltoJson(motTest);
        }

        return null;
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.mottest.api.MotTest> getMotTestsByDateRange(Date startDate, Date endDate) {

        List<MotTest> motTests = motTestReadDao.getMotTestsByDateRange(startDate, endDate);

        List<uk.gov.dvsa.mot.mottest.api.MotTest> result = new ArrayList<>();

        for (MotTest mottest : motTests) {
            result.add(mapMotTestSqltoJson(mottest));
        }

        return result;
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.mottest.api.MotTest> getMotTestsByPage(Long offset, Long limit) {

        if (offset == null) {
            offset = 0L;
        }

        if ((limit == null) || (limit > 100L)) {
            limit = 100L;
        }

        List<MotTest> motTests = motTestReadDao.getMotTestCurrentsByPage(offset, limit);

        List<uk.gov.dvsa.mot.mottest.api.MotTest> result = new ArrayList<>();

        for (MotTest mottest : motTests) {
            result.add(mapMotTestSqltoJson(mottest));
        }

        return result;
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.mottest.api.MotTest> getMotTestsByDatePage(Date date, Integer page) {

        if (date == null) {
            date = new Date();
        }
        if (page == null) {
            page = 0;
        } else if (page > 1440) {
            page = 1440;
        }

        Date startDate = date;
        Date endDate = date;

        List<MotTest> motTests = motTestReadDao.getMotTestsByDateRange(startDate, endDate);

        List<uk.gov.dvsa.mot.mottest.api.MotTest> result = new ArrayList<>();

        for (MotTest mottest : motTests) {
            result.add(mapMotTestSqltoJson(mottest));
        }

        return result;
    }

    protected uk.gov.dvsa.mot.mottest.api.MotTest mapMotTestSqltoJson(MotTest storedMotTest) {

        uk.gov.dvsa.mot.mottest.api.MotTest jsonMotTest = new uk.gov.dvsa.mot.mottest.api.MotTest();

        jsonMotTest.setId(storedMotTest.getId());

        jsonMotTest.setVehicleId(storedMotTest.getVehicleId());
        jsonMotTest.setVehicleVersion(storedMotTest.getVehicleVersion());

        jsonMotTest.setStatus(storedMotTest.getMotTestStatus().getName());
        jsonMotTest.setMotTestType(storedMotTest.getMotTestType().getDescription());

        if (storedMotTest.getNumber() != null) {
            jsonMotTest.setNumber(storedMotTest.getNumber().longValue());
        }

        jsonMotTest.setStartedDate(storedMotTest.getStartedDate());
        jsonMotTest.setCompletedDate(storedMotTest.getCompletedDate());
        jsonMotTest.setIssuedDate(storedMotTest.getIssuedDate());
        jsonMotTest.setExpiryDate(storedMotTest.getExpiryDate());

        jsonMotTest.setVehicleWeight(storedMotTest.getVehicleWeight());
        if (storedMotTest.getWeightSourceLookup() != null) {
            jsonMotTest.setWeightSourceLookup(storedMotTest.getWeightSourceLookup().getName());
        }

        if (storedMotTest.getMotTestAddressComment() != null) {
            jsonMotTest.setAddressComment(storedMotTest.getMotTestAddressComment().getComment().getComment());
        }
        if (storedMotTest.getMotTestComplaintRef() != null) {
            jsonMotTest.setComplaintRef(storedMotTest.getMotTestComplaintRef().getComplaintRef());
        }

        jsonMotTest.setHasRegistration(storedMotTest.getHasRegistration());
        if (storedMotTest.getDocument() != null) {
            jsonMotTest.setDocumentId(storedMotTest.getDocument().getId());
        }

        if (storedMotTest.getMotTestCancelled() != null) {
            if (storedMotTest.getMotTestCancelled().getMotTestReasonForCancelLookup() != null) {
                jsonMotTest
                        .setReasonForCancel(storedMotTest.getMotTestCancelled().getMotTestReasonForCancelLookup().getReason());
            }
            if (storedMotTest.getMotTestCancelled().getComment() != null) {
                jsonMotTest.setReasonForCancelComment(storedMotTest.getMotTestCancelled().getComment().getComment());
            }
        }

        if (storedMotTest.getMotTestEmergencyReason() != null) {
            if (storedMotTest.getMotTestEmergencyReason().getEmergencyLog() != null) {
                jsonMotTest.setEmergencyReasonLogId(storedMotTest.getMotTestEmergencyReason().getEmergencyLog().getId());
            }
            if (storedMotTest.getMotTestEmergencyReason().getEmergencyReasonLookup() != null) {
                jsonMotTest.setEmergencyReason(
                        storedMotTest.getMotTestEmergencyReason().getEmergencyReasonLookup().getDescription());
            }
            if (storedMotTest.getMotTestEmergencyReason().getComment() != null) {
                jsonMotTest.setEmergencyReason(storedMotTest.getMotTestEmergencyReason().getComment().getComment());
            }
        }

        jsonMotTest.setOdometerReadingValue(storedMotTest.getOdometerReadingValue());
        if (storedMotTest.getOdometerReadingUnit() != null) {
            jsonMotTest.setOdometerReadingUnit(storedMotTest.getOdometerReadingUnit());
        }
        if (storedMotTest.getOdometerReadingType() != null) {
            jsonMotTest.setOdometerReadingType(storedMotTest.getOdometerReadingType());
        }

        if (storedMotTest.getMotTestOriginal() != null) {
            jsonMotTest.setMotTestIdOriginal(storedMotTest.getMotTestOriginal().getId());
        }

        if (storedMotTest.getMotTestPrs() != null) {
            jsonMotTest.setPrsMotTestId(storedMotTest.getMotTestPrs().getId());
        }

        if (storedMotTest.getPerson() != null) {
            jsonMotTest.setPersonId(storedMotTest.getPerson().getId());
        }

        if (storedMotTest.getSite() != null) {
            jsonMotTest.setSiteId(storedMotTest.getSite().getId());
        }

        /* need to lookup person */
        jsonMotTest.setCreatedBy(String.valueOf(storedMotTest.getCreatedBy()));
        jsonMotTest.setLastUpdatedBy(String.valueOf(storedMotTest.getLastUpdatedBy()));
        jsonMotTest.setCreatedOn(storedMotTest.getCreatedOn());
        jsonMotTest.setLastUpdatedOn(storedMotTest.getLastUpdatedOn());
        jsonMotTest.setVersion(storedMotTest.getVersion());

        jsonMotTest.setClientIp(storedMotTest.getClientIp());

        for (MotTestRfrMap storedMotTestRfrMap : storedMotTest.getMotTestCurrentRfrMaps()) {
            jsonMotTest.getMotTestRfrMaps().add(MotTestToJsonMapper.mapMotTestRfrMapSqlToJson(storedMotTestRfrMap));
        }

        return jsonMotTest;
    }
}
