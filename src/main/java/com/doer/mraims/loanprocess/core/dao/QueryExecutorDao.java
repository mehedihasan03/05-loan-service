package com.doer.mraims.loanprocess.core.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryExecutorDao implements QueryExecutorInterface {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int saveSingleRow(String sql, Object... params) {
        if (!StringUtils.hasText(sql)) {
            log.warn("SQL is null or empty {}", sql);
            return 0;
        }
        try {
            return jdbcTemplate.update(sql, params);
        } catch (Exception e) {
            log.error("Error while executing query: {}", sql, e);
            throw e;
        }
    }

    @Override
    public JSONArray getMultipleRows(String sql, Object[] params) {
        if (!StringUtils.hasText(sql)) {
            log.warn("SQL is null or empty {}", sql);
            return new JSONArray();
        }
        try {
            return new JSONArray(jdbcTemplate.queryForList(sql, params));
        } catch (Exception e) {
            log.error("Error while executing query: {}", sql, e);
            throw e;
        }
    }

    @Override
    public JSONObject getSingleRow(String sql, Object[] params) {
        if (!StringUtils.hasText(sql)) {
            log.warn("SQL is null or empty {}", sql);
            return new JSONObject();
        }
        try {
            return new JSONObject(jdbcTemplate.queryForMap(sql, params));
        } catch (Exception e) {
            log.error("Error while executing query: {}", sql, e);
            throw e;
        }
    }
}
