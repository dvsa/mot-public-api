package uk.gov.dvsa.mot.persist.jdbc ;

import java.sql.Connection ;

import uk.gov.dvsa.mot.persist.ConnectionFactory ;

/**
 * Base class for JDBC DAOs which need to create new connections during the DAO's lifecycle.
 */
public class AbstractDaoJDBC
{
  private final ConnectionFactory connectionFactory ;

  protected AbstractDaoJDBC( ConnectionFactory connectionFactory )
  {
    this.connectionFactory = connectionFactory ;
  }

  /**
   * Get a connection from the connection factory and return it.
   * 
   * The caller is responsible for ensuring that the connection is closed after use.
   * 
   * @return A connection to the database, which must be closed when no longer required.
   */
  protected Connection getConnection()
  {
    return connectionFactory.getConnection();
  }
}
