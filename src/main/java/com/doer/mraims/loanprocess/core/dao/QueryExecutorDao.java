package com.doer.mraims.loanprocess.core.dao;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository("queryExecutorDao")
public class QueryExecutorDao {

    private final JdbcTemplate jdbcTemplate;

    public QueryExecutorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public int saveSingleRow(String sql, Object... params) {
        if (!StringUtils.hasText(sql)) {
            log.warn("SQL is null or empty: {}", sql);
            return 0;
        }

        try {
            log.debug("Executing SQL: {} with params: {}", sql, params);
            return jdbcTemplate.update(sql, params);
        } catch (DataAccessException dae) {
            log.error("Database access error while executing query: {}", sql, dae);
            throw new RuntimeException("Failed to execute update query", dae);
        } catch (Exception e) {
            log.error("Unexpected error while executing query: {}", sql, e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }


    public JSONArray getMultipleRows(Map<String, Object> objectMap) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList((String) objectMap.get("query"), objectMap.get("params"));
            JSONArray result = new JSONArray();
            for (Map<String, Object> row : rows) {
                JSONObject json = new JSONObject();
                json.put("name", new BigDecimal(0));
                row.forEach((key, value) -> json.put(key.toLowerCase(), value));
                result.put(json);
            }
            return result;
        } catch (Exception e) {
            log.error("Error while executing query: {}", objectMap.get("query"), e);
            throw new RuntimeException("Failed to execute query", e);
        }
    }


    public JSONObject getSingleRow(String sqlQuery, Object... params) {
        try {
            // Ensure parameters are passed correctly as varargs
            Map<String, Object> row = jdbcTemplate.queryForMap(sqlQuery, params.getClass().isArray() ? params : params.clone());
            // Convert result to JSONObject with lowercase keys
            JSONObject result = new JSONObject();
            row.forEach((key, value) -> result.put(key.toLowerCase(), value));
            return result;
        } catch (EmptyResultDataAccessException e) {
            log.warn("No data found for query: {}", sqlQuery);
            return new JSONObject();  // Return an empty JSON object if no results
        } catch (Exception e) {
            log.error("Error while executing query: {}", sqlQuery, e);
            throw new RuntimeException("Failed to execute query: " + e.getMessage(), e);
        }
    }




}
