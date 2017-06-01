package uk.gov.dvsa.mot.persist.jdbc;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.app.ConfigKeys;
import uk.gov.dvsa.mot.app.ConfigManager;
import uk.gov.dvsa.mot.persist.ConnectionFactory;
import uk.gov.dvsa.mot.trade.api.InternalException;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * A factory which creates connections to MySQL based on the configuration
 * provided by {@link ConfigManager}
 */
public class MySqlConnectionFactory implements ConnectionFactory {
    private static final Logger logger = Logger.getLogger(MySqlConnectionFactory.class);

    @Override
    public Connection getConnection() {

        try {
            String url = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseConnection);
            String username = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseUsername);

            String password = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword);

            if (password == null) {
                password = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabasePassword);
            }

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("Unable to connect to database", e);
            throw new InternalException(e);
        }
    }

}
