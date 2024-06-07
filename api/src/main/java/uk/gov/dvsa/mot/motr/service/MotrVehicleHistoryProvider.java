package uk.gov.dvsa.mot.motr.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.motr.model.HgvPsvVehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.model.VehicleWithLatestTest;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.vehicle.hgv.SearchVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.validation.TrailerIdFormat;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

public class MotrVehicleHistoryProvider {

    private static final Logger logger = LogManager.getLogger(MotrVehicleHistoryProvider.class);

    private MotrReadService motrReadService;
    private SearchVehicleProvider searchVehicleProvider;

    @Inject
    public MotrVehicleHistoryProvider(MotrReadService motrReadService, SearchVehicleProvider searchVehicleProvider) {
        this.motrReadService = motrReadService;
        this.searchVehicleProvider = searchVehicleProvider;
    }

    @NotNull public VehicleWithLatestTest searchVehicleByRegistration(@NotNull String registration) throws Exception {
        Optional<VehicleWithLatestTest> optionalMotVehicle = motrReadService.getLatestMotTestByRegistration(registration);

        if (optionalMotVehicle.isPresent()) {
            return getVehicleDetailsFromMostRecentSource(optionalMotVehicle.get());
        }

        Optional<VehicleWithLatestTest> optionalDvlaVehicle = motrReadService.getLatestMotTestForDvlaVehicleByRegistration(registration);

        if (shouldGetHgvPsvHistory(optionalDvlaVehicle.isPresent(), registration)) {
            logger.trace("Fetching HGV/PSV test history for registration: {}", registration);

            Optional<Vehicle> optionalHgvPsvVehicle = Optional.ofNullable(searchVehicleProvider.getVehicle(registration));

            if (optionalHgvPsvVehicle.isPresent()) {
                logger.debug("HGV/PSV history found for registration: {}", registration);
                return new HgvPsvVehicleWithLatestTest(optionalHgvPsvVehicle.get(),
                        optionalDvlaVehicle.map(VehicleWithLatestTest::getDvlaVehicleId).orElse(null));
            } else {
                logger.debug("No HGV/PSV vehicle retrieved");

                if (optionalDvlaVehicle.isPresent() && optionalDvlaVehicle.get().hasTestExpiryDate()) {
                    return optionalDvlaVehicle.get();
                }

                throw new InvalidResourceException("Could not determine test expiry date for registration " + registration);
            }
        } else {
            throw new InvalidResourceException("No MOT Test or DVLA vehicle found for registration " + registration);
        }
    }

    @NotNull public VehicleWithLatestTest searchForCommercialVehicleByRegistration(@NotNull String registration) throws Exception {
        Optional<VehicleWithLatestTest> dvlaVehicleOptional = motrReadService.getLatestMotTestForDvlaVehicleByRegistration(registration);

        if (shouldGetHgvPsvHistory(dvlaVehicleOptional.isPresent(), registration)) {

            Vehicle hgvPsvVehicle = searchVehicleProvider.getVehicle(registration);

            if (hgvPsvVehicle != null) {
                return new HgvPsvVehicleWithLatestTest(hgvPsvVehicle,
                        dvlaVehicleOptional.map(VehicleWithLatestTest::getDvlaVehicleId).orElse(null));
            } else {
                logger.debug("No HGV/PSV vehicle retrieved");
                throw new InvalidResourceException(String.format("No HGV/PSV vehicle found for registration %s", registration));
            }

        } else {
            throw new InvalidResourceException(String.format("No DVLA vehicle found for registration %s", registration));
        }
    }

    @NotNull public VehicleWithLatestTest getDvlaVehicleWithTestByDvlaVehicleId(Integer dvlaVehicleId)
        throws TradeException {
        Optional<VehicleWithLatestTest> vehicle = motrReadService.getLatestMotTestByDvlaVehicleId(dvlaVehicleId);

        return vehicle.orElseThrow(() ->
                new InvalidResourceException(String.format("No MOT Test or DVLA vehicle found for DVLA vehicle id %d", dvlaVehicleId))
        );

    }

    @NotNull public VehicleWithLatestTest getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber)
        throws TradeException {
        Optional<VehicleWithLatestTest> vehicle = motrReadService.getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(motTestNumber);
        return vehicle.orElseThrow(() ->
            new InvalidResourceException(String.format("No MOT Tests found with number: %d", motTestNumber))
        );
    }

    private VehicleWithLatestTest getVehicleDetailsFromMostRecentSource(VehicleWithLatestTest motVehicle) throws Exception {
        logger.trace("Selecting more recent vehicle history");
        if (!hasHgvPsvCompatibileVehicleClass(motVehicle) || motVehicle.getDvlaVehicleId() == null) {
            logger.debug("MOT vehicle without dvla id nor matching HGV/PSV vehicle class");
            return motVehicle;
        }

        Vehicle hgvVehicle = searchVehicleProvider.getVehicle(motVehicle.getRegistration());
        if (hgvVehicle == null) {
            logger.debug("No HGV/PSV history found for registration{}", motVehicle.getRegistration());
            return motVehicle;
        }
        return selectMostRecentVehicleHistory(motVehicle, new HgvPsvVehicleWithLatestTest(hgvVehicle, motVehicle.getDvlaVehicleId()));
    }

    private boolean hasHgvPsvCompatibileVehicleClass(VehicleWithLatestTest motVehicle) {
        String vehicleClass = motVehicle.getMotVehicleClass();
        return "5".equals(vehicleClass) || "7".equals(vehicleClass);
    }

    private VehicleWithLatestTest selectMostRecentVehicleHistory(VehicleWithLatestTest motVehicle, VehicleWithLatestTest hgvVehicle) {
        if (motVehicle.hasTestDate() && hgvVehicle.hasTestDate()) {
            logger.trace("Selecting most recent history based on test dates, MOT: {}, annual: {}",
                    motVehicle.getTestDate(), hgvVehicle.getTestDate());
            if (motVehicle.getTestDate().isAfter(hgvVehicle.getTestDate())) {
                return motVehicle;
            } else {
                return hgvVehicle;
            }
        }

        logger.trace("Selecting most recent history based on test due date, MOT: {}, annual: {}",
                motVehicle.getTestExpiryDate(), hgvVehicle.getTestExpiryDate());
        if (!hgvVehicle.hasTestExpiryDate()
                || motVehicle.getTestExpiryDate().isAfter(hgvVehicle.getTestExpiryDate())) {
            return motVehicle;
        } else {
            return hgvVehicle;
        }
    }

    private boolean shouldGetHgvPsvHistory(boolean hasDvlaVehicle, String vrm) {
        TrailerIdFormat trailerIdFormat = new TrailerIdFormat();

        return hasDvlaVehicle || trailerIdFormat.matches(vrm);
    }
}
