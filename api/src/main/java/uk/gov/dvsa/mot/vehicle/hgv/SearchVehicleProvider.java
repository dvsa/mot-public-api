package uk.gov.dvsa.mot.vehicle.hgv;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;

import uk.gov.dvsa.mot.app.logging.CompletableFutureWrapper;
import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.response.ResponseTestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.response.ResponseVehicle;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

public class SearchVehicleProvider {
    private static final Logger logger = LogManager.getLogger(SearchVehicleProvider.class);
    private static final String VEHICLE_MOTH_ENDPOINT = "/mot-test-history";

    private SearchConfiguration configuration;

    /**
     * Sets the search api configuration.
     *
     * @param configuration an instance of something which implements {@link SearchConfiguration}
     */
    @Inject
    public void setSearchConfiguration(SearchConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * This method calls SearchAPI asynchronously and returns an instance of {@link Vehicle} if the
     * VRM is found; else returns null.
     *
     * One call is made to SearchAPI (asynchronously):
     *  Gets the vehicle properties and test history.
     *
     * @param registration VRM to query SearchAPI
     * @return
     * @throws Exception
     */
    public Vehicle getVehicle(String registration) throws Exception {

        Vehicle vehicle;

        try {
            CompletableFuture<Vehicle> vehicleFuture = CompletableFutureWrapper.supplyAsync(() -> getSearchVehicle(registration));

            vehicle = vehicleFuture.get();
        } catch (Exception e) {
            logger.error("Search API execution error", e);
            throw new Exception("Search API execution error", e);
        }

        return vehicle;
    }

    private Vehicle getSearchVehicle(String registration) {
        ResponseVehicle response = getSearchResponse(VEHICLE_MOTH_ENDPOINT + "/" + registration, ResponseVehicle.class);
        return response != null ? response.getVehicle() : null;
    }

    private <T> T getSearchResponse(String endpointToCall, Class<T> type) {

        try {
            logger.trace("Entering getSearchResponse(), endpoint to call: " + endpointToCall);

            Response response = getClient().target(configuration.getApiUrl() + endpointToCall)
                    .request()
                    .header("x-api-key", configuration.getApiKey())
                    .get();

            int status = response.getStatus();
            logger.debug("Search API response HTTP status: " + status);

            switch (status) {
                case 200:
                    return response.readEntity(type);
                case 204:
                    response.close();
                    return type.newInstance();
                default:
                    String responseBody = response.readEntity(String.class);
                    logger.error("Got invalid response code - response body: " + responseBody);
                    throw new IOException("Invalid http response code");
            }
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            logger.error(String.format("Error during communication with Search API from endpoint : %s", endpointToCall), e);
            throw new RuntimeException("Error during communication with Search API", e);
        }
    }

    private Client getClient() throws IOException {
        ClientConfig config = new ClientConfig();
        config.connectorProvider(new ApacheConnectorProvider());

        String proxyHost = configuration.getProxyHost();
        if (proxyHost != null) {
            config.property(ClientProperties.PROXY_URI, "http://" + proxyHost + ":" + configuration.getProxyPort());
        }

        int timeout = configuration.getTimeoutInSeconds() * 1000;
        config.property(ClientProperties.CONNECT_TIMEOUT, timeout);
        config.property(ClientProperties.READ_TIMEOUT, timeout);

        return JerseyClientBuilder.newClient(config);
    }
}
