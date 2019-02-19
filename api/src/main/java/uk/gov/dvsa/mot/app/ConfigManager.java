package uk.gov.dvsa.mot.app;

import com.google.common.base.Strings;

import uk.gov.dvsa.mot.security.Decrypt;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Handles configuration loading, abstracting automatically over environment
 * variables (inside Lambda) and config.properties (outside Lambda).
 *
 * In a Lambda environment, also handles decryption of encrypted environment
 * variables by using KMS.
 */
public class ConfigManager {

    // Stores secrets in memory for the lifetime of the Lambda. Prevents repeated calls to KMS:Decrypt.
    private static HashMap<String, String> cachedSecrets = new HashMap<>();

    private ConfigManager() {
    }

    /**
     * Retrieve the value of the environment variable with the given name.
     *
     * If the name contains the string "ENCRYPTED", it will pass the value through
     * AWS KMS to get the plaintext version.
     *
     * If the code is not running inside a Lambda container, retrieve the same key
     * from config.properties instead.
     *
     * @param name The name of the environment variable to get the value of.
     * @return The value of the environment variable, decrypted through KMS if the environment variable name has "ENCRYPTED" in it.
     * @throws IOException if a connection to KMS was attempted and failed
     */
    public static String getEnvironmentVariable(String name) throws IOException {

        return getEnvironmentVariable(name, true);
    }

    /**
     * Retrieve the value of the environment variable with the given name.
     *
     * If the code is not running inside a Lambda container, retrieve the same key
     * from config.properties instead.
     *
     * @param name The name of the environment variable to get the value of.
     * @return The value of the environment variable.
     * @throws IOException if an error occured getting local config settings
     */
    public static String getEnvironmentVariable(String name, boolean decrypt) throws IOException {

        if (cachedSecrets.containsKey(name)) {
            return cachedSecrets.get(name);
        }

        if (isRunningInLambda()) {
            String value = System.getenv(name);

            if (Strings.isNullOrEmpty(value)) {
                return value;
            }

            if (isValueDecryptionRequired(name, decrypt)) {
                String decryptedValue = new Decrypt().decrypt(value);
                cachedSecrets.put(name, decryptedValue);
                return decryptedValue;
            } else {
                return value;
            }
        } else {
            return getConfigSettingsFromCommand(name);
        }
    }

    private static String getConfigSettingsFromCommand(String property) {

        return System.getProperty(property, null);
    }

    /**
     * Get a setting from config.properties.
     *
     * @param name The name of the setting to load.
     * @return The value of that setting.
     * @throws IOException If there is an error with the resource stream.
     */
    private static String getLocalConfigSetting(String name) throws IOException {

        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties");

            // load a properties file
            prop.load(input);

            return prop.getProperty(name);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    /**
     * Checks if the code is running inside an AWS Lambda environment.
     *
     * @return true if inside a lambda, false otherwise.
     */
    private static boolean isRunningInLambda() {

        return System.getenv("AWS_LAMBDA_FUNCTION_NAME") != null;
    }

    private static boolean isValueDecryptionRequired(String name, boolean decrypt) {

        if (decrypt) {
            return name.contains("ENCRYPTED");
        }

        return false;
    }
}
