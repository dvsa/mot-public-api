package uk.gov.dvsa.mot.motr.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.motr.model.HgvPsvVehicleWithLatestTest;
import uk.gov.dvsa.mot.motr.model.VehicleWithLatestTest;
import uk.gov.dvsa.mot.trade.api.InvalidResourceException;
import uk.gov.dvsa.mot.trade.api.TradeException;
import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.validation.TrailerIdFormat;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

public class MotrVehicleHistoryProvider {

    private static final Logger logger = LogManager.getLogger(MotrVehicleHistoryProvider.class);

    private MotrReadService motrReadService;
    private HgvVehicleProvider hgvVehicleProvider;

    @Inject
    public MotrVehicleHistoryProvider(MotrReadService motrReadService, HgvVehicleProvider hgvVehicleProvider) {
        this.motrReadService = motrReadService;
        this.hgvVehicleProvider = hgvVehicleProvider;
    }

    @NotNull public VehicleWithLatestTest searchVehicleByRegistration(@NotNull String registration) throws Exception {
        Optional<VehicleWithLatestTest> optionalMotVehicle = motrReadService.getLatestMotTestByRegistration(registration);

        if (optionalMotVehicle.isPresent()) {
            return optionalMotVehicle.get();
        }

        Optional<VehicleWithLatestTest> optionalDvlaVehicle = motrReadService.getLatestMotTestForDvlaVehicleByRegistration(registration);

        if (shouldGetHgvPsvHistory(optionalDvlaVehicle.isPresent(), registration)) {

            Optional<Vehicle> optionalHgvPsvVehicle = Optional.ofNullable(hgvVehicleProvider.getVehicle(registration));

            if (optionalHgvPsvVehicle.isPresent()) {
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

            Vehicle hgvPsvVehicle = hgvVehicleProvider.getVehicle(registration);

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

    private boolean shouldGetHgvPsvHistory(boolean hasDvlaVehicle, String vrm) {
        TrailerIdFormat trailerIdFormat = new TrailerIdFormat();

        return hasDvlaVehicle || trailerIdFormat.matches(vrm);
    }
}
