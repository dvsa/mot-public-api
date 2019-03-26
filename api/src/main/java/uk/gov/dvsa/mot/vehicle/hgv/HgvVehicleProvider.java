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
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

public class HgvVehicleProvider {
    private static final Logger logger = LogManager.getLogger(HgvVehicleProvider.class);

    private HgvConfiguration configuration;

    @Inject
    public void setHgvConfiguration(HgvConfiguration configuration) {
        this.configuration = configuration;
    }

    public Vehicle getVehicle(String registration) throws Exception {

        Vehicle vehicle;
        TestHistory[] vehicleTestHistory;

        try {
            CompletableFuture<Vehicle> vehicleFuture = CompletableFutureWrapper.supplyAsync(() -> getHgvVehicle(registration));

            CompletableFuture<TestHistory[]> vehicleTestHistoryFuture = CompletableFutureWrapper.supplyAsync(
                    () -> getHgvVehicleTestHistory(registration));

            vehicle = vehicleFuture.get();
            vehicleTestHistory = vehicleTestHistoryFuture.get();
        } catch (Exception e) {
            logger.error("HGV calls execution error", e);
            throw new Exception("HGV calls execution error", e);
        }

        if (vehicle != null) {
            vehicle.setTestHistory(vehicleTestHistory);
        }

        return vehicle;
    }

    private Vehicle getHgvVehicle(String registration) {
        ResponseVehicle response = getHgvResponse(registration,  "/vehicle/moth", ResponseVehicle.class);
        return response != null ? response.getVehicle() : null;
    }

    private TestHistory[] getHgvVehicleTestHistory(String registration) {
        ResponseTestHistory response = getHgvResponse(registration, "/testhistory/moth", ResponseTestHistory.class);

        return response != null ? response.getTestHistory() : null;
    }

    private <T> T getHgvResponse(String registration, String endpointToCall, Class<T> type) {

        try {
            logger.trace("Entering getHgvResponse(), endpoint to call: " + endpointToCall);

            Response response = getClient().target(configuration.getApiUrl() + endpointToCall)
                    .queryParam("identifier", registration)
                    .request()
                    .header("x-api-key", configuration.getApiKey())
                    .get();

            int status = response.getStatus();
            logger.debug("HGV response status code: " + status);

            switch (status) {
                case 200:
                    return response.readEntity(type);
                case 204:
                    response.close();
                    return type.newInstance();
                default:
                    response.close();
                    throw new IOException("Invalid http response code");
            }
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            logger.error(String.format("Error during communication with api from HGV endpoint : %s", endpointToCall), e);
            throw new RuntimeException("Error during communication with HGV/PSV API", e);
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
