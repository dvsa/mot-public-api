package uk.gov.dvsa.mot.persist.jdbc.util;

import uk.gov.dvsa.mot.trade.api.InternalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbQueryRunnerImpl implements DbQueryRunner {

    private Connection connection;

    public DbQueryRunnerImpl(Connection connection) {

        this.connection = connection;
    }

    @Override
    public <T> T executeQuery(String sql, ResultSetMapper<T> mapper, Object... params) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] != null) {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
            }

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return mapper.map(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return null;
    }

    @Override
    public <T> List<T> executeQueryForList(String sql, ResultSetMapper<T> mapper, Object... params) {

        List<T> list = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] != null) {
                        stmt.setObject(i + 1, params[i]);
                    }
                }
            }

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    T result = mapper.map(resultSet);
                    list.add(result);
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return list;
    }
}
