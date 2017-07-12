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

    private static final DatabasePasswordHolder DB_PASSWORD_HOLDER = new DatabasePasswordHolder();

    @Override
    public Connection getConnection() {

        try {
            return DriverManager.getConnection(getDbUrl(), getDbUsername(), getDbPassword());
        } catch (Exception e) {
            logger.error("Unable to connect to database", e);
            throw new InternalException(e);
        }
    }

    private String getDbUrl() throws IOException {

        return ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseConnection);
    }

    private String getDbUsername() throws IOException {

        return ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseUsername);
    }

    private String getDbPassword() throws IOException {

        return DB_PASSWORD_HOLDER.getDbPassword();
    }

    private static class DatabasePasswordHolder {

        private String passwordDecrypted;
        private String passwordEncrypted;

        public String getDbPassword() throws IOException {

            String password = null;
            String configPasswordEncrypted = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword, false);

            if (Strings.isNullOrEmpty(configPasswordEncrypted)) {
                password = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabasePassword);

            } else {
                if (Strings.isNullOrEmpty(passwordDecrypted) || !configPasswordEncrypted.equalsIgnoreCase(passwordEncrypted)) {

                    password = ConfigManager.getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword);

                    passwordEncrypted = configPasswordEncrypted;
                    passwordDecrypted = password;
                }
            }
            return password;
        }
    }
}
