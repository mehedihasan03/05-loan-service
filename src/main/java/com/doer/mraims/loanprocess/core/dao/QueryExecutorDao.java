package com.doer.mraims.loanprocess.core.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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


    public JSONObject getSingleRow(Map<String, Object> objectMap) {
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap((String) objectMap.get("query"), objectMap.get("params"));

            JSONObject result = new JSONObject();
            row.forEach((key, value) -> result.put(key.toLowerCase(), value));

            return result;
        } catch (EmptyResultDataAccessException e) {
            log.warn("No data found for query: {}", objectMap.get("query"));
            return new JSONObject();
        } catch (Exception e) {
            log.error("Error while executing query: {}", objectMap.get("query"), e);
            throw new RuntimeException("Failed to execute query", e);
        }
    }

}
