package uk.gov.dvsa.mot.vehicle.hgv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;

import uk.gov.dvsa.mot.app.logging.CompletableFutureWrapper;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothVehicle;
import uk.gov.dvsa.mot.vehicle.hgv.model.moth.MothVehicleMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

        MothVehicle mothVehicle;

        try {
            CompletableFuture<MothVehicle> vehicleFuture = CompletableFutureWrapper.supplyAsync(() -> getSearchVehicle(registration));

            mothVehicle = vehicleFuture.get();
        } catch (Exception e) {
            logger.error("Search API execution error", e);
            throw new Exception("Search API execution error", e);
        }

        List<String> filterDataOrigin = Arrays.asList("CVS", "NI");

        mothVehicle = Optional.ofNullable(mothVehicle)
                .filter(vehicle -> vehicle.getVehicleDataOrigin().stream().anyMatch(filterDataOrigin::contains))
                .filter(vehicle -> Arrays.stream(new String[]{"HGV", "PSV", "Trailer"}).anyMatch(vehicle.getVehicleType()::contains))
                .orElse(null);

        return mothVehicle != null ? MothVehicleMapper.mapFromMothVehicle(mothVehicle) : null;
    }

    private MothVehicle getSearchVehicle(String registration) {
        return getSearchResponse(registration);
    }

    private MothVehicle getSearchResponse(String registration) {

        try {
            logger.trace("Entering getSearchResponse(), endpoint to call: " + SearchVehicleProvider.VEHICLE_MOTH_ENDPOINT);

            Response response = getClient().target(configuration.getApiUrl() + SearchVehicleProvider.VEHICLE_MOTH_ENDPOINT)
                    .queryParam("identifier", registration)
                    .request()
                    .header("x-api-key", configuration.getApiKey())
                    .get();

            int status = response.getStatus();
            logger.debug("Search API response HTTP status: " + status);

            if (status == 200) {
                String jsonString = response.readEntity(String.class);
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(jsonString, MothVehicle.class);
            }

            if (status == 404) {
                return null;
            }

            String responseBody = response.readEntity(String.class);
            logger.error("Got invalid response code - response body: " + responseBody);
            throw new IOException("Invalid http response code");

        } catch (IOException e) {
            logger.error(String.format("Error during communication with Search API from endpoint : %s",
                    SearchVehicleProvider.VEHICLE_MOTH_ENDPOINT), e);
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
