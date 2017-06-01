package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;

public class MotServiceRequestHandler extends AbstractRequestHandler {
    private MotTestReadService motTestReadService;

    @Inject
    public void setMotTestReadService(MotTestReadService motTestReadService) {

        this.motTestReadService = motTestReadService;
    }

    /**
     * Get an MOT test by the MOT test number.
     *
     * This is a Lambda entry point.
     *
     * @param id      the MOT test number to search for
     * @param context AWS Lambda request context
     */
    public MotTest getMotTestById(Integer id, Context context) {

        return motTestReadService.getMotTestById(id);
    }

}
