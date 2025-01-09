package com.doer.mraims.loanprocess.features.processtracker.samity.repository.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.utils.Table;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("postgresSamityEventTrackerQuery")
public class PostgresSamityEventTrackerQuery implements SamityEventQueryProvider {

    public Map<String, Object> getSamityEventTrackerQuery(AuthUser authUser, String managementProcessId, String officeId) {
        String query = "SELECT management_process_id, samity_event_tracker_id, office_id, samity_id, samity_event " +
                "FROM " + authUser.getSchemaName() + Table.SAMITY_EVENT_TRACKER +
                " WHERE management_process_id = ? AND office_id = ? " +
                "ORDER BY created_on ASC";

        String[] params = new String[]{managementProcessId, officeId};
        return Map.of("query", query, "params", params);
    }
}
