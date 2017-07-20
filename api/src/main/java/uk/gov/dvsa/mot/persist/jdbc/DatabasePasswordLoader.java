package uk.gov.dvsa.mot.persist.jdbc;

import com.google.common.base.Strings;

import uk.gov.dvsa.mot.app.ConfigKeys;
import uk.gov.dvsa.mot.app.ConfigManager;

import java.io.IOException;

public class DatabasePasswordLoader {

    private String passwordDecrypted;
    private String passwordEncrypted;

    public String getDbPassword() throws IOException {

        String password = getEnvironmentVariable(ConfigKeys.DatabaseEncryptedPassword, true);

        if (password == null || password.length() == 0) {
            password = getEnvironmentVariable(ConfigKeys.DatabasePassword);
        }

        return password;
    }

    String getEnvironmentVariable(String name) throws IOException {

        return ConfigManager.getEnvironmentVariable(name);
    }

    String getEnvironmentVariable(String name, boolean decrypt) throws IOException {

        return ConfigManager.getEnvironmentVariable(name, decrypt);
    }
}
