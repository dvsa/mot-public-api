package uk.gov.dvsa.mot.app;

/**
 * Configuration keys for various settings.
 */
public final class ConfigKeys {
    public static final String DatabaseConnection = "DATABASE_CONNECTION";
    public static final String DatabaseUsername = "DATABASE_USERNAME";
    public static final String DatabasePassword = "DATABASE_PASSWORD";
    public static final String DatabaseEncryptedPassword = "DATABASE_ENCRYPTED_PASSWORD";
    public static final String ProxyHost = "PROXY_HOST";
    public static final String ProxyPort = "PROXY_PORT";
    public static final String LogLevel = "LOG_LEVEL";

    public static final String MothApiUrl = "MOTH_API_URL";
    public static final String MothApiKey = "MOTH_API_KEY";
    public static final String MothApiKeyEncrypted = "MOTH_API_KEY_ENCRYPTED";
    public static final String MothApiConnectionTimeout = "MOTH_API_CONNECTION_TIMEOUT";

    public static final String ObfuscationSecret = "OBFUSCATION_SECRET";
    public static final String ObfuscationEncryptedSecret = "OBFUSCATION_ENCRYPTED_SECRET";

    public static final String AnnualTestsMaxQueryableVehicleIdentifiers = "ANNUAL_TESTS_MAX_QUERYABLE_REGISTRATIONS";
    public static final String AppEnv = "APP_ENV";
}
