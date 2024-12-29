package com.doer.mraims.loanprocess.core.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryExecutorDao implements QueryExecutorInterface {

    private final JdbcTemplate jdbcTemplate;

    @Override
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


    @Override
    public JSONArray getMultipleRows(String sql, Object[] params) {
        if (!StringUtils.hasText(sql)) {
            log.warn("SQL is null or empty: {}", sql);
            return new JSONArray();
        }

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);

            JSONArray result = new JSONArray();
            for (Map<String, Object> row : rows) {
                JSONObject json = new JSONObject();
                row.forEach((key, value) -> json.put(key.toLowerCase(), value));
                result.put(json);
            }

            return result;
        } catch (Exception e) {
            log.error("Error while executing query: {}", sql, e);
            throw new RuntimeException("Failed to execute query", e);
        }
    }

    @Override
    public JSONObject getSingleRow(String sql, Object[] params) {
        if (!StringUtils.hasText(sql)) {
            log.warn("SQL is null or empty: {}", sql);
            return new JSONObject();
        }

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, params);

            JSONObject result = new JSONObject();
            row.forEach((key, value) -> result.put(key.toLowerCase(), value));

            return result;
        } catch (EmptyResultDataAccessException e) {
            log.warn("No data found for query: {}", sql);
            return new JSONObject();
        } catch (Exception e) {
            log.error("Error while executing query: {}", sql, e);
            throw new RuntimeException("Failed to execute query", e);
        }
    }

}
