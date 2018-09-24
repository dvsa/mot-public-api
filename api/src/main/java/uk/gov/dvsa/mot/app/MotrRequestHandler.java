package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.motr.model.VehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.service.MotrVehicleHistoryProvider;
import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.MotrResponse;
import uk.gov.dvsa.mot.trade.api.TradeException;

import java.time.format.DateTimeFormatter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


@Path("/")
public class MotrRequestHandler extends AbstractRequestHandler {
    private static final Logger logger = LogManager.getLogger(MotrRequestHandler.class);

    private MotrVehicleHistoryProvider motrVehicleProvider;
    private String awsRequestId;

    public MotrRequestHandler() {
        super();
    }

    public MotrRequestHandler(boolean injectSelf) {
        super(injectSelf);
    }

    @Inject
    public MotrRequestHandler setMotrVehicleProvider(MotrVehicleHistoryProvider motrVehicleProvider) {
        this.motrVehicleProvider = motrVehicleProvider;
        return this;
    }

    /**
     * Search for any vehicle by VRM
     *
     * @param registration
     * @param requestContext
     * @return Response
     * @throws TradeException
     */
    @GET
    @Path("motr/v2/search/registration/{registration}")
    @Produces("application/json")
    public Response getVehicle(
            @PathParam("registration") String registration,
            @Context ContainerRequestContext requestContext
    ) throws TradeException {
        try {
            readAwsRequestId(requestContext);
            logger.trace("Entering MotrRequestHandler");
            logger.info("MOTR request for vehicle with registraiton = {}", registration);

            requireRegistration(registration);
            VehicleWithLatestTest vehicleWithTest = motrVehicleProvider.searchVehicleByRegistration(registration);
            return Response.ok(mapVehicleToDto(vehicleWithTest)).build();

        } catch (TradeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.trace("Exiting getVehicle");
        }
    }

    /**
     * Search for commercial vehicles by registration. These include HGV, PSV
     * (and in the future possibly trailers by trailer ID passed as registration)
     *
     * @param registration vehicle vrm or trailer id
     * @param requestContext
     * @return
     * @throws TradeException
     */
    @GET
    @Path("motr/v2/search/commercial/registration/{registration}")
    @Produces("application/json")
    public Response getCommercialVehicle(
            @PathParam("registration") String registration,
            @Context ContainerRequestContext requestContext
    ) throws TradeException {
        try {
            logger.trace("Entering getCommercialVehicle");
            logger.info("MOTR request for commercial vehicle with registraiton = {}", registration);
            readAwsRequestId(requestContext);

            VehicleWithLatestTest vehicle = motrVehicleProvider.searchForCommercialVehicleByRegistration(registration);
            return Response.ok(mapVehicleToDto(vehicle)).build();
        } catch (TradeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.trace("Exiting getCommercialVehicle");
        }
    }

    @GET
    @Path("motr/v2/search/dvla-id/{id}")
    @Produces("application/json")
    public Response getVehicleByDvlaId(
            @PathParam("id") Integer dvlaVehicleId,
            @Context ContainerRequestContext requestContext
    ) throws TradeException {
        try {
            logger.trace("Entering getVehicleByDvlaId");
            logger.info("MOTR request for vehicle with dvla_id = {}", dvlaVehicleId);
            readAwsRequestId(requestContext);
            requireDvlaVehicleId(dvlaVehicleId);

            VehicleWithLatestTest vehicle = motrVehicleProvider.getDvlaVehicleWithTestByDvlaVehicleId(dvlaVehicleId);
            logger.debug("Public API MOTR request for DVLA id = {} returned 1 record", dvlaVehicleId);
            return Response.ok(mapVehicleToDto(vehicle)).build();
        } catch (TradeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.trace("Exiting getVehicleByDvlaId");
        }
    }

    @GET
    @Path("motr/v2/search/mot-test/{motTestNumber}")
    @Produces("application/json")
    public Response getVehicleByMotTestNumber(
            @PathParam("motTestNumber") Long motTestNumber,
            @Context ContainerRequestContext requestContext
    ) throws TradeException {
        try {
            logger.trace("Entering getVehicleByMotTestNumber");
            logger.info("MOTR request for vehicle with MOT test number = {}", motTestNumber);
            readAwsRequestId(requestContext);
            requireMotTestNumber(motTestNumber);

            VehicleWithLatestTest vehicle = motrVehicleProvider.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);
            logger.debug("Public API MOTR request for mot test number = {} returned 1 record", motTestNumber);
            return Response.ok(mapVehicleToDto(vehicle)).build();
        } catch (TradeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.trace("Exiting getVehicleByMotTestNumber");
        }
    }

    private void readAwsRequestId(@Context ContainerRequestContext requestContext) {
        ApiGatewayRequestContext context = (ApiGatewayRequestContext) requestContext.getProperty(
                RequestReader.API_GATEWAY_CONTEXT_PROPERTY);
        if (context != null) {
            awsRequestId = context.getRequestId();
        }
    }

    private void requireRegistration(String registration) throws InvalidResourceException {
        if (Strings.isNullOrEmpty(registration)) {
            throw new InvalidResourceException("Invalid Parameters - missing registration");
        }
    }

    private void requireDvlaVehicleId(Integer dvlaVehicleId) throws BadRequestException {
        if (dvlaVehicleId == null) {
            throw new BadRequestException("Invalid Parameters - missing dvla_vehicle_id", awsRequestId);
        }
    }

    private void requireMotTestNumber(Long motTestNumber) throws BadRequestException {
        if (motTestNumber == null) {
            throw new BadRequestException("Invalid Parameters - missing MOT test number", awsRequestId);
        }
    }

    private MotrResponse mapVehicleToDto(VehicleWithLatestTest vehicle) {
        MotrResponse motrResponse = new MotrResponse();

        motrResponse.setRegistration(vehicle.getRegistration());
        motrResponse.setMake(vehicle.getMake());
        motrResponse.setModel(vehicle.getModel());
        motrResponse.setPrimaryColour(vehicle.getPrimaryColour());

        if (!"Not Stated".equalsIgnoreCase(vehicle.getSecondaryColour())) {
            motrResponse.setSecondaryColour(vehicle.getSecondaryColour());
        }

        if (vehicle.hasManufactureYear()) {
            motrResponse.setManufactureYear(vehicle.getManufactureYear().toString());
        }

        if (vehicle.hasTestNumber()) {
            motrResponse.setMotTestNumber(vehicle.getTestNumber());
        }

        if (vehicle.hasTestExpiryDate()) {
            motrResponse.setMotTestExpiryDate(vehicle.getTestExpiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        motrResponse.setDvlaId(vehicle.getDvlaVehicleId());

        motrResponse.setVehicleType(vehicle.getVehicleType());

        return motrResponse;
    }
}
