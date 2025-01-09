package com.doer.mraims.loanprocess.features.processtracker.management.repository.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.utils.Table;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("postgresManagementProcessTrackerQuery")
public class PostgresManagementProcessTrackerQuery implements ManagementProcessTrackerQueryProvider {

    public Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId) {
        String query = "SELECT mpt.MANAGEMENT_PROCESS_ID, " +
                "mpt.OFFICE_ID, " +
                "o.OFFICE_NAME_EN, " +
                "o.OFFICE_NAME_BN, " +
                "mpt.BUSINESS_DATE " +
                "FROM " + authUser.getSchemaName() + Table.MANAGEMENT_PROCESS_TRACKER + " mpt " +
                "JOIN " + authUser.getSchemaName() + Table.OFFICE + " o ON mpt.OFFICE_ID = o.OFFICE_ID " +
                "WHERE mpt.OFFICE_ID = ?";

        String[] params = new String[]{officeId};
        return Map.of("query", new String[]{query}, "params", params);
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

}
