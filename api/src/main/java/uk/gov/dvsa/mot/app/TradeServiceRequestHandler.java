package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.util.CollectionUtils;
import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.mapper.VehicleResponseMapper;
import uk.gov.dvsa.mot.trade.api.response.mapper.VehicleResponseMapperFactory;
import uk.gov.dvsa.mot.trade.read.core.TradeReadService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Entry point class for Lambdas.
 *
 * Various public methods in this class are called on Lambda invocation for
 * trade service queries.
 */
@Path("/")
public class TradeServiceRequestHandler extends AbstractRequestHandler {
    private static final Logger logger = LogManager.getLogger(TradeServiceRequestHandler.class);
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

    private TradeReadService tradeReadService;
    private VehicleResponseMapperFactory vehicleResponseMapperFactory;

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

        logger.trace("Entering setTradeReadService");
        this.tradeReadService = tradeReadService;
        logger.trace("Exiting setTradeReadService");
    }

    @Inject
    public void setVehicleResponseMapperFactory(VehicleResponseMapperFactory vehicleResponseMapperFactory) {

        this.vehicleResponseMapperFactory = vehicleResponseMapperFactory;
    }

    /**
     * Get MOT tests as per the provided request, grouped by vehicle.
     *
     * @param vehicleId query parameter
     * @param number query parameter
     * @param registration query parameter
     * @param motTestDate date query parameter
     * @param page query parameter
     * @param requestContext AWS Lambda context object
     * @return A list of {@link Vehicle}, with each vehicle populated with its MOT tests matching the search parameters.
     * @throws TradeException Under various error conditions, including no tests found. It is expected that the surrounding integration will
     *                        interpret this exception appropriately.
     */
    @GET
    @Path("trade/vehicles/mot-tests")
    @Produces({MediaType.APPLICATION_JSON,
            MediaType.WILDCARD,
            "application/json+v1",
            "application/json+v2",
            "application/json+v3",
            "application/json+v4"})
    public Response getTradeMotTests(@QueryParam("vehicleId") Integer vehicleId,
                                          @QueryParam("number") Long number,
                                          @QueryParam("registration") String registration,
                                          @QueryParam("date") String motTestDate,
                                          @QueryParam("page") Integer page,
                                     ContainerRequestContext requestContext) throws TradeException {

        String awsRequestId = null;
        try {
            logger.info("Entering getTradeMotTests");
            ApiGatewayRequestContext context = (ApiGatewayRequestContext) requestContext.getProperty(
                    RequestReader.API_GATEWAY_CONTEXT_PROPERTY);
            if (context != null) {
                awsRequestId = context.getRequestId();
            }

            VehicleResponseMapper mapper = vehicleResponseMapperFactory.getMapper(this.parseVersionNumber(requestContext));
            List<Vehicle> vehicles;

            if (vehicleId != null) {
                logger.info("Trade API request for vehicle_id = {}", vehicleId);
                vehicles = tradeReadService.getVehiclesByVehicleId(vehicleId);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException(
                            "No MOT Tests found with vehicle id : " + vehicleId.toString(),
                            awsRequestId);
                }

                logger.info("Trade API request for vehicle_id = " + vehicleId.toString() + " returned " + vehicles
                        .size() + " records");
                logger.trace("Exiting getTradeMotTests");

            } else if (number != null) {
                logger.info("Trade API request for mot test number = " + number);
                vehicles = tradeReadService.getVehiclesMotTestsByMotTestNumber(number);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found with number : " + number.toString(),
                            awsRequestId);
                }

                logger.info("Trade API request for mot test number = " + number.toString() + " returned " + vehicles
                        .size() + " records");
                logger.trace("Exiting getTradeMotTests");

            } else if ((registration != null)) {
                logger.info("Trade API request for registration = " + registration);
                vehicles = tradeReadService.getVehiclesByRegistration(registration);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found with vehicle registration : " + registration,
                            awsRequestId);
                }

                logger.info("Trade API request for registration = " + registration + " returned " +
                        vehicles.size() + " records");
                logger.trace("Exiting getTradeMotTests");

            } else if (motTestDate != null) {
                Date date = sdfDate.parse(motTestDate);
                logger.info("Trade API request for date = " + date + " and page = " + page.toString());
                vehicles = tradeReadService.getVehiclesByDatePage(date, page);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found for date : " + motTestDate + " page : " +
                            page.toString(), awsRequestId);
                }

                logger.info("Trade API request for date = " + date + " and page = " + page.toString() + " returned " +
                        vehicles.size() + " records");
                logger.trace("Exiting getTradeMotTests");

            } else if (page != null) {
                logger.info("Trade API request for page = " + page.toString());
                vehicles = tradeReadService.getVehiclesByPage(page);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found for page: " + page.toString(), awsRequestId);
                }

                logger.info("Trade API request for page = " + page.toString() + " returned " + vehicles.size() + " " +
                        "records");
                logger.trace("Exiting getTradeMotTests");

            } else {
                logger.info("Unrecognised parameter set");
                throw new BadRequestException("Unrecognised parameter set", awsRequestId);
            }

            return Response.ok(mapper.map(vehicles)).build();

        } catch (TradeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error("error occurred", e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.info("Exiting getTradeMotTests");
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
            logger.error(e.getMessage(), e);
            logger.trace("Exiting getMakes");
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        }
    }

    /**
     * Retrieve MOT tests in legacy format, based on the provided request.
     *
     * @param registration a vehicle registration as a path parameter
     * @param make a vehicle make as a path parameter.
     * @param requestContext AWS Lambda request context
     * @return Response A list of MOTs in the legacy format.
     * @throws TradeException If there's an error retrieving data or a not-found condition.
     */
    @GET
    @Path("mot-history/{registration}/{make}")
    @Produces("application/json")
    public Response getTradeMotTestsLegacy(@PathParam("registration") String registration,
                                                           @PathParam("make") String make,
                                           ContainerRequestContext requestContext) throws TradeException {
        String awsRequestId = "";
        try {
            logger.info("Entering getMakes");
            ApiGatewayRequestContext context = (ApiGatewayRequestContext) requestContext.getProperty(
                    RequestReader.API_GATEWAY_CONTEXT_PROPERTY);
            if (context != null) {
                awsRequestId = context.getRequestId();
            }

            logger.info("Entering getTradeMotTestsLegacy");
            logger.info("Trade API MOTH request for registration = " + registration + " and make = " + make);
            List<DisplayMotTestItem> items = tradeReadService.getMotTestsByRegistrationAndMake(registration, make);

            if (CollectionUtils.isNullOrEmpty(items)) {
                throw new InvalidResourceException("No MOT Tests found for registration : " + registration + " and make : " + make,
                        awsRequestId);
            }

            logger.info("Trade API MOTR request for registration = " + registration + " and make = " + make + " returned " + items
                    .size() + " records");
            return Response.ok(items).build();
        } catch (TradeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.info("Exiting getTradeMotTestsLegacy");
        }
    }

    private String parseVersionNumber(ContainerRequestContext requestContext) {
        logger.trace("Entering parseVersionNumber");
        if (requestContext.getHeaders() != null && requestContext.getHeaders().containsKey("Accept")) {
            List<String> versionHeaderList = requestContext.getHeaders().get("Accept");
            if (versionHeaderList.size() > 0 && versionHeaderList.get(0) != null) {

                String[] headerSplit = versionHeaderList.get(0).split("\\+");
                String version = headerSplit.length > 1 ? headerSplit[1] : null;

                logger.trace("Exiting parseVersionNumber");
                return version;
            }
        }

        logger.warn("Accept mime type header not set. TAPI will use default API version");
        logger.trace("Exiting parseVersionNumber");
        return null;
    }
}
