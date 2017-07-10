package uk.gov.dvsa.mot.mottest.read.core;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.persist.jdbc.MySqlConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final Logger logger = Logger.getLogger(ConnectionManager.class);
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public Connection getConnection() {

        openConnection();

        return connectionHolder.get();
    }

    public void openConnection() {

        Connection connection = connectionHolder.get();

        if (connection == null) {
            connection = new MySqlConnectionFactory().getConnection();
            connectionHolder.set(connection);
        }
    }

    public void closeConnection() {

        Connection connection = connectionHolder.get();

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.warn("Exception when closing database connection.", e);
            } finally {
                connectionHolder.remove();
            }
        }
    }
}
