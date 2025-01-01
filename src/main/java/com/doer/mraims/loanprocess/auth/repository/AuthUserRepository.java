package com.doer.mraims.loanprocess.auth.repository;

import com.doer.mraims.loanprocess.core.utils.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static com.doer.mraims.loanprocess.core.utils.DBSchema.COMMON;

@Slf4j
@Repository
public class AuthUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuthUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getOracleTokenUserStatus(String userId) {
        String sql = String.format(
                "SELECT t.revoked AS revoked, lh.status AS status " +
                        "FROM %s%s t, %s%s lh " +
                        "WHERE t.user_id = lh.user_id AND t.user_id = ? " +
                        "ORDER BY t.expiry_date DESC, lh.signin_time DESC FETCH NEXT 1 ROWS ONLY",
                COMMON.getSchema(), Table.SEC_REFRESH_TOKEN, COMMON.getSchema(), Table.SEC_LOGIN_HISTORY
        );

        try {
            Map<String, Object> objectMap = jdbcTemplate.queryForMap(sql, userId);

            return objectMap;
        } catch (Exception e) {
            log.error("An exception occurred while getting token status from Oracle: {}", e.getMessage());
            throw new RuntimeException("Error fetching Oracle token user status", e);
        }
    }

    public Map<String, Object> getPostgresTokenUserStatus(String userId) {
        String sql = String.format(
                "SELECT t.revoked AS revoked, lh.status AS status " +
                        "FROM %s%s t, %s%s lh " +
                        "WHERE t.user_id = lh.user_id AND t.user_id = ? " +
                        "ORDER BY t.expiry_date DESC, lh.signin_time DESC",
                COMMON.getSchema(), Table.SEC_REFRESH_TOKEN, COMMON.getSchema(), Table.SEC_LOGIN_HISTORY
        );

        try {
            return jdbcTemplate.queryForMap(sql, userId);
        } catch (Exception e) {
            log.error("An exception occurred while getting token status from Postgres: {}", e.getMessage());
            throw new RuntimeException("Error fetching Postgres token user status", e);
        }
    }
}
