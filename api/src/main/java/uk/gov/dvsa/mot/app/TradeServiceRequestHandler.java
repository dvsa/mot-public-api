package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.util.CollectionUtils;
import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.security.ParamObfuscator;
import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.ServiceTemporarilyUnavailableException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.api.TradeServiceRequest;
import uk.gov.dvsa.mot.trade.api.Vehicle;
import uk.gov.dvsa.mot.trade.api.response.mapper.VehicleResponseMapper;
import uk.gov.dvsa.mot.trade.api.response.mapper.VehicleResponseMapperFactory;
import uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle.SearchVehicleResponseMapper;
import uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle.SearchVehicleResponseMapperFactory;
import uk.gov.dvsa.mot.trade.read.core.TradeAnnualTestsReadService;
import uk.gov.dvsa.mot.trade.read.core.TradeReadService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static uk.gov.dvsa.mot.app.ConfigManager.getEnvironmentVariable;

/**
 * Entry point class for Lambdas.
 * <p>
 * Various public methods in this class are called on Lambda invocation for trade service queries.
 */
@Path("/")
public class TradeServiceRequestHandler extends AbstractRequestHandler {
    private static final Logger logger = LogManager.getLogger(TradeServiceRequestHandler.class);
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

    private TradeReadService tradeReadService;
    private TradeAnnualTestsReadService tradeAnnualTestsReadService;
    private VehicleResponseMapperFactory vehicleResponseMapperFactory;
    private SearchVehicleResponseMapperFactory hgvResponseMapperFactory;

    public TradeServiceRequestHandler() {

        super();
    }

    public TradeServiceRequestHandler(boolean injectSelf) {

        super(injectSelf);
    }

    /**
     * Set the read service which will be used to make queries required by this class.
     *
     * @param tradeReadService an instance of something which implements {@link TradeReadService}
     */
    @Inject
    public void setTradeReadService(TradeReadService tradeReadService) {

        logger.trace("Entering setTradeReadService");
        this.tradeReadService = tradeReadService;
        logger.trace("Exiting setTradeReadService");
    }

    /**
     * Sets the read service for Annual Tests which will be used to make queries required by this class.
     *
     * @param tradeAnnualTestsReadService an instance of something which implements {@link TradeAnnualTestsReadService}
     */
    @Inject
    public void setTradeAnnualTestReadService(TradeAnnualTestsReadService tradeAnnualTestsReadService) {
        logger.trace("Entering setTradeReadService");
        this.tradeAnnualTestsReadService = tradeAnnualTestsReadService;
        logger.trace("Exiting setTradeReadService");
    }

    @Inject
    public void setVehicleResponseMapperFactory(VehicleResponseMapperFactory vehicleResponseMapperFactory) {

        this.vehicleResponseMapperFactory = vehicleResponseMapperFactory;
    }

    @Inject
    public void setHgvResponseMapperFactory(SearchVehicleResponseMapperFactory hgvResponseMapperFactory) {

        this.hgvResponseMapperFactory = hgvResponseMapperFactory;
    }

