package uk.gov.dvsa.mot.vehicle.hgv;

import com.amazonaws.util.StringUtils;

import uk.gov.dvsa.mot.app.ConfigKeys;
import uk.gov.dvsa.mot.app.ConfigManager;

import java.io.IOException;

public class HgvConfiguration {

    private static String apiKey;

    private String apiUrl;
    private int timeoutInSeconds;
    private String proxyHost;
    private String proxyPort;

    public HgvConfiguration() throws IOException {

        // if api key is not already set
        if (StringUtils.isNullOrEmpty(apiKey)) {
            apiKey = ConfigManager.getEnvironmentVariable(ConfigKeys.HgvPsvApiKeyEncrypted);
        }

        this.apiUrl = ConfigManager.getEnvironmentVariable(ConfigKeys.HgvPsvApiUrl);
        this.timeoutInSeconds = Integer.parseInt(ConfigManager.getEnvironmentVariable(ConfigKeys.HgvPsvApiConnectionTimeout));
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