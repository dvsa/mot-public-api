package uk.gov.dvsa.mot.persist.jdbc ;

import java.sql.Connection ;

import org.apache.log4j.Logger ;

import com.google.inject.Inject ;

import uk.gov.dvsa.mot.persist.ConnectionFactory ;
import uk.gov.dvsa.mot.persist.Database ;
import uk.gov.dvsa.mot.persist.MotTestReadDao ;
import uk.gov.dvsa.mot.persist.TradeReadDao ;
import uk.gov.dvsa.mot.persist.VehicleReadDao ;
import uk.gov.dvsa.mot.trade.api.InternalException ;

/**
 * An implementation of {@link Database} for MySQL JDBC connections.
 */
public class DatabaseMySQLJDBC implements Database
{
  private static final Logger logger = Logger.getLogger( DatabaseMySQLJDBC.class ) ;
  private Connection connection ;
  private final ConnectionFactory connectionFactory ;

  @Inject
  public DatabaseMySQLJDBC( ConnectionFactory connectionFactory )
  {
    this.connectionFactory = connectionFactory ;
  }

  @Override
  public boolean open()
  {
    this.connection = connectionFactory.getConnection() ;
    return true ;
  }

  /**
   * Close the connection.
   */
  @Override
  public void close()
  {
    try
    {
      connection.close() ;
    }
    catch ( Exception e )
    {
      logger.error( "Unable to disconnect from database", e ) ;
      throw new InternalException( e ) ;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.gov.dvsa.mot.persist.jdbc.Database#getVehicleReadDao()
   */
  @Override
  public VehicleReadDao getVehicleReadDao()
  {
    verifyConnection() ;
    return new VehicleReadDaoMySQLJDBC( connection ) ;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.gov.dvsa.mot.persist.jdbc.Database#getMotTestReadDao()
   */
  @Override
  public MotTestReadDao getMotTestReadDao()
  {
    verifyConnection() ;
    return new MotTestReadDaoMySQLJDBC( connection ) ;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.gov.dvsa.mot.persist.jdbc.Database#getTradeReadDao()
   */
  @Override
  public TradeReadDao getTradeReadDao()
  {
    verifyConnection() ;
    return new TradeReadDaoMySQLJDBC( new MySQLConnectionFactory() ) ;
  }

  /**
   * Test if the connection has been opened before. If it has not, open it,
   * throwing an error if it can't open.
   * 
   * If we have a connection already, do nothing.
   */
  private void verifyConnection()
  {
    if ( ( connection == null ) && ( !open() ) )
    {
      logger.error( "Not connected to database" ) ;
      throw new InternalException( "Not connected" ) ;
    }
  }
}