    /**
     * Get MOT tests as per the provided request, grouped by vehicle.
     *
     * @param vehicleId      query parameter
     * @param number         query parameter
     * @param registration   query parameter
     * @param motTestDate    date query parameter
     * @param page           query parameter
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
            "application/json+v4",
            "application/json+v5",
            "application/json+v6",
            "application/json+v7"})
    public Response getTradeMotTests(
            @QueryParam("vehicleId") String vehicleId,
            @QueryParam("number") Long number,
            @QueryParam("registration") String registration,
            @QueryParam("date") String motTestDate,
            @QueryParam("page") Integer page,
            @javax.ws.rs.core.Context ContainerRequestContext requestContext
    ) throws TradeException {

        String awsRequestId = null;
        try {
            logger.trace("Entering getTradeMotTests");
            ApiGatewayRequestContext context = (ApiGatewayRequestContext) requestContext.getProperty(
                    RequestReader.API_GATEWAY_CONTEXT_PROPERTY);
            if (context != null) {
                awsRequestId = context.getRequestId();
            }

            VehicleResponseMapper mapper = vehicleResponseMapperFactory.getMapper(this.parseVersionNumber(requestContext));
            List<Vehicle> vehicles;

            if (vehicleId != null && !vehicleId.isEmpty()) {
                logger.info("Trade API request for vehicle_id = {}", vehicleId);

                String deobfuscatedVehicleId;
                try {
                    deobfuscatedVehicleId = ParamObfuscator.deobfuscate(vehicleId);

                } catch (ParamObfuscator.ObfuscationException e) {

                    // Check to see if exception was caused by potentially bad user input/ciphertext.
                    // Then throw a 404 instead.. Else throw the 500..
                    if (e.getCause() instanceof IllegalArgumentException
                            || e.getCause() instanceof IllegalBlockSizeException
                            || e.getCause() instanceof BadPaddingException) {
                        throw new InvalidResourceException(
                                "No MOT Tests found with vehicle id : " + vehicleId,
                                awsRequestId);
                    }
                    throw e;
                }

                int decodedVehicleId = Integer.parseInt(deobfuscatedVehicleId);
                logger.trace("Decoded vehicle_id to {}", decodedVehicleId);
                vehicles = tradeReadService.getVehiclesByVehicleId(decodedVehicleId);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException(
                            "No MOT Tests found with vehicle id : " + vehicleId,
                            awsRequestId);
                }

                logger.debug("Trade API request for vehicle_id = {} returned {} records",
                        Integer.toString(decodedVehicleId), vehicles.size());
                logger.trace("Exiting getTradeMotTests");

            } else if (registration != null) {
                logger.info("Trade API request for registration {}", registration);
                vehicles = tradeReadService.getVehiclesByRegistration(registration);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found with vehicle registration : " + registration,
                            awsRequestId);
                }

                logger.debug("Trade API request for registration = {} returned {} records", registration, vehicles.size());
                logger.trace("Exiting getTradeMotTests");

            } else if (motTestDate != null) {
                Date today = new Date();
                Date date = sdfDate.parse(motTestDate);

                long millisecondDiff = Math.abs(today.getTime() - date.getTime());
                long diff = TimeUnit.DAYS.convert(millisecondDiff, TimeUnit.MILLISECONDS);

                // If the difference is greater than 5 weeks, then return a Bad Request response
                if (diff > 35) {
                    throw new BadRequestException("Date exceeds 5 weeks from current date", awsRequestId);
                }

                logger.info("Trade API request for date = {} and page {}", date, page);
                vehicles = tradeReadService.getVehiclesByDatePage(date, page);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found for date : " + motTestDate + " page : " +
                            page.toString(), awsRequestId);
                }

                logger.debug("Trade API request for date = {} and page {} returned {} records", date, page, vehicles.size());
                logger.trace("Exiting getTradeMotTests");

            } else if (page != null) {
                logger.info("Trade API request for page = {}", page);
                vehicles = tradeReadService.getVehiclesByPage(page);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException("No MOT Tests found for page: " + page.toString(), awsRequestId);
                }

                logger.debug("Trade API request for page = {} returned {} records", page, vehicles.size());
                logger.trace("Exiting getTradeMotTests");

            } else {
                logger.warn("Unrecognised parameter set");
                throw new BadRequestException("Unrecognised parameter set", awsRequestId);
            }

            return Response.ok(mapper.map(vehicles))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (TradeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error("error occurred", e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.trace("Exiting getTradeMotTests");
        }
    }

    /**
     * Get HGV/PSV/Trailer Annual Test History as per the provided request, grouped by vehicle.
     *
     * @param registrationsOrVins Query parameter: accepts 1 or more VRMs or VINs as a comma separated list.
     * @return A list of {@link Vehicle}, with each vehicle populated with its annual tests matching the registrations parameters.
     * @throws TradeException Under various error conditions, including no vehicles are found. It is expected that the surrounding
     *                        integration will interpret this exception appropriately.
     */
    @GET
    @Path("trade/vehicles/annual-tests")
    @Produces({
            MediaType.APPLICATION_JSON,
            MediaType.WILDCARD,
            MediaType.APPLICATION_JSON + "+v6",
            MediaType.APPLICATION_JSON + "+v7"
            })
    public Response getTradeAnnualTests(
            @QueryParam("registrations") String registrations,
            @QueryParam("registrationsOrVins") String registrationsOrVins,
            @javax.ws.rs.core.Context ContainerRequestContext requestContext
    ) throws TradeException {

        String versionNumber = this.parseVersionNumber(requestContext);

        String vehicleIdentifiers = "v7".equals(versionNumber) ? registrationsOrVins : registrations;
        String errorMessageIdentifiers = "v7".equals(versionNumber) ? "registrations / VINs" : "registrations";
        String queryParamName = "v7".equals(versionNumber) ? "registrationsOrVins" : "registrations";

        String awsRequestId = null;
        SearchVehicleResponseMapper mapper = hgvResponseMapperFactory.getMapper(versionNumber);
        List<uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle> vehicles;

        try {
            logger.trace("Entering getAnnualTests");
            ApiGatewayRequestContext context = (ApiGatewayRequestContext) requestContext.getProperty(
                    RequestReader.API_GATEWAY_CONTEXT_PROPERTY);
            if (context != null) {
                awsRequestId = context.getRequestId();
            }

            if (vehicleIdentifiers != null) {

                int csvMaxQueryableVehicleIdentifiers =
                        Integer.parseInt(getEnvironmentVariable(ConfigKeys.AnnualTestsMaxQueryableVehicleIdentifiers, false));

                if (csvMaxQueryableVehicleIdentifiers == 0) {
                    throw new ServiceTemporarilyUnavailableException(String.format("Cannot query annual test %s at this time",
                            errorMessageIdentifiers), awsRequestId);
                }

                if (vehicleIdentifiers.isEmpty()) {
                    throw new BadRequestException(
                            String.format("Query parameter %s expects one or more %s separated by commas",
                                    queryParamName, errorMessageIdentifiers), awsRequestId);
                }

                HashSet<String> requestedVehicleIdentifiers = parseIdentifiers(vehicleIdentifiers);
                if (requestedVehicleIdentifiers.size() > csvMaxQueryableVehicleIdentifiers) {
                    throw new BadRequestException(
                            String.format("You have defined %d %s; the limit is %d per request",
                                    requestedVehicleIdentifiers.size(),
                                    errorMessageIdentifiers,
                                    csvMaxQueryableVehicleIdentifiers
                            ),
                            awsRequestId
                    );
                }

                vehicles = tradeAnnualTestsReadService.getAnnualTests(requestedVehicleIdentifiers);

                if (CollectionUtils.isNullOrEmpty(vehicles)) {
                    throw new InvalidResourceException(
                            String.format("No annual tests found for %s: %s", errorMessageIdentifiers, vehicleIdentifiers),
                            awsRequestId
                    );
                }

            } else {
                logger.warn("Unrecognised parameter set");
                throw new BadRequestException("Unrecognised parameter set", awsRequestId);
            }
        } catch (TradeException e) {
            logger.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // Log unhandled exceptions then throw a 500 Internal Server Error
            logger.error("Unhandled error has occurred: ", e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.trace("Exiting getAnnualTests");
        }

        return Response.ok(mapper.map(vehicles))
                .type(MediaType.APPLICATION_JSON)
                .build();
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
            return makes;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException(e, context.getAwsRequestId());
        } finally {
            logger.trace("Exiting getMakes");
        }
    }

