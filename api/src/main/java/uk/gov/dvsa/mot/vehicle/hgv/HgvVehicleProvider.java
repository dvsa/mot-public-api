package uk.gov.dvsa.mot.vehicle.hgv;

import com.google.inject.Inject;

import org.apache.log4j.Logger;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;

import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;
import uk.gov.dvsa.mot.vehicle.hgv.response.ResponseTestHistory;
import uk.gov.dvsa.mot.vehicle.hgv.response.ResponseVehicle;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

public class HgvVehicleProvider {
    private static final Logger logger = Logger.getLogger(HgvVehicleProvider.class);

    private HgvConfiguration configuration;

    @Inject
    public void setHgvConfiguration(HgvConfiguration configuration) {
        this.configuration = configuration;
    }

    public Vehicle getVehicle(String vin) throws Exception {
        logger.trace("Entering HgvVehicleProvider getVehicle()");

        if (vin == null || vin.isEmpty()) {
            throw new IllegalArgumentException("VIN number is null or empty");
        }

        Vehicle vehicle;
        TestHistory[] vehicleTestHistory;

        try {
            CompletableFuture<Vehicle> vehicleFuture = CompletableFuture.supplyAsync(() -> {
                Vehicle result = null;

                try {
                    result = getHgvVehicle(vin);
                } catch (IOException e) {
                    logger.error("IO error during communication with HGV vehicle api",  e);
                } catch (InstantiationException e) {
                    logger.error("Instantiation error during communication with HGV api", e);
                } catch (IllegalAccessException e) {
                    logger.error("IllegalAccess error during communication with HGV api", e);
                }

                return result;
            });

            CompletableFuture<TestHistory[]> vehicleTestHistoryFuture = CompletableFuture.supplyAsync(() -> {
                TestHistory[] result = null;

                try {
                    result = getHgvVehicleTestHistory(vin);
                } catch (IOException e) {
                    logger.error("IO error during communication with HGV test history api", e);
                } catch (InstantiationException e) {
                    logger.error("Instantiation error during communication with HGV test history api", e);
                } catch (IllegalAccessException e) {
                    logger.error("IllegalAccess error during communication with HGV test history api", e);
                }

                return result;
            });

            vehicle = vehicleFuture.get();
            vehicleTestHistory = vehicleTestHistoryFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("HGV calls execution error", e);
            throw new Exception("HGV calls execution error" + e.getStackTrace());
        }

        if (vehicle != null) {
            vehicle.setTestHistory(vehicleTestHistory);
            logger.info("HGV vehicle fetched successfully. Registration " + vehicle.getVehicleIdentifier());
        }

        return vehicle;
    }

    private Vehicle getHgvVehicle(String vin) throws IOException, InstantiationException, IllegalAccessException {
        ResponseVehicle response = getHgvResponse(vin,  "/vehicle/moth", ResponseVehicle.class);

        return response.getVehicle();
    }

    private TestHistory[] getHgvVehicleTestHistory(String vin) throws IOException, InstantiationException, IllegalAccessException {
        ResponseTestHistory response = getHgvResponse(vin, "/testhistory/moth", ResponseTestHistory.class);

        return response.getTestHistory();
    }

    private <T> T getHgvResponse(String vin, String endpointToCall, Class<T> type) throws IOException, IllegalAccessException,
            InstantiationException {
        logger.trace("Entering getHgvResponse(), endpoint to call: " + endpointToCall);

        Response response = getClient().target(configuration.getApiUrl() + endpointToCall)
                .queryParam("identifier", vin)
                .request()
                .header("x-api-key", configuration.getApiKey())
                .get();

        int status = response.getStatus();
        logger.info("HGV response status code: " + status);

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
