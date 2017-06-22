package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

/**
 * This is an entry point class for an AWS Lambda.
 */
public class VehicleServiceRequestHandler extends AbstractRequestHandler {
    private static final Logger logger = Logger.getLogger(VehicleServiceRequestHandler.class);

    private VehicleReadService vehicleReadService;

    public VehicleServiceRequestHandler() {

        super();
    }

    public VehicleServiceRequestHandler(boolean injectSelf) {

        super(injectSelf);
    }

    @Inject
    public void setVehicleReadService(VehicleReadService vehicleReadService) {

        this.vehicleReadService = vehicleReadService;
    }

    /**
     * Get a vehicle by the vehicle ID.
     *
     * This is a Lambda entry point.
     *
     * @param id      The vehicle ID to search for
     * @param context AWS Lambda request context
     * @return The vehicle, if found.
     * @throws TradeException If the vehicle is not found, or an error occurred.
     */
    public Vehicle getVehicleById(Integer id, Context context) throws TradeException {

        logger.debug("getVehicleById : " + id);
        try {
            Vehicle vehicle = vehicleReadService.getVehicleById(id);

            if (vehicle != null) {
                return vehicle;
            } else {
                throw new InvalidResourceException("Vehicle id " + id + " not found", context.getAwsRequestId());
            }
        } catch (TradeException e) {
            logger.error(e);
            throw e;
        } catch (Exception e) {
            logger.error(e);
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        }
    }

}
