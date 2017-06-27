package uk.gov.dvsa.mot.mottest.read.core;

import uk.gov.dvsa.mot.persist.ConnectionFactory;
import uk.gov.dvsa.mot.persist.jdbc.MySqlConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public Connection getConnection() {

        openConnection();

        return connectionHolder.get();
    }

    public void openConnection() {

        Connection connection = connectionHolder.get();

        if (connection == null) {
            ConnectionFactory factory = new MySqlConnectionFactory();
            connection = factory.getConnection();
            connectionHolder.set(connection);
        }
    }

    public void closeConnection() {

        Connection connection = connectionHolder.get();

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connectionHolder.remove();
            }
        }
    }
}
