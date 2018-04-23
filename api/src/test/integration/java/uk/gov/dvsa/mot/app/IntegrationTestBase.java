package uk.gov.dvsa.mot.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Before;

import uk.gov.dvsa.mot.dataprovider.IntegrationEnvVehicleProvider;
import uk.gov.dvsa.mot.dataprovider.LocalEnvVehicleProvider;
import uk.gov.dvsa.mot.dataprovider.VehicleProvider;

public class IntegrationTestBase {
    protected Injector injector = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {

            //can't bind factory, so this part of code servers purpose of factory
            if (isIntegrationEnvironment()) {
                bind(VehicleProvider.class).to(IntegrationEnvVehicleProvider.class);
            } else {
                bind(VehicleProvider.class).to(LocalEnvVehicleProvider.class);
            }
        }

        private boolean isIntegrationEnvironment() {
            return this.getEnv() != null && this.getEnv().equals("integration");
        }

        private String getEnv() {
            return System.getProperty("ENV", null);
        }
    });

    @Before
    public void setup() {
        injector.injectMembers(this);
    }
}
