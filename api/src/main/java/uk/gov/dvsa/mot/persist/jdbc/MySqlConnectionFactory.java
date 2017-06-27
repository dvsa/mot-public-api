package uk.gov.dvsa.mot.persist.jdbc;

import com.google.common.base.Strings;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.app.ConfigKeys;
import uk.gov.dvsa.mot.app.ConfigManager;
import uk.gov.dvsa.mot.persist.ConnectionFactory;
import uk.gov.dvsa.mot.trade.api.InternalException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * A factory which creates connections to MySQL based on the configuration
 * provided by {@link ConfigManager}
 */
public class MySqlConnectionFactory implements ConnectionFactory {
    private static final Logger logger = Logger.getLogger(MySqlConnectionFactory.class);

    private static String url;
    private static String username;
    private static String password;
    private static String passwordEncrypted;

    @Override
    public Connection getConnection() {

        try {
            initDbEnvironmentVariables();

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("Unable to connect to database", e);
            throw new InternalException(e);
        }
    }

    private void initDbEnvironmentVariables() throws IOException {

        url = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseConnection);
        username = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseUsername);

        String configPasswordEncrypted = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword, false);

        if (Strings.isNullOrEmpty(configPasswordEncrypted)) {
            password = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabasePassword);

        } else {
            // If not set or changed, then get decrypted password.
            if (Strings.isNullOrEmpty(password) || !configPasswordEncrypted.equalsIgnoreCase(passwordEncrypted)) {
                password = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword);
                passwordEncrypted = configPasswordEncrypted;
            }
        }
    }
}
