package uk.gov.dvsa.mot.motr.service;

import uk.gov.dvsa.mot.motr.model.VehicleWithLatestTest;
import uk.gov.dvsa.mot.trade.api.MotrResponse;

import java.util.Optional;

public interface MotrReadService {

    Optional<VehicleWithLatestTest> getLatestMotTestByRegistration(String registration);

    Optional<VehicleWithLatestTest> getLatestMotTestForDvlaVehicleByRegistration(String registration);

    Optional<VehicleWithLatestTest> getLatestMotTestByDvlaVehicleId(Integer dvlaVehicleId);

    Optional<VehicleWithLatestTest> getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber);
}
