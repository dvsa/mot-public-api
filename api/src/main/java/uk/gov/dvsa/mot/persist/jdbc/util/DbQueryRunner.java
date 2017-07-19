package uk.gov.dvsa.mot.persist.jdbc.util;

import java.util.List;

public interface DbQueryRunner {

    <T> T executeQuery(String sql, ResultSetMapper<T> mapper, Object... params);

    <T> List<T> executeQueryForList(String sql, ResultSetMapper<T> mapper, Object... params);
}
