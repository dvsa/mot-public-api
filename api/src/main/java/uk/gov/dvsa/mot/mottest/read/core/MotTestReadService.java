package uk.gov.dvsa.mot.mottest.read.core;

import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;

import java.util.Date;
import java.util.List;

/**
 * A service which can read information about MOT tests
 */
public interface MotTestReadService {
    List<DisplayMotTestItem> getMotHistoryByDateRange(Date startDate, Date endDate);

    MotTest getMotTestById(long id);

    MotTest getMotTestByNumber(long number);

    MotTest getLatestMotTestPassByVehicle(Vehicle vehicle);

    List<MotTest> getMotTestsByVehicleId(int vehicleId);

    List<MotTest> getMotTestsByDateRange(Date startDate, Date endDate);

    List<MotTest> getMotTestsByPage(Long startId, Long endId);

    List<MotTest> getMotTestsByDatePage(Date date, Integer page);
}