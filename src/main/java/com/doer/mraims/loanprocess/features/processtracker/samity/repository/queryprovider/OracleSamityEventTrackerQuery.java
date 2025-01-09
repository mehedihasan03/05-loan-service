package com.doer.mraims.loanprocess.features.processtracker.samity.repository.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.utils.Table;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("oracleSamityEventTrackerQuery")
public class OracleSamityEventTrackerQuery implements SamityEventQueryProvider {

    @Override
    public Map<String, Object> getSamityEventTrackerQuery(AuthUser authUser, String managementProcessId, String officeId) {
        String query = "SELECT MANAGEMENT_PROCESS_ID, SAMITY_EVENT_TRACKER_ID, " +
                "OFFICE_ID, SAMITY_ID, SAMITY_EVENT, " +
                "FROM " + authUser.getSchemaName() + Table.SAMITY_EVENT_TRACKER +
                " WHERE MANAGEMENT_PROCESS_ID = ? AND OFFICE_ID = ? " +
                "ORDER BY CREATED_ON ASC";

        Object[] params = new Object[]{officeId};
        return Map.of("query", query, "params", params);
    }
}
