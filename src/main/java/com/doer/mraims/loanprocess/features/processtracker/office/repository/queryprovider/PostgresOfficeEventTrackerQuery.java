package com.doer.mraims.loanprocess.features.processtracker.office.repository.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.utils.Table;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("postgresOfficeEventTrackerQuery")
public class PostgresOfficeEventTrackerQuery implements OfficeEventQueryProvider {

    public Map<String, Object> getOfficeEventTrackerQuery(AuthUser authUser, String managementProcessId, String officeId) {
        String query = "SELECT management_process_id, office_id, " +
                "office_event_tracker_id, office_event " +
                "FROM " + authUser.getSchemaName() + Table.OFFICE_EVENT_TRACKER +
                " WHERE management_process_id = ? AND office_id = ? " +
                "ORDER BY created_on ASC";

        String[] params = new String[]{managementProcessId, officeId};
        return Map.of("query", query, "params", params);
    }
}
