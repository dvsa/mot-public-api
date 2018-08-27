package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.MotrResponse;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.read.core.MotrReadService;
import uk.gov.dvsa.mot.trade.service.AnnualTestExpiryDateCalculator;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.validation.TrailerIdFormat;

import java.util.Optional;

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

    private MotrReadService motrReadService;
    private HgvVehicleProvider hgvVehicleProvider;
    private String awsRequestId;

    public MotrRequestHandler() {
        super();
    }

    public MotrRequestHandler(boolean injectSelf) {
        super(injectSelf);
    }

    @Inject
    public void setHgvVehicleProvider(HgvVehicleProvider hgvVehicleProvider) {
        this.hgvVehicleProvider = hgvVehicleProvider;
    }

    @Inject
    public void setMotrReadService(MotrReadService motrReadService) {
        this.motrReadService = motrReadService;
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

            Optional<MotrResponse> motVehiclesOptional = getMotVehicle(registration);

            if (motVehiclesOptional.isPresent()) {

                return Response.ok(buildMotResponse(motVehiclesOptional.get())).build();
            }

            Optional<MotrResponse> dvlaVehicleOptional = getDvlaVehicle(registration);

            if (shouldGetHgvPsvHistory(dvlaVehicleOptional, registration)) {

                Optional<Vehicle> hgvPsvVehicleOptional = getHgvPsvVehicle(registration);

                if (!hgvPsvVehicleOptional.isPresent()) {
                    logger.debug("No HGV/PSV vehicle retrieved");

                    if (dvlaVehicleOptional.isPresent() && dvlaVehicleOptional.get().getMotTestExpiryDate() != null) {
                        return Response.ok(buildMotResponse(dvlaVehicleOptional.get())).build();
                    }

                    throw new InvalidResourceException("Could not determine test expiry date for registration " + registration,
                            awsRequestId);
                }

                MotrResponse hgvPsvVehicle = buildHgvPsvResponse(hgvPsvVehicleOptional.get(), dvlaVehicleOptional);

                return Response.ok(hgvPsvVehicle).build();
            } else {
                throw new InvalidResourceException("No MOT Test or DVLA vehicle found for registration " + registration,
                        awsRequestId);
            }
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
     * @param registration
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

            logger.trace(String.format("Entering MotrRequestHandler.getCommercialVehicle, awsRequestId: %s", awsRequestId));

            Optional<MotrResponse> dvlaVehicleOptional = getDvlaVehicle(registration);

            if (shouldGetHgvPsvHistory(dvlaVehicleOptional, registration)) {

                Optional<Vehicle> hgvPsvVehicleOptional = getHgvPsvVehicle(registration);

                if (!hgvPsvVehicleOptional.isPresent()) {
                    logger.debug("No HGV/PSV vehicle retrieved");
                    throw new InvalidResourceException(String.format("No HGV/PSV vehicle found for registration %s", registration),
                            awsRequestId);
                }

                MotrResponse hgvPsvVehicle = buildHgvPsvResponse(hgvPsvVehicleOptional.get(), dvlaVehicleOptional);
                return Response.ok(hgvPsvVehicle).build();
            } else {
                throw new InvalidResourceException(String.format("No DVLA vehicle found for registration %s", registration),
                        awsRequestId);
            }
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

            if (dvlaVehicleId != null) {
                logger.debug(String.format("Public API MOTR request for DVLA id = %d", dvlaVehicleId));

                MotrResponse response = motrReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

                if (response == null) {
                    throw new InvalidResourceException(
                            String.format("No MOT Test or DVLA vehicle found for DVLA vehicle id %d", dvlaVehicleId),
                            awsRequestId);
                }

                logger.debug(String.format("Public API MOTR request for DVLA id = %d returned 1 record", dvlaVehicleId));
                return Response.ok(buildMotResponse(response)).build();
            } else {
                throw new BadRequestException("Invalid Parameters", awsRequestId);
            }
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
            readAwsRequestId(requestContext);

            if (motTestNumber != null) {
                logger.debug(String.format("Public API MOTR request for mot test number = %d", motTestNumber));

                MotrResponse motrResponse =
                        motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

                if (motrResponse == null) {
                    logger.debug(String.format("getVehicleByMotTestNumber for number = %d found 0", motTestNumber));
                    throw new InvalidResourceException(String.format("No MOT Tests found with number: %d", motTestNumber),
                            awsRequestId);
                }

                logger.debug(String.format("Public API MOTR request for mot test number = %s returned 1 record", awsRequestId));
                return Response.ok(buildMotResponse(motrResponse)).build();
            } else {
                throw new BadRequestException("Invalid Parameters", awsRequestId);
            }
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

    private Optional<MotrResponse> getMotVehicle(String registration) throws Exception {
        logger.trace(String.format("Retrieving vehicle by registration: %s", registration));

        MotrResponse motVehicles;

        try {
            motVehicles = motrReadService.getLatestMotTestByRegistration(registration);
        } catch (Exception e) {
            logger.error(String.format("There was an error retrieving mot vehicle with reg: %s",
                    registration), e);
            throw new InvalidResourceException("There was an error retrieving mot vehicle", awsRequestId);
        }

        return Optional.ofNullable(motVehicles);
    }

    private Optional<MotrResponse> getDvlaVehicle(String registration) throws Exception {
        logger.trace(String.format("Retrieving dvla vehicle by registration: %s", registration));

        MotrResponse dvlaVehicle;

        try {
            dvlaVehicle = motrReadService.getLatestMotTestForDvlaVehicleByRegistration(registration);
        } catch (Exception e) {
            logger.error(String.format("There was an error retrieving dvla vehicle with reg: %s",
                    registration), e);
            throw new InvalidResourceException("There was an error retrieving dvla vehicle", awsRequestId);
        }

        return Optional.ofNullable(dvlaVehicle);
    }

    private Optional<Vehicle> getHgvPsvVehicle(String registration) throws Exception {
        Vehicle foundVehicle = null;

        try {
            if (!Strings.isNullOrEmpty(registration)) {
                foundVehicle = hgvVehicleProvider.getVehicle(registration);
            } else {
                logger.warn("Passed registration was empty or null");
            }
        } catch (Exception e) {
            logger.error("There was an error retrieving the HGV/PSV history", e);
            throw new InternalServerErrorException("There was an error retrieving the HGV/PSV history", awsRequestId);
        }

        return Optional.ofNullable(foundVehicle);
    }

    private MotrResponse buildHgvPsvResponse(Vehicle vehicle, Optional<MotrResponse> dvlaVehicle) throws Exception {
        MotrResponse hgvPsvVehicle = new MotrResponse();
        AnnualTestExpiryDateCalculator annualTestExpiryDateCalculator = new AnnualTestExpiryDateCalculator();

        hgvPsvVehicle.setMake(vehicle.getMake());
        hgvPsvVehicle.setModel(vehicle.getModel());
        hgvPsvVehicle.setRegistration(vehicle.getVehicleIdentifier());
        hgvPsvVehicle.setVehicleType(vehicle.getVehicleType());
        if (vehicle.getYearOfManufacture() != null) {
            hgvPsvVehicle.setManufactureYear(vehicle.getYearOfManufacture().toString());
        }
        if (dvlaVehicle.isPresent()) {
            hgvPsvVehicle.setDvlaId(dvlaVehicle.get().getDvlaId());
        }
        hgvPsvVehicle.setMotTestExpiryDate(annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle));

        if (vehicle.getTestHistory() != null && vehicle.getTestHistory().length > 0) {
            TestHistory[] testHistory = vehicle.getTestHistory();
            hgvPsvVehicle.setMotTestNumber(testHistory[testHistory.length - 1].getTestCertificateSerialNo());
        }

        return hgvPsvVehicle;
    }

    private MotrResponse buildMotResponse(MotrResponse response) {
        response.setVehicleType("MOT");

        return response;
    }

    private boolean shouldGetHgvPsvHistory(Optional<MotrResponse> dvlaVehicleOptional, String vrm) {
        TrailerIdFormat trailerIdFormat = new TrailerIdFormat();

        return dvlaVehicleOptional.isPresent()
                || trailerIdFormat.matches(vrm);
    }
}
