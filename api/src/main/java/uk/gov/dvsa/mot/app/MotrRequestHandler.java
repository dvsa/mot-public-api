package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.MotrResponse;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.read.core.MotrReadService;
import uk.gov.dvsa.mot.trade.service.AnnualTestExpiryDateCalculator;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.HgvPsvVehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

@Path("/")
public class MotrRequestHandler extends AbstractRequestHandler {
    private static final Logger logger = Logger.getLogger(MotrRequestHandler.class);

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

    @GET
    @Path("motr/v2/search/registration/{registration}")
    @Produces("application/json")
    public Response getVehicle(@PathParam("registration") String registration,
                               ContainerRequestContext requestContext) throws TradeException {
        try {

            ApiGatewayRequestContext context = (ApiGatewayRequestContext) requestContext.getProperty(
                    RequestReader.API_GATEWAY_CONTEXT_PROPERTY);
            if (context != null) {
                awsRequestId = context.getRequestId();
            }

            logger.info(String.format("Entering MotrRequestHandler, awsRequestId: %s", awsRequestId));

            Optional<MotrResponse> motVehiclesOptional = getMotVehicle(registration);

            if (motVehiclesOptional.isPresent()) {

                return Response.ok(buildMotResponse(motVehiclesOptional.get())).build();
            }

            Optional<MotrResponse> dvlaVehicleOptional = getDvlaVehicle(registration);

            if (dvlaVehicleOptional.isPresent()) {
                MotrResponse dvlaVehicle = dvlaVehicleOptional.get();

                Optional<Vehicle> hgvPsvVehicleOptional = getHgvPsvVehicle(registration);

                if (!hgvPsvVehicleOptional.isPresent()) {
                    logger.info("No HGV/PSV vehicle retrieved");

                    if (dvlaVehicle.getMotTestExpiryDate() != null) {
                        return Response.ok(buildMotResponse(dvlaVehicle)).build();
                    }

                    throw new InvalidResourceException("Could not determine test expiry date for registration " + registration,
                            awsRequestId);
                }

                HgvPsvVehicle hgvPsvVehicle = buildHgvPsvResponse(hgvPsvVehicleOptional.get(), dvlaVehicle);

                return Response.ok(hgvPsvVehicle).build();
            } else {
                logger.error("No MOT Test or DVLA vehicle found for registration.");
                throw new InvalidResourceException("No MOT Test or DVLA vehicle found for registration " + registration,
                        awsRequestId);
            }
        } catch (TradeException e) {
            // no need to log these errors, just throw them back
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e);
            throw new InternalServerErrorException(e, awsRequestId);
        } finally {
            logger.trace("Exiting getVehicle");
        }
    }

    private Optional<MotrResponse> getMotVehicle(String registration) throws Exception {
        logger.info(String.format("Retrieving vehicle by registration: %s", registration));

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
        logger.info(String.format("Retrieving dvla vehicle by registration: %s", registration));

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

    private HgvPsvVehicle buildHgvPsvResponse(Vehicle vehicle, MotrResponse dvlaVehicle) throws Exception {
        HgvPsvVehicle hgvPsvVehicle = new HgvPsvVehicle();
        AnnualTestExpiryDateCalculator annualTestExpiryDateCalculator = new AnnualTestExpiryDateCalculator();

        hgvPsvVehicle.setMake(vehicle.getMake());
        hgvPsvVehicle.setModel(vehicle.getModel());
        hgvPsvVehicle.setRegistration(vehicle.getVehicleIdentifier());
        hgvPsvVehicle.setVehicleType(vehicle.getVehicleType());
        hgvPsvVehicle.setManufactureYear(vehicle.getYearOfManufacture().toString());
        hgvPsvVehicle.setDvlaId(dvlaVehicle.getDvlaId());
        hgvPsvVehicle.setMotTestExpiryDate(annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle, awsRequestId));

        if (vehicle.getTestHistory() != null && vehicle.getTestHistory().length > 0) {
            TestHistory[] testHistory = vehicle.getTestHistory();
            hgvPsvVehicle.setMotTestNumber(testHistory[testHistory.length - 1].getTestCertificateSerialNo());
        }

        return hgvPsvVehicle;
    }

    private HgvPsvVehicle buildMotResponse(MotrResponse response) {
        HgvPsvVehicle hgvPsvVehicle = new HgvPsvVehicle();

        hgvPsvVehicle.setDvlaId(response.getDvlaId());
        hgvPsvVehicle.setMake(response.getMake());
        hgvPsvVehicle.setManufactureYear(response.getManufactureYear());
        hgvPsvVehicle.setModel(response.getModel());
        hgvPsvVehicle.setMotTestExpiryDate(response.getMotTestExpiryDate());
        hgvPsvVehicle.setRegistration(response.getRegistration());
        hgvPsvVehicle.setMotTestNumber(response.getMotTestNumber());
        hgvPsvVehicle.setVehicleType("MOT");

        return hgvPsvVehicle;
    }
}
