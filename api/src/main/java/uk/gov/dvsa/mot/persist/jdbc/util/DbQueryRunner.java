package uk.gov.dvsa.mot.persist.jdbc.util;

import java.util.List;

public interface DbQueryRunner {

    <T> T executeQuery(String sql, ResultSetRowMapper<T> mapper, Object... params);

    <T> T executeQuery(String sql, ResultSetExtractor<T> extractor, Object... params);

    <T> List<T> executeQueryForList(String sql, ResultSetRowMapper<T> mapper, Object... params);
}
