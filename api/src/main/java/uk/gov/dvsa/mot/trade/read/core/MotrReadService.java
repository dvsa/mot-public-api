package uk.gov.dvsa.mot.trade.read.core;

import uk.gov.dvsa.mot.trade.api.MotrResponse;

public interface MotrReadService {

    MotrResponse getLatestMotTestForMotOrDvlaVehicleByRegistration(String registration);

    MotrResponse getLatestMotTestByRegistration(String registration);

    MotrResponse getLatestMotTestForDvlaVehicleByRegistration(String registration);

    MotrResponse getLatestMotTestByDvlaVehicleId(Integer dvlaVehicleId);

    MotrResponse getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber);
}
