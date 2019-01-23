package uk.gov.dvsa.mot.trade.api.response.mapper.cvsvehicle;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CvsVehicleResponseMapperFactory {
    private static final Logger logger = LogManager.getLogger(CvsVehicleResponseMapperFactory.class);

    public CvsVehicleResponseMapper getMapper(String version) {

        logger.trace("Entering CvsVehicleResponseMapperFactory.getMapper");
        CvsVehicleResponseMapper mapper;

        if (version == null) {
            logger.debug("API version requested is null, using v6");
            mapper = new CvsVehicleV6ResponseMapper();
        } else {
            switch (version) {
                case "v6":
                    mapper = new CvsVehicleV6ResponseMapper();
                    break;
                default:
                    logger.warn("Unknown API version selected. Using v6");
                    mapper = new CvsVehicleV6ResponseMapper();
            }
        }

        logger.debug("Mapper version selected: " + mapper.getClass().getSimpleName());
        logger.trace("Exiting CvsVehicleResponseMapperFactory.getMapper");
        return mapper;
    }
}
