package uk.gov.dvsa.mot.app;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.util.CollectionUtils;
import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.read.core.TradeReadService;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Entry point class for Lambdas.
 *
 * Various public methods in this class are called on Lambda invocation for
 * trade service queries.
 */
public class TradeServiceRequestHandler extends AbstractRequestHandler {
    private static final Logger logger = Logger.getLogger(TradeServiceRequestHandler.class);
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

    private TradeReadService tradeReadService;

    public TradeServiceRequestHandler() {

        super();
    }

    public TradeServiceRequestHandler(boolean injectSelf) {

        super(injectSelf);
    }

    /**
     * Set the read service which will be used to make queries required by this
     * class.
     *
     * @param tradeReadService an instance of something which implements {@link TradeReadService}
     */
    @Inject
    public void setTradeReadService(TradeReadService tradeReadService) {

        logger.trace("Entering setTradeServiceRequestHandler");
        this.tradeReadService = tradeReadService;
        logger.trace("Exiting setTradeServiceRequestHandler");
    }

    /**
     * Get MOT tests as per the provided request, grouped by vehicle.
     *
     * @param request a {@link TradeServiceRequest} which describes the parameters to search for MOT tests by. This method only uses query
     *                parameters, and the valid options are: vehicleId, number (MOT test number), registration and make (must be specified
     *                together), date and page, or page.
     * @param context AWS Lambda context object
     * @return A list of {@link Vehicle}, with each vehicle populated with its MOT tests matching the search parameters.
     * @throws TradeException Under various error conditions, including no tests found. It is expected that the surrounding integration will
     *                        interpret this exception appropriately.
     */
    public List<Vehicle> getTradeMotTests(TradeServiceRequest request, Context context) throws TradeException {

        try {
            logger.trace("Entering getTradeMotTests");

            if (request.getQueryParams().getVehicleId() != null) {
                logger.info("Trade API request for vehicle_id = " + request.getQueryParams().getVehicleId());
                List<Vehicle> vehicles = tradeReadService.getVehiclesByVehicleId(request.getQueryParams().getVehicleId());

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    logger.debug("getTradeMotTests for vehicle_id = " + request.getQueryParams().getVehicleId() + " found 0 ");
                    throw new InvalidResourceException("No MOT Tests found with vehicle id : " + request.getQueryParams().getVehicleId(),
                            context.getAwsRequestId());
                }

                logger.info("Trade API request for vehicle_id = " + request.getQueryParams().getVehicleId() + " returned " + vehicles
                        .size() + " records");
                logger.trace("Exiting getTradeMotTests");
                return vehicles;
            } else if (request.getQueryParams().getNumber() != null) {
                logger.info("Trade API request for mot test number = " + request.getQueryParams().getNumber());
                List<Vehicle> vehicles = tradeReadService.getVehiclesMotTestsByMotTestNumber(request.getQueryParams().getNumber());

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    logger.debug("getTradeMotTests for number = " + request.getQueryParams().getNumber() + " found 0");
                    throw new InvalidResourceException("No MOT Tests found with number : " + request.getQueryParams().getNumber(),
                            context.getAwsRequestId());
                }

                logger.info("Trade API request for mot test number = " + request.getQueryParams().getNumber() + " returned " + vehicles
                        .size() + " records");
                logger.trace("Exiting getTradeMotTests");
                return vehicles;
            } else if ((request.getQueryParams().getRegistration() != null)) {
                String registration = URLDecoder.decode(request.getQueryParams().getRegistration(), "UTF-8");

                logger.info("Trade API request for registration = " + registration);
                List<Vehicle> vehicles = tradeReadService.getVehiclesByRegistration(registration);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    logger.debug("getTradeMotTests for registration = " + request.getQueryParams().getNumber() + " found 0");
                    throw new InvalidResourceException("No MOT Tests found with vehicle registration : " + registration,
                            context.getAwsRequestId());
                }

