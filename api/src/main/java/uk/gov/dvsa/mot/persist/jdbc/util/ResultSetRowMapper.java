package uk.gov.dvsa.mot.persist.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetRowMapper<T> {

    /**
     * Implementations must implement this method to map each row of data
     * in the ResultSet. This method should not call {@code next()} on
     * the ResultSet, as it is only supposed to map values of the current row.
     * @param rs the ResultSet to map
     * @return the result object for the current row
     * @throws SQLException if a SQLException is encountered.
     */
    T mapRow(ResultSet rs) throws SQLException;
}
