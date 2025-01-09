package com.doer.mraims.loanprocess.core.dao;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository("queryExecutorDao")
public class QueryExecutorDao {

    private final JdbcTemplate jdbcTemplate;

    public QueryExecutorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int executeSingleInsertOrUpdateQuery(String sql, Object... params) {
        if (!StringUtils.hasText(sql)) {
            log.info("SQL is null or empty: {}", sql);
            return 0;
        }
        try {
            log.info("Executing SQL: {} with params: {}", sql, params);
            return jdbcTemplate.update(sql, params);
        } catch (DataAccessException dae) {
            log.error("Database access error while executing query: {}", sql, dae);
            throw new RuntimeException("Failed to execute update query", dae);
        } catch (Exception e) {
            log.error("Unexpected error while executing query: {}", sql, e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Transactional
    public void executeMultipleInsertOrUpdateQueries(List<String> sql, List<Object[]> parameters) {
        try {
            for (int i = 0; i < sql.size(); i++) {
                String query = sql.get(i);
                Object[] params = parameters.get(i);
                log.debug("Executing query: {} with params: {}", query, params);
                jdbcTemplate.update(query, params);
            }
            log.info("All queries executed successfully, committing transaction");
        } catch (DataAccessException dae) {
            log.error("Error while executing queries, rolling back transaction: ", dae);
            throw new RuntimeException("Error executing queries", dae);
        } catch (Exception e) {
            log.error("Unexpected error while executing queries:", e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    public JSONArray getMultipleRowsData(String query, Object... params) {
        JSONArray result = new JSONArray();
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, params);
            for (Map<String, Object> row : rows) {
                JSONObject json = new JSONObject();
                row.forEach((key, value) -> json.put(key.toLowerCase(), value));
                result.put(json);
            }
            return result;
        } catch (EmptyResultDataAccessException e) {
            log.info("No data found for query: {}", query);
            return new JSONArray();
        } catch (Exception e) {
            log.error("Error while executing query: {}", query, e);
            throw new RuntimeException("Failed to execute query: " + e.getMessage(), e);
        }
    }

    public JSONObject getSingleRowData(String query, Object... params) {
        JSONObject result = new JSONObject();
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(query, params.getClass().isArray() ? params : params.clone());
            row.forEach((key, value) -> result.put(key.toLowerCase(), value));
            return result;
        } catch (EmptyResultDataAccessException e) {
            log.info("No data found for query : {}", query);
            return null;
        } catch (Exception e) {
            log.error("Error while executing query : {}", query, e);
            throw new RuntimeException("Failed to execute query : " + e.getMessage(), e);
        }
    }
}