                logger.info("Trade API request for registration = " + registration + " returned " +
                        vehicles.size() + " records");
                logger.trace("Exiting getTradeMotTests");
                return vehicles;
            } else if (request.getQueryParams().getDate() != null) {
                Date date = sdfDate.parse(request.getQueryParams().getDate());
                logger.info("Trade API request for date = " + date + " and page = " + request.getQueryParams().getPage());
                List<Vehicle> vehicles = tradeReadService.getVehiclesByDatePage(date, request.getQueryParams().getPage());

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found for date : " + request.getQueryParams().getDate() + " page : " +
                            request.getQueryParams().getPage(), context.getAwsRequestId());
                }

                logger.info("Trade API request for date = " + date + " and page = " + request.getQueryParams().getPage() + " returned " +
                        vehicles.size() + " records");
                logger.trace("Exiting getTradeMotTests");
                return vehicles;
            } else if (request.getQueryParams().getPage() != null) {
                logger.info("Trade API request for page = " + request.getQueryParams().getPage());
                List<Vehicle> vehicles = tradeReadService.getVehiclesByPage(request.getQueryParams().getPage());

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found for page: " + request.getQueryParams().getPage(), context
                            .getAwsRequestId());
                }

                logger.info("Trade API request for page = " + request.getQueryParams().getPage() + " returned " + vehicles.size() + " " +
                        "records");
                logger.trace("Exiting getTradeMotTests");
                return vehicles;
            } else {
                logger.trace("Exiting getTradeMotTests");
                throw new BadRequestException("Unrecognised parameter set", context.getAwsRequestId());
            }
        } catch (TradeException e) {
            // no need to log these errors, just throw them back
            logger.trace("Exiting getTradeMotTests");
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e);
            logger.trace("Exiting getTradeMotTests");
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        }
    }

    /**
     * Get a list of known vehicle makes.
     *
     * @param request this parameter is not used
     * @param context AWS Lambda context
     * @return A list of the names of known vehicle makes.
     * @throws TradeException if there is an error retrieving the list of makes from the read service.
     */
    public List<String> getMakes(TradeServiceRequest request, Context context) throws TradeException {

        try {
            logger.trace("Entering getMakes");
            List<String> makes = tradeReadService.getMakes();
            logger.trace("Exiting getMakes");
            return makes;
        } catch (Exception e) {
            logger.error(e);
            logger.trace("Exiting getMakes");
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        }
    }

    /**
     * Retrieve MOT tests in legacy format, based on the provided request.
     *
     * @param request a {@link TradeServiceRequest} object describing the search to perform for MOTs. This method requires the provision of
     *                a vehicle registration as a path parameter, along with a vehicle make also as a path parameter.
     * @param context AWS Lambda request context
     * @return A list of MOTs in the legacy format.
     * @throws TradeException If there's an error retrieving data or a not-found condition.
     */
    public List<DisplayMotTestItem> getTradeMotTestsLegacy(TradeServiceRequest request, Context context) throws TradeException {

        try {
            logger.trace("Entering getTradeMotTestsLegacy");
            if (request.getPathParams().getRegistration() != null) {
                String registration = URLDecoder.decode(request.getPathParams().getRegistration(), "UTF-8");
                String make = URLDecoder.decode(request.getPathParams().getMake(), "UTF-8");
                logger.info("Trade API MOTH request for registration = " + registration + " and make = " + make);
                List<DisplayMotTestItem> items = tradeReadService.getMotTestsByRegistrationAndMake(registration, make);

                if (CollectionUtils.isNullOrEmpty(items)) {
                    throw new InvalidResourceException("No MOT Tests found for registration : " + registration + " and make : " + make,
                            context.getAwsRequestId());
                }

                logger.info("Trade API MOTR request for registration = " + registration + " and make = " + make + " returned " + items
                        .size() + " records");
                logger.trace("Exiting getTradeMotTestsLegacy");
                return items;
            } else {
                logger.trace("Exiting getTradeMotTestsLegacy");
                throw new BadRequestException("Invalid Parameters", context.getAwsRequestId());
            }
        } catch (TradeException e) {
            // no need to log these errors, just throw them back
            logger.trace("Exiting getTradeMotTestsLegacy");
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e);
            logger.trace("Exiting getTradeMotTestsLegacy");
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        }
    }

    /**
     * Get the latest MOT test recorded for the vehicle specified by registration.
     *
     * @param request Describes the request. For this method, we require only the registration as a path parameter.
     * @param context AWS Lambda request context
     * @return The vehicle and its latest MOT, if found.
     * @throws TradeException If there is a retrieval error or the vehicle and test are not found.
     */
    public Vehicle getLatestMotTest(TradeServiceRequest request, Context context) throws TradeException {

        try {
            logger.trace("Entering getLatestMotTest");
            if (request.getPathParams().getRegistration() != null) {
                String registration = URLDecoder.decode(request.getPathParams().getRegistration(), "UTF-8");
                logger.info("Trade API MOTR request for registration = " + registration);
                Vehicle vehicle = tradeReadService.getLatestMotTestByRegistration(registration);

                if (vehicle == null) {
                    throw new InvalidResourceException("No MOT Test or DVLA vehicle found for registration " + registration,
                            context.getAwsRequestId());
                }

                logger.info("Trade API MOTR request for registration = " + registration + " returned 1 record");
                logger.trace("Exiting getLatestMotTest");
                return vehicle;
            } else {
                logger.trace("Exiting getLatestMotTest");
                throw new BadRequestException("Invalid Parameters", context.getAwsRequestId());
            }
        } catch (TradeException e) {
            // no need to log these errors, just throw them back
            logger.trace("Exiting getLatestMotTest");
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e);
            logger.trace("Exiting getLatestMotTest");
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        }
    }

    /**
     * Get the latest vehicle information and MOT test recorded for the vehicle specified by MOT Test Number.
     *
     * @param request Describes the request. For this method, we require only the registration as a path parameter.
     * @param context AWS Lambda request context
     * @return The vehicle and its latest MOT, if found.
     * @throws TradeException If there is a retrieval error or the vehicle and test are not found.
     */
    public Vehicle getLatestMotTestByMotTestNumber(TradeServiceRequest request, Context context) throws TradeException {

        try {
            logger.trace("Entering getLatestMotTestByMotTestNumber");
            if (request.getPathParams().getNumber() != null) {
                logger.info("Trade API MOTR request for mot test number = " + request.getPathParams().getNumber());

                Vehicle vehicle =
                        tradeReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(request.getPathParams().getNumber());

                if (vehicle == null) {
                    logger.debug("getLatestMotTestByMotTestNumber for number = " + request.getPathParams().getNumber() + " found 0");
                    throw new InvalidResourceException("No MOT Tests found with number : " + request.getPathParams().getNumber(),
                            context.getAwsRequestId());
                }

                logger.info("Trade API MOTR request for mot test number = " + request.getPathParams().getNumber() + " returned 1 record");
                logger.trace("Exiting getLatestMotTestByMotTestNumber");
                return vehicle;
            } else {
                logger.trace("Exiting getLatestMotTestByMotTestNumber");
                throw new BadRequestException("Invalid Parameters", context.getAwsRequestId());
            }
        } catch (TradeException e) {
            // no need to log these errors, just throw them back
            logger.trace("Exiting getLatestMotTestByMotTestNumber");
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e);
            logger.trace("Exiting getLatestMotTestByMotTestNumber");
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        }
    }

    /**
     * Get the latest vehicle information and MOT test recorded for the vehicle specified by vehicle DVLA id.
     *
     * @param request Describes the request. For this method, we require only the dvla id as a path parameter.
     * @param context AWS Lambda request context
     * @return The vehicle and its latest MOT, if found.
     * @throws TradeException If there is a retrieval error or the vehicle and test are not found.
     */
    public Vehicle getLatestMotTestByDvlaVehicleId(TradeServiceRequest request, Context context) throws TradeException {

        Integer dvlaVehicleId = request.getPathParams().getDvlaId();

        try {
            logger.trace("Entering getLatestMotTestByDvlaVehicleId");
            if (dvlaVehicleId != null) {
                logger.info("Trade API MOTR request for DVLA id = " + dvlaVehicleId);

                Vehicle vehicle = tradeReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

                if (vehicle == null) {
                    throw new InvalidResourceException("No MOT Test or DVLA vehicle found for DVLA vehicle id " + dvlaVehicleId,
                            context.getAwsRequestId());
                }

                logger.info("Trade API MOTR request for DVLA id = " + dvlaVehicleId + " returned 1 record");
                logger.trace("Exiting getLatestMotTestByDvlaVehicleId");
                return vehicle;
            } else {
                logger.trace("Exiting getLatestMotTestByDvlaVehicleId");
                throw new BadRequestException("Invalid Parameters", context.getAwsRequestId());
            }
        } catch (TradeException e) {
            // no need to log these errors, just throw them back
            logger.trace("Exiting getLatestMotTestByDvlaVehicleId");
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e);
            logger.trace("Exiting getLatestMotTestByDvlaVehicleId");
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        }
    }
}
