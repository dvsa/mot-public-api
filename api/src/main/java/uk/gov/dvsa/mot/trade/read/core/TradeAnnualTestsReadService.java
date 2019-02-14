package uk.gov.dvsa.mot.trade.read.core;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.vehicle.hgv.HgvVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TradeAnnualTestsReadService {

    private static final Logger logger = LogManager.getLogger(TradeAnnualTestsReadService.class);

    private HgvVehicleProvider hgvVehicleProvider;

    @Inject
    public void setHgvVehicleProvider(HgvVehicleProvider hgvVehicleProvider) {

        this.hgvVehicleProvider = hgvVehicleProvider;
    }

    public List<Vehicle> getAnnualTests(Collection<String> requestedRegistrations) throws Exception {
        logger.trace("Entering getAnnualTests");
        List<Vehicle> vehicleList = new ArrayList<>();

        for (String registration: requestedRegistrations) {
            if (registration.isEmpty()) {
                logger.trace("Empty registration, skipping...");
                continue;
            }
            logger.debug("Querying search api for registration {}", registration);
            Vehicle vehicle = hgvVehicleProvider.getVehicle(registration);

            if (vehicle != null) {
                logger.trace("Vehicle with registration {} found. Appending to result...", registration);
                vehicleList.add(vehicle);
            }
        }
        logger.trace("Exiting getAnnualTests");
        return vehicleList;
    }
}
