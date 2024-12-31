package com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.postgres;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.ManagementProcessTrackerQueryProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("PostgresManagementProcessTrackerQuery")
public class PostgresManagementProcessTrackerQuery implements ManagementProcessTrackerQueryProvider {

    public Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId) {
            String query = "SELECT " +
                "    mpt.management_process_id, " +
                "    mpt.office_id, " +
                "    o.office_name_en, " +
                "    o.office_name_bn, " +
                "    mpt.business_date, " +
                "    to_char(mpt.business_date, 'day') as business_day" +
                "from " + authUser.getSchemaName() + ".management_process_tracker mpt " +
                "    join "  + authUser.getSchemaName() + ".office o on mpt.office_id = o.office_id " +
                "where " +
                "    mpt.office_id = ?";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("officeId", officeId);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("query", query);
        objectMap.put("params", paramMap);
        return objectMap;
    }
}
