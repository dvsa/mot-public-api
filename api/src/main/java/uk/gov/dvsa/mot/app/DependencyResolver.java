package uk.gov.dvsa.mot.app ;

import com.google.inject.AbstractModule ;
import com.google.inject.Singleton ;

import uk.gov.dvsa.mot.mottest.read.core.* ;
import uk.gov.dvsa.mot.persist.ConnectionFactory ;
import uk.gov.dvsa.mot.persist.Database ;
import uk.gov.dvsa.mot.persist.TradeReadDao ;
import uk.gov.dvsa.mot.persist.jdbc.* ;
import uk.gov.dvsa.mot.trade.read.core.* ;
import uk.gov.dvsa.mot.vehicle.read.core.* ;

/**
 * Configure implementations for dependency injection.
 */
public class DependencyResolver extends AbstractModule
{
  @Override
  protected void configure()
  {
    bind( Database.class ).to( DatabaseMySQLJDBC.class ).in( Singleton.class ) ;
    bind( MotTestReadService.class ).to( MotTestReadServiceDatabase.class ) ;
    bind( VehicleReadService.class ).to( VehicleReadServiceDatabase.class ) ;
    bind( TradeReadService.class ).to( TradeReadServiceDatabase.class ) ;
    bind( TradeReadDao.class ).to( TradeReadDaoMySQLJDBC.class ) ;
    bind( ConnectionFactory.class ).to( MySQLConnectionFactory.class ) ;
  }
}
