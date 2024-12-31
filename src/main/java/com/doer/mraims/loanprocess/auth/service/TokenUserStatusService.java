package com.doer.mraims.loanprocess.auth.service;
import com.doer.mraims.loanprocess.core.utils.Table;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.doer.mraims.loanprocess.core.utils.DBSchema.*;

@Slf4j
@Service
public class TokenUserStatusService {

    private final JdbcTemplate jdbcTemplate;

    public TokenUserStatusService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JSONObject getOracleTokenUserStatus(String userId) {
        String sql = String.format(
                "SELECT t.revoked AS revoked, lh.status AS status " +
                        "FROM %s%s t, %s%s lh " +
                        "WHERE t.user_id = lh.user_id AND t.user_id = ? " +
                        "ORDER BY t.expiry_date DESC, lh.signin_time DESC FETCH NEXT 1 ROWS ONLY",
                COMMON, Table.SEC_REFRESH_TOKEN, COMMON, Table.LOGIN_HISTORY
        );

        try {
            return (JSONObject) jdbcTemplate.queryForMap(sql, userId);
        } catch (Exception e) {
            log.error("An exception occurred while getting token status from Oracle: {}", e.getMessage());
            throw new RuntimeException("Error fetching Oracle token user status", e);
        }
    }

    public JSONObject getPostgresTokenUserStatus(String userId) {
        String sql = String.format(
                "SELECT t.revoked AS revoked, lh.status AS status " +
                        "FROM %s%s t, %s%s lh " +
                        "WHERE t.user_id = lh.user_id AND t.user_id = ? " +
                        "ORDER BY t.expiry_date DESC, lh.signin_time DESC",
                COMMON, Table.SEC_REFRESH_TOKEN, COMMON, Table.LOGIN_HISTORY
        );

        try {
            return (JSONObject) jdbcTemplate.queryForMap(sql, userId);
        } catch (Exception e) {
            log.error("An exception occurred while getting token status from Postgres: {}", e.getMessage());
            throw new RuntimeException("Error fetching Postgres token user status", e);
        }
    }
}
