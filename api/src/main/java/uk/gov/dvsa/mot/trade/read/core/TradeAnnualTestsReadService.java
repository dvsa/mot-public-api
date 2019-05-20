package uk.gov.dvsa.mot.trade.read.core;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.vehicle.hgv.SearchVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TradeAnnualTestsReadService {

    private static final Logger logger = LogManager.getLogger(TradeAnnualTestsReadService.class);

    private SearchVehicleProvider searchVehicleProvider;

    /**
     * Sets the search vehicle provider which will be used to make queries required by this class.
     *
     * @param searchVehicleProvider an instance of something which implements {@link SearchVehicleProvider}
     */
    @Inject
    public void setSearchVehicleProvider(SearchVehicleProvider searchVehicleProvider) {

        this.searchVehicleProvider = searchVehicleProvider;
    }

    /**
     * This method will iterate over a collection of VRMs and query SearchAPI.
     *
     * Vehicles found are added to the result set which is returned after iteration.
     *
     * @param requestedRegistrations collection of VRMs to query SearchAPI
     * @return collection of vehicles found on SearchAPI.
     * @throws Exception
     */
    public List<Vehicle> getAnnualTests(Collection<String> requestedRegistrations) throws Exception {
        logger.trace("Entering getAnnualTests");
        List<Vehicle> vehicleList = new ArrayList<>();

        for (String registration: requestedRegistrations) {
            if (registration.isEmpty()) {
                logger.trace("Empty registration, skipping...");
                continue;
            }
            logger.debug("Querying search api for registration {}", registration);
            Vehicle vehicle = searchVehicleProvider.getVehicle(registration);

            if (vehicle != null) {
                logger.trace("Vehicle with registration {} found. Appending to result...", registration);
                vehicleList.add(vehicle);
            }
        }
        logger.trace("Exiting getAnnualTests");
        return vehicleList;
    }
}
