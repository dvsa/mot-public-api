package uk.gov.dvsa.mot.persist.jdbc;

import uk.gov.dvsa.mot.persist.ConnectionFactory;

import java.sql.Connection;

/**
 * Base class for JDBC DAOs which need to create new connections during the DAO's lifecycle.
 */
public class AbstractDaoJdbc {
    private final ConnectionFactory connectionFactory;

    protected AbstractDaoJdbc(ConnectionFactory connectionFactory) {

        this.connectionFactory = connectionFactory;
    }

    /**
     * Get a connection from the connection factory and return it.
     *
     * The caller is responsible for ensuring that the connection is closed after use.
     *
     * @return A connection to the database, which must be closed when no longer required.
     */
    protected Connection getConnection() {

        return connectionFactory.getConnection();
    }
}
