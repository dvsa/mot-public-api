package uk.gov.dvsa.mot.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import uk.gov.dvsa.mot.motr.service.MotrReadService;
import uk.gov.dvsa.mot.motr.service.MotrReadServiceDatabase;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadServiceDatabase;
import uk.gov.dvsa.mot.persist.ConnectionManager;
import uk.gov.dvsa.mot.persist.DbConnectionInterceptor;
import uk.gov.dvsa.mot.persist.MotTestReadDao;
import uk.gov.dvsa.mot.persist.ProvideDbConnection;
import uk.gov.dvsa.mot.persist.TradeReadDao;
import uk.gov.dvsa.mot.persist.VehicleReadDao;
import uk.gov.dvsa.mot.persist.jdbc.MotTestReadDaoJdbc;
import uk.gov.dvsa.mot.persist.jdbc.TradeReadDaoJdbc;
import uk.gov.dvsa.mot.persist.jdbc.VehicleReadDaoJdbc;
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

        bind(MotTestReadService.class).to(MotTestReadServiceDatabase.class);
        bind(VehicleReadService.class).to(VehicleReadServiceDatabase.class);
        bind(TradeReadService.class).to(TradeReadServiceDatabase.class);
        bind(MotrReadService.class).to(MotrReadServiceDatabase.class);
        bind(TradeReadDao.class).to(TradeReadDaoJdbc.class);
        bind(MotTestReadDao.class).to(MotTestReadDaoJdbc.class);
        bind(VehicleReadDao.class).to(VehicleReadDaoJdbc.class);
        bind(ConnectionManager.class).in(Singleton.class);

        DbConnectionInterceptor dbConnectionInterceptor = new DbConnectionInterceptor();
        requestInjection(dbConnectionInterceptor);

        bindInterceptor(Matchers.any(), Matchers.annotatedWith(ProvideDbConnection.class),
                dbConnectionInterceptor);
    }
}
