package uk.gov.dvsa.mot.vehicle.hgv;

import com.amazonaws.util.StringUtils;

import uk.gov.dvsa.mot.app.ConfigKeys;
import uk.gov.dvsa.mot.app.ConfigManager;

import java.io.IOException;

public class SearchConfiguration {

    private static String apiKey;

    private String apiUrl;
    private int timeoutInSeconds;
    private String proxyHost;
    private String proxyPort;

    public SearchConfiguration() throws IOException {

        if (StringUtils.isNullOrEmpty(apiKey)) {
            apiKey = ConfigManager.getEnvironmentVariable(ConfigKeys.MothApiKey);
        }

        if (StringUtils.isNullOrEmpty(apiKey)) {
            apiKey = ConfigManager.getEnvironmentVariable(ConfigKeys.MothApiKeyEncrypted);
        }

        this.apiUrl = ConfigManager.getEnvironmentVariable(ConfigKeys.MothApiUrl);
        this.timeoutInSeconds = Integer.parseInt(ConfigManager.getEnvironmentVariable(ConfigKeys.MothApiConnectionTimeout));
        this.proxyHost = ConfigManager.getEnvironmentVariable(ConfigKeys.ProxyHost);
        this.proxyPort = ConfigManager.getEnvironmentVariable(ConfigKeys.ProxyPort);
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public void setTimeoutInSeconds(int timeout) {
        this.timeoutInSeconds = timeout;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }
}