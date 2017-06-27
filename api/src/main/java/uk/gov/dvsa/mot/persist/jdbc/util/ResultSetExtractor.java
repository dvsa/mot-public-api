package uk.gov.dvsa.mot.persist.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<T> {

    /**
     * Implementations must implement this method to process the entire ResultSet.
     * @param rs the ResultSet to extract data from.
     * @return an arbitrary result object, or {@code null} if none.
     * @throws SQLException if a SQLException is encountered
     */
    T extractData(ResultSet rs) throws SQLException;
}
