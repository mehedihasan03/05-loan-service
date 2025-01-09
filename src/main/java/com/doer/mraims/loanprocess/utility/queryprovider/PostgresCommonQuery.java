package com.doer.mraims.loanprocess.utility.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.utils.Table;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("postgresCommonQuery")
public class PostgresCommonQuery implements CommonQueryProvider {

    public Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId) {
        String query = "SELECT management_process_id, office_id, office_name_en, office_name_bn, business_date, business_day, mfi_id " +
                "FROM " + authUser.getSchemaName() + Table.MANAGEMENT_PROCESS_TRACKER +
                " WHERE office_id = ? " +
                "ORDER BY business_date DESC " +
                "limit 1";

        String[] params = new String[]{officeId};
        return Map.of("query", query, "params", params);
    }

    @Override
    public Map<String, Object> getNextBusinessCalenderData(AuthUser authUser, String officeId, String currentBusinessDate) {
        String query = "SELECT * " +
                "FROM " + authUser.getSchemaName() + Table.CALENDAR +
                " WHERE OFFICE_ID = ? " +
                "AND IS_WORKING_DAY = 'Yes' " +
                "AND CALENDAR_DATE > TO_DATE(?, 'YYYY-MM-DD') " +
                "ORDER BY CALENDAR_DATE ASC " +
                "LIMIT 1";

        Object[] params = new Object[]{officeId, currentBusinessDate};
        return Map.of("query", query, "params", params);
    }

    @Override
    public Map<String, Object> getNextHolidayData(AuthUser authUser, String officeId, String currentBusinessDate) {
        String query = "SELECT * " +
                "FROM " + authUser.getSchemaName() + Table.HOLIDAY +
                " WHERE OFFICE_ID = ? " +
                "AND HOLIDAY_DATE > TO_DATE(?, 'YYYY-MM-DD') " +
                "ORDER BY HOLIDAY_DATE ASC " +
                "LIMIT 1";

        Object[] params = new Object[]{officeId, currentBusinessDate};
        return Map.of("query", query, "params", params);
    }

    @Override
    public Map<String, Object> getStagingProcessTrackerList(AuthUser authUser, String managementProcessId, String officeId) {
        String query = "SELECT * " +
                "FROM " + authUser.getSchemaName() + Table.STAGING_PROCESS_TRACKER +
                " WHERE MANAGEMENT_PROCESS_ID = ? AND OFFICE_ID = ? ";
        Object[] params = new Object[]{managementProcessId, officeId};
        return Map.of("query", query, "params", params);
    }

}
