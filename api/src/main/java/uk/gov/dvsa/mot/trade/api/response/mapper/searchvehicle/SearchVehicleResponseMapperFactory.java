package uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchVehicleResponseMapperFactory {
    private static final Logger logger = LogManager.getLogger(SearchVehicleResponseMapperFactory.class);

    public SearchVehicleResponseMapper getMapper(String version) {

        logger.trace("Entering SearchVehicleResponseMapperFactory.getMapper");
        SearchVehicleResponseMapper mapper;

        if (version == null) {
            logger.debug("API version requested is null, using v6");
            mapper = new SearchVehicleV6ResponseMapper();
        } else {
            switch (version) {
                case "v6":
                    mapper = new SearchVehicleV6ResponseMapper();
                    break;
                case "v7":
                    mapper = new SearchVehicleV7ResponseMapper();
                    break;
                default:
                    logger.warn("Unknown API version selected. Using v6");
                    mapper = new SearchVehicleV6ResponseMapper();
            }
        }

        logger.debug("Mapper version selected: " + mapper.getClass().getSimpleName());
        logger.trace("Exiting SearchVehicleResponseMapperFactory.getMapper");
        return mapper;
    }
}
