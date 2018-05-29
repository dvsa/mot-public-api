package uk.gov.dvsa.mot.trade.read.core;

import uk.gov.dvsa.mot.trade.api.MotrResponse;

public interface MotrReadService {

    MotrResponse getLatestMotTestByRegistration(String registration);

    MotrResponse getLatestMotTestByRegistration(String registration, Boolean withDvlaVehicles);

    MotrResponse getLatestMotTestByDvlaVehicleId(Integer dvlaVehicleId);

    MotrResponse getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber);
}
