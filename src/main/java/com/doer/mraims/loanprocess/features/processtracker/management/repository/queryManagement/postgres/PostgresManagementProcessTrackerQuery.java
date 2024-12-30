package com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.postgres;

import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.ManagementProcessTrackerQueryProvider;
import org.springframework.stereotype.Component;

@Component("PostgresManagementProcessTrackerQuery")
public class PostgresManagementProcessTrackerQuery implements ManagementProcessTrackerQueryProvider {

    public String getManagementProcessTrackerQuery(String schemaName) {
        return "select " +
                "    mpt.management_process_id, " +
                "    mpt.office_id, " +
                "    o.office_name_en, " +
                "    o.office_name_bn, " +
                "    mpt.business_date, " +
                "    to_char(mpt.business_date, 'day') as business_day" +
                "from " + schemaName + ".management_process_tracker mpt " +
                "    join "  + schemaName + ".office o on mpt.office_id = o.office_id " +
                "where " +
                "    mpt.office_id = ?";

    }
}
