package uk.gov.dvsa.mot.trade.api.response.mapper;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VehicleResponseMapperFactory {
    private static final Logger logger = LogManager.getLogger(VehicleResponseMapperFactory.class);

    public VehicleResponseMapper getMapper(String version) {

        logger.trace("Entering VehicleResponseMapperFactory.getMapper");
        VehicleResponseMapper mapper;

        if (version == null) {
            logger.debug("API version requested is null, using v1");
            mapper = new VehicleV1ResponseMapper();
        } else {
            switch (version) {
                case "v1":
                    mapper = new VehicleV1ResponseMapper();
                    break;
                case "v2":
                    mapper = new VehicleV2ResponseMapper();
                    break;
                case "v3":
                    mapper = new VehicleV3ResponseMapper();
                    break;
                case "v4":
                    mapper = new VehicleV4ResponseMapper();
                    break;
                case "v5":
                    mapper = new VehicleV5ResponseMapper();
                    break;
                case "v6":
                    mapper = new VehicleV6ResponseMapper();
                    break;
                default:
                    logger.warn("Unknown API version selected. Using v1");
                    mapper = new VehicleV1ResponseMapper();
            }
        }

        logger.debug("Mapper version selected: " + mapper.getClass().getSimpleName());
        logger.trace("Exiting VehicleResponseMapperFactory.getMapper");
        return mapper;
    }
}
