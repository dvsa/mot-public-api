package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.persist.model.DvlaVehicle;
import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.MotrResponse;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.trade.read.core.MotrReadService;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.HgvPsvVehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

@Path("/")
public class MotrRequestHandler extends AbstractRequestHandler {
    private static final String HGV_VEHICLE_TYPE = "HGV";
    private static final Logger logger = Logger.getLogger(MotrRequestHandler.class);

    private MotrReadService motrReadService;
    private VehicleReadService vehicleReadService;
    private HgvVehicleProvider hgvVehicleProvider;
    private String awsRequestId;

    public MotrRequestHandler() {
        super();
    }

    public MotrRequestHandler(boolean injectSelf) {
        super(injectSelf);
    }

    @Inject
    public void setVehicleReadService(VehicleReadService vehicleReadService) {
        this.vehicleReadService = vehicleReadService;
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
            Context context = (Context) requestContext.getProperty(RequestReader.LAMBDA_CONTEXT_PROPERTY);

            if (context != null) {
                awsRequestId = context.getAwsRequestId();
            }

            logger.info(String.format("Entering MotrRequestHandler, awsRequestId: %s", awsRequestId));

            if (Strings.isNullOrEmpty(registration)) {
                logger.error("Registration param is missing");
                throw new BadRequestException("Registration param is missing", awsRequestId);
            }

            Optional<MotrResponse> motVehiclesOptional = getMotVehicle(registration);

            if (motVehiclesOptional.isPresent()) {

                return Response.ok(setResponseMotVehicle(motVehiclesOptional.get())).build();
            }

            Optional<DvlaVehicle> dvlaVehicleOptional = getDvlaVehicle(registration);

            if (dvlaVehicleOptional.isPresent()) {
                DvlaVehicle dvlaVehicle = dvlaVehicleOptional.get();

                Optional<Vehicle> hgvPsvVehicleOptional = getHgvPsvVehicle(registration);

                if (!hgvPsvVehicleOptional.isPresent()) {
                    logger.error("No HGV/PSV vehicle retrieved");
                    throw new InvalidResourceException("No HGV/PSV vehicle retrieved", awsRequestId);
                }

                HgvPsvVehicle hgvPsvVehicle = setResponseHgvVehicle(hgvPsvVehicleOptional.get(), dvlaVehicle);

                return Response.ok(hgvPsvVehicle).build();
            } else {
                logger.error("No MOT Test or DVLA vehicle found for registration.");
                throw new InvalidResourceException("No MOT Test or DVLA vehicle found for registration " + registration,
                        awsRequestId);
            }
        } catch (TradeException e) {
            // no need to log these errors, just throw them back
            logger.trace("Exiting getVehicle");
            throw e;
        } catch (Exception e) {
            // log all unhandled exceptions and throw an internal server error
            logger.error(e);
            logger.trace("Exiting getVehicle");
            throw new InternalServerErrorException(e, awsRequestId);
        }
    }

    private Optional<MotrResponse> getMotVehicle(String registration) throws Exception {
        logger.info(String.format("Retrieving vehicle by registration: %s", registration));

        MotrResponse motVehicles;

        try {
            motVehicles = motrReadService.getLatestMotTestByRegistration(registration, false);
        } catch (Exception e) {
            logger.error(String.format("There was an error retrieving mot vehicle with reg: %s",
                    registration), e);
            throw new InvalidResourceException("There was an error retrieving mot vehicle", awsRequestId);
        }

        if (motVehicles == null) {
            return Optional.empty();
        }

        return Optional.of(motVehicles);
    }



    private Optional<DvlaVehicle> getDvlaVehicle(String registration) throws Exception {
        logger.info(String.format("Retrieving dvla vehicle by registration: %s", registration));

        DvlaVehicle dvlaVehicle;

        try {
            dvlaVehicle = vehicleReadService.getDvlaVehicleByRegistrationWithVin(registration);
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
            throw new InvalidResourceException("There was an error retrieving the HGV/PSV history", awsRequestId);
        }

        return Optional.ofNullable(foundVehicle);
    }

    private HgvPsvVehicle setResponseHgvVehicle(Vehicle vehicle, DvlaVehicle dvlaVehicle) throws Exception {
        HgvPsvVehicle hgvPsvVehicle = new HgvPsvVehicle();

        hgvPsvVehicle.setMake(vehicle.getMake());
        hgvPsvVehicle.setModel(vehicle.getModel());
        hgvPsvVehicle.setPrimaryColour(dvlaVehicle.getColour1().getName());
        hgvPsvVehicle.setRegistration(dvlaVehicle.getRegistration());
        hgvPsvVehicle.setVin(dvlaVehicle.getVin());
        hgvPsvVehicle.setVehicleType(vehicle.getVehicleType());
        hgvPsvVehicle.setManufactureYear(vehicle.getYearOfManufacture().toString());
        hgvPsvVehicle.setDvlaId(Integer.toString(dvlaVehicle.getDvlaVehicleId()));
        hgvPsvVehicle.setMotTestExpiryDate(determineAnnualTestExpiryDate(vehicle));

        if (dvlaVehicle.getColour2() != null) {
            hgvPsvVehicle.setSecondaryColour(dvlaVehicle.getColour2().getName());
        }

        if (vehicle.getTestHistory() != null && vehicle.getTestHistory().length > 0) {
            TestHistory[] testHistory = vehicle.getTestHistory();
            hgvPsvVehicle.setMotTestNumber(testHistory[testHistory.length - 1].getTestCertificateSerialNo());
        }

        return hgvPsvVehicle;
    }

    private String determineAnnualTestExpiryDate(Vehicle vehicle) throws Exception {
        DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate datetime;

        String date = vehicle.getTestCertificateExpiryDate();

        if (Strings.isNullOrEmpty(date)) {
            logger.info("Test Certificate Expiry date is null or empty");

            String registrationDate = vehicle.getRegistrationDate();

            if (Strings.isNullOrEmpty(registrationDate)) {
                logger.error("Registration date is null or empty");
                throw new BadRequestException("Registration date is null or empty", awsRequestId);
            }

            datetime = LocalDate.parse(registrationDate, oldPattern);

            if (vehicle.getVehicleType().equals(HGV_VEHICLE_TYPE)) {
                datetime = datetime.plusMonths(12);
                datetime = datetime.with(TemporalAdjusters.lastDayOfMonth());
            } else {
                datetime = datetime.plusMonths(12);
            }
        } else {
            datetime = LocalDate.parse(date, oldPattern);
        }

        return datetime.format(newPattern);
    }

    private HgvPsvVehicle setResponseMotVehicle(MotrResponse response) {
        HgvPsvVehicle hgvPsvVehicle = new HgvPsvVehicle();

        hgvPsvVehicle.setDvlaId(response.getDvlaId());
        hgvPsvVehicle.setMake(response.getMake());
        hgvPsvVehicle.setManufactureYear(response.getManufactureYear());
        hgvPsvVehicle.setModel(response.getModel());
        hgvPsvVehicle.setMotTestExpiryDate(response.getMotTestExpiryDate());
        hgvPsvVehicle.setPrimaryColour(response.getPrimaryColour());
        hgvPsvVehicle.setRegistration(response.getRegistration());
        hgvPsvVehicle.setSecondaryColour(response.getSecondaryColour());
        hgvPsvVehicle.setMotTestNumber(response.getMotTestNumber());
        hgvPsvVehicle.setVehicleType("MOT");

        return hgvPsvVehicle;
    }
}
