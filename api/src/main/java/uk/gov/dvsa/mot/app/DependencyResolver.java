package uk.gov.dvsa.mot.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadServiceDatabase;
import uk.gov.dvsa.mot.persist.ConnectionFactory;
import uk.gov.dvsa.mot.persist.Database;
import uk.gov.dvsa.mot.persist.TradeReadDao;
import uk.gov.dvsa.mot.persist.jdbc.DatabaseJdbc;
import uk.gov.dvsa.mot.persist.jdbc.MySqlConnectionFactory;
import uk.gov.dvsa.mot.persist.jdbc.TradeReadDaoJdbc;
import uk.gov.dvsa.mot.trade.read.core.TradeReadService;
import uk.gov.dvsa.mot.trade.read.core.TradeReadServiceDatabase;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadServiceDatabase;

/**
 * Configure implementations for dependency injection.
 */
public class DependencyResolver extends AbstractModule {

    @Override
    protected void configure() {

        bind(Database.class).to(DatabaseJdbc.class).in(Singleton.class);
        bind(MotTestReadService.class).to(MotTestReadServiceDatabase.class);
        bind(VehicleReadService.class).to(VehicleReadServiceDatabase.class);
        bind(TradeReadService.class).to(TradeReadServiceDatabase.class);
        bind(TradeReadDao.class).to(TradeReadDaoJdbc.class);
        bind(ConnectionFactory.class).to(MySqlConnectionFactory.class);
    }
}
