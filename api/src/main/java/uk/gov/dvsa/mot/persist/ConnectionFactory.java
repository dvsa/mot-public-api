package uk.gov.dvsa.mot.persist;

import java.sql.Connection;

/**
 * A ConnectionFactory is an object which can make database connections with no
 * further configuration.
 */
public interface ConnectionFactory {
    /**
     * Create and return a new database connection. The caller will be expected to
     * close it when they are done.
     *
     * @return A database connection ready to use. The caller should close this connection when they no longer need it.
     */
    Connection getConnection();
}