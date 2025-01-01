package com.doer.mraims.loanprocess.features.processtracker.management.repository.query.oracle;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.query.ManagementProcessTrackerQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("OracleManagementProcessTrackerQuery")
public class OracleManagementProcessTrackerQuery implements ManagementProcessTrackerQueryProvider {

        @Autowired
        private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId) {
        // Safely build the query string dynamically with schema and officeId
        String query = "SELECT mpt.MANAGEMENT_PROCESS_ID, " +
                "mpt.OFFICE_ID, " +
                "o.OFFICE_NAME_EN, " +
                "o.OFFICE_NAME_BN, " +
                "mpt.BUSINESS_DATE " +
                "FROM " + authUser.getSchemaName() + "MANAGEMENT_PROCESS_TRACKER mpt " +
                "JOIN " + authUser.getSchemaName() + "OFFICE o ON mpt.OFFICE_ID = o.OFFICE_ID " +
                "WHERE mpt.OFFICE_ID = ?";

        // Ensuring params are passed correctly as varargs
        Object[] params = new Object[] { officeId };  // Use Object[] to allow flexibility

        return Map.of("query", query, "params", params);
    }
}


