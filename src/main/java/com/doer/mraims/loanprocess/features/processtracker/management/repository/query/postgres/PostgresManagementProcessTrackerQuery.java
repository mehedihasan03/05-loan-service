package com.doer.mraims.loanprocess.features.processtracker.management.repository.query.postgres;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.query.ManagementProcessTrackerQueryProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("PostgresManagementProcessTrackerQuery")
public class PostgresManagementProcessTrackerQuery implements ManagementProcessTrackerQueryProvider {

    public Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId) {
        String query = "SELECT mpt.MANAGEMENT_PROCESS_ID, " +
                "mpt.OFFICE_ID, " +
                "o.OFFICE_NAME_EN, " +
                "o.OFFICE_NAME_BN, " +
                "mpt.BUSINESS_DATE " +
                "FROM " + authUser.getSchemaName() + ".MANAGEMENT_PROCESS_TRACKER mpt " +
                "JOIN " + authUser.getSchemaName() + ".OFFICE o ON mpt.OFFICE_ID = o.OFFICE_ID " +
                "WHERE mpt.OFFICE_ID = ?";
        String[] params = new String[]{officeId};
        return Map.of("query", new String[]{query}, "params", params);
    }
}