    /**
     * Retrieve MOT tests in legacy format, based on the provided request.
     *
     * @param registration   a vehicle registration as a path parameter
     * @param make           a vehicle make as a path parameter.
     * @param requestContext AWS Lambda request context
     * @return Response A list of MOTs in the legacy format.
     * @throws TradeException If there's an error retrieving data or a not-found condition.
     */
    @GET
    @Path("mot-history/{registration}/{make}")
    @Produces("application/json")
    public Response getTradeMotTestsLegacy(
            @PathParam("registration") String registration,
            @PathParam("make") String make,
            @javax.ws.rs.core.Context ContainerRequestContext requestContext
    ) throws TradeException {
        String awsRequestId = "";
        try {
            logger.trace("Entering getMakes");
            ApiGatewayRequestContext context = (ApiGatewayRequestContext) requestContext.getProperty(
                    RequestReader.API_GATEWAY_CONTEXT_PROPERTY);
            if (context != null) {
                awsRequestId = context.getRequestId();
            }

            logger.trace("Entering getTradeMotTestsLegacy");
            logger.info("Trade API MOTH request for registration = {} and make = {} ", registration, make);
            List<DisplayMotTestItem> items = tradeReadService.getMotTestsByRegistrationAndMake(registration, make);

            if (CollectionUtils.isNullOrEmpty(items)) {
                throw new InvalidResourceException("No MOT Tests found for registration : " + registration + " and make : " + make,
                        awsRequestId);
            }

            logger.debug("Trade API MOTR request for registration = {} and make {} returned {} records", registration, make, items.size());
            return Response.ok(items)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (TradeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.trace("Exiting getTradeMotTestsLegacy");
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

    /**
     * This method accepts the query string of VRMs or VINs (separated by commas) and parses them into
     * a trimmed set.
     *
     * Bonus of using {@link HashSet} is that it ensures no duplicate VRMs or VINs.
     *
     * @param vehicleIdentifiers VRMs or VINs provided as a comma separated list
     * @return the set of processed VRMs o VINs
     */
    private HashSet<String> parseIdentifiers(String vehicleIdentifiers) {
        HashSet<String> processedIdentifiers = new HashSet<>();
        StringTokenizer st = new StringTokenizer(vehicleIdentifiers, ",");

        while (st.hasMoreTokens()) {
            processedIdentifiers.add(
                    parseIdentifier(st.nextToken())
            );
        }

        return processedIdentifiers;
    }

    /**
     * This method will parse the VRM or VIN; currently by trimming excess whitespace.
     *
     * @param vehicleIdentifier the VRM or VIN to be parsed
     * @return parsed/cleaned VRM or VIN
     */
    private String parseIdentifier(String vehicleIdentifier) {
        return vehicleIdentifier.trim();
    }
}
