package uk.gov.dvsa.mot.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class AbstractRequestHandler {
    private static final Logger logger = Logger.getLogger(AbstractRequestHandler.class);
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
                        Logger.getRootLogger().setLevel(Level.OFF);
                        break;

                    case "ALL":
                        Logger.getRootLogger().setLevel(Level.ALL);
                        break;

                    case "INFO":
                        Logger.getRootLogger().setLevel(Level.INFO);
                        break;

                    case "TRACE":
                        Logger.getRootLogger().setLevel(Level.TRACE);
                        break;

                    case "DEBUG":
                        Logger.getRootLogger().setLevel(Level.DEBUG);
                        break;

                    case "WARN":
                        Logger.getRootLogger().setLevel(Level.WARN);
                        break;

                    case "ERROR":
                        Logger.getRootLogger().setLevel(Level.ERROR);
                        break;

                    case "FATAL":
                        Logger.getRootLogger().setLevel(Level.FATAL);
                        break;

                    /* log if environment variable found but not in valid list */
                    default:
                        logger.warn("Unknown log level " + logLevel);
                        break;
                }

                logger.info("Changed log level to " + logLevel.toUpperCase());
            }
        } catch (Exception e) {
            logger.error("Unable to override log level", e);
        }

        logger.trace("Exiting overrideLogLevel");
    }
}