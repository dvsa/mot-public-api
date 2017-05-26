package uk.gov.dvsa.mot.persist ;

/**
 * An aggregated provider of all the database read objects.
 */
public interface Database extends java.lang.AutoCloseable
{
  boolean open() ;

  VehicleReadDao getVehicleReadDao() ;
  MotTestReadDao getMotTestReadDao() ;
  TradeReadDao getTradeReadDao() ;
}