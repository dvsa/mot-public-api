package uk.gov.dvsa.mot.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public abstract class AbstractRequestHandler {
    private static final Logger logger = LogManager.getLogger(AbstractRequestHandler.class);
    private final Injector injector;

    public AbstractRequestHandler() {

        this(true);
    }

    /**
     * Construct an AbstractRequestHandler, specifying whether it should inject
     * dependencies into itself.
     *
     * @param inject If false, will not initiate dependency injection.
     */
    AbstractRequestHandler(boolean inject) {

        this(inject ? new DependencyResolver() : null, inject);
    }

    private AbstractRequestHandler(AbstractModule dependencyResolver, boolean injectSelf) {

        logger.trace("Entered Abstract Request Handler");
        overrideLogLevel();

        if (injectSelf) {
            this.injector = Guice.createInjector(dependencyResolver);
            this.injector.injectMembers(this);
        } else {
            this.injector = null;
        }
        logger.trace("Exiting Abstract Request Handler");
    }

    /**
     * Set the logging level from the configuration.
     */
    private void overrideLogLevel() {

        logger.trace("Entering overrideLogLevel");

        try {
            String logLevel = ConfigManager.getEnvironmentVariable(ConfigKeys.LogLevel);

            if (logLevel != null) {
                logger.info("Changing log level to " + logLevel.toUpperCase());

                switch (logLevel.toUpperCase()) {
                    case "OFF":
                        Configurator.setLevel(LogManager.getRootLogger().getName(), Level.OFF);
                        break;

                    case "ALL":
                        Configurator.setLevel(LogManager.getRootLogger().getName(), Level.ALL);
                        break;

                    case "INFO":
                        Configurator.setLevel(LogManager.getRootLogger().getName(), Level.INFO);
                        break;

                    case "TRACE":
                        Configurator.setLevel(LogManager.getRootLogger().getName(), Level.TRACE);
                        break;

                    case "DEBUG":
                        Configurator.setLevel(LogManager.getRootLogger().getName(), Level.DEBUG);
                        break;

                    case "WARN":
                        Configurator.setLevel(LogManager.getRootLogger().getName(), Level.WARN);
                        break;

                    case "ERROR":
                        Configurator.setLevel(LogManager.getRootLogger().getName(), Level.ERROR);
                        break;

                    case "FATAL":
                        Configurator.setLevel(LogManager.getRootLogger().getName(), Level.FATAL);
                        break;

                    /* log if environment variable found but not in valid list */
                    default:
                        logger.warn("Unknown log level " + logLevel);
                        break;
                }

                logger.info("Log level changed to " + logLevel.toUpperCase());
            }
        } catch (Exception e) {
            logger.error("Unable to override log level", e);
        }

        logger.trace("Exiting overrideLogLevel");
    }
}