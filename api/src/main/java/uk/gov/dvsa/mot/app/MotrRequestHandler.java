package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.persist.model.DvlaVehicle;
import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.api.InternalServerErrorException;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.HgvPsvVehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Path("/")
public class MotrRequestHandler extends AbstractRequestHandler {
    private static final String HGV_VEHICLE_TYPE = "HGV";
    private static final Logger logger = Logger.getLogger(MotrRequestHandler.class);

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

    @GET
    @Path("motr/v2/search/registration/{registration}")
    @Produces("application/json")
    public AwsProxyResponse getVehicle(@PathParam("registration") String registration) throws Exception {
        // awsRequestId = context.getAwsRequestId();
        logger.info(String.format("Entering MotrRequestHandler, awsRequestId: %s", awsRequestId));

        if (Strings.isNullOrEmpty(registration)) {
            logger.error("Registration param is missing");
            throw new BadRequestException("Registration param is missing", awsRequestId);
        }

        Optional<List<uk.gov.dvsa.mot.vehicle.api.Vehicle>> motVehiclesOptional = getMotVehicle(registration);
        Optional<DvlaVehicle> dvlaVehicleOptional = getDvlaVehicle(registration);

        if ((!motVehiclesOptional.isPresent() || motVehiclesOptional.get().isEmpty()) && dvlaVehicleOptional.isPresent()) {
            DvlaVehicle dvlaVehicle = dvlaVehicleOptional.get();

            Optional<Vehicle> hgvPsvVehicleOptional = getHgvPsvVehicle(dvlaVehicle.getVin());
            if (!hgvPsvVehicleOptional.isPresent()) {
                logger.error("No HGV/PSV vehicle retrieved");
                throw new InvalidResourceException("No HGV/PSV vehicle retrieved", awsRequestId);
            }

            if (!registration.equals(hgvPsvVehicleOptional.get().getVehicleIdentifier())) {
                logger.error(String.format("VRM mismatch between user input and HGV api; user input: %s, registration from HGV api: %s ",
                        registration, hgvPsvVehicleOptional.get().getVehicleIdentifier()));
                throw new Exception("VRM mismatch between user input and HGV api");
            }

            return createResponse(setResponseVehicle(hgvPsvVehicleOptional.get(), dvlaVehicle));
        } else {
            logger.error("Vehicle is not HGV/PSV vehicle");
            throw new InternalServerErrorException("Vehicle is not HGV/PSV vehicle", awsRequestId);
        }
    }

    private Optional<List<uk.gov.dvsa.mot.vehicle.api.Vehicle>> getMotVehicle(String registration) throws Exception {
        logger.info(String.format("Retrieving vehicle by registration: %s", registration));

        List<uk.gov.dvsa.mot.vehicle.api.Vehicle> motVehicles;

        try {
            motVehicles = vehicleReadService.findByRegistration(registration);
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

    private Optional<Vehicle> getHgvPsvVehicle(String vin) throws Exception {
        Vehicle foundVehicle = null;

        try {
            if (!Strings.isNullOrEmpty(vin)) {
                foundVehicle = hgvVehicleProvider.getVehicle(vin);
            } else {
                logger.warn("Passed VIN was empty or null");
            }
        } catch (Exception e) {
            logger.error("There was an error retrieving the HGV/PSV history", e);
            throw new InvalidResourceException("There was an error retrieving the HGV/PSV history", awsRequestId);
        }

        return Optional.ofNullable(foundVehicle);
    }

    private HgvPsvVehicle setResponseVehicle(Vehicle vehicle, DvlaVehicle dvlaVehicle) throws Exception {
        HgvPsvVehicle hgvPsvVehicle = new HgvPsvVehicle();

        hgvPsvVehicle.setMake(vehicle.getMake());
        hgvPsvVehicle.setModel(vehicle.getModel());
        hgvPsvVehicle.setPrimaryColour(dvlaVehicle.getColour1().getName());
        hgvPsvVehicle.setRegistration(dvlaVehicle.getRegistration());
        hgvPsvVehicle.setVin(dvlaVehicle.getVin());
        hgvPsvVehicle.setVehicleType(vehicle.getVehicleType());
        hgvPsvVehicle.setManufactureYear(vehicle.getYearOfManufacture().toString());
        hgvPsvVehicle.setDvlaId(dvlaVehicle.getDvlaVehicleId());
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TestHistory[] testHistory = vehicle.getTestHistory();
        String registrationDate = vehicle.getRegistrationDate();

        if (testHistory.length == 0) {
            if (Strings.isNullOrEmpty(registrationDate)) {
                logger.error("Registration date is null or empty");
                throw new BadRequestException("Registration date is null or empty", awsRequestId);
            }

            Date date = dateFormat.parse(registrationDate);
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(date);

            if (vehicle.getVehicleType().equals(HGV_VEHICLE_TYPE)) {
                calendarDate.add(Calendar.MONTH, 12);
                calendarDate.set(Calendar.DAY_OF_MONTH, calendarDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            } else {
                calendarDate.add(Calendar.MONTH, 12);
            }

            return dateFormat.format(calendarDate.getTime());
        } else {
            return testHistory[testHistory.length - 1].getTestDate();
        }
    }

    private AwsProxyResponse createResponse(HgvPsvVehicle vehicle) throws Exception {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "text/html");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString;

        try {
            jsonString = mapper.writeValueAsString(vehicle);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting hgv/psv vehicle to a JSON string", e);
            throw new InternalServerErrorException("Error while converting hgv/psv vehicle to a JSON string", awsRequestId);
        }

        AwsProxyResponse response = new AwsProxyResponse(200, responseHeaders, jsonString);
        response.setBase64Encoded(false);

        return response;
    }
}
