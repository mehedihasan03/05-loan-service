package com.doer.mraims.loanprocess.utility.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.utils.Table;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("oracleCommonQuery")
public class OracleCommonQuery implements CommonQueryProvider {

    @Override
    public Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId) {
        String query = "SELECT MANAGEMENT_PROCESS_ID, OFFICE_ID, OFFICE_NAME_EN, " +
                "OFFICE_NAME_BN, BUSINESS_DATE, BUSINESS_DAY, MFI_ID " +
                "FROM " + authUser.getSchemaName() + Table.MANAGEMENT_PROCESS_TRACKER +
                " WHERE OFFICE_ID = ? " +
                "ORDER BY BUSINESS_DATE DESC " +
                "FETCH FIRST 1 ROW ONLY";

        Object[] params = new Object[]{officeId};
        return Map.of("query", query, "params", params);
    }

    @Override
    public Map<String, Object> getNextBusinessCalenderData(AuthUser authUser, String officeId, String currentBusinessDate) {
        String query = "SELECT * " +
                "FROM " + authUser.getSchemaName() + Table.CALENDAR +
                " WHERE OFFICE_ID = ? " +
                "AND IS_WORKING_DAY = 'Yes' " +
                "AND CALENDAR_DATE > TO_DATE(?, 'YYYY-MM-DD') " +
                "ORDER BY CALENDAR_DATE FETCH FIRST 1 ROWS ONLY";

        Object[] params = new Object[]{officeId, currentBusinessDate};
        return Map.of("query", query, "params", params);
    }

    @Override
    public Map<String, Object> getNextHolidayData(AuthUser authUser, String officeId, String currentBusinessDate) {
        String query = "SELECT * " +
                "FROM " + authUser.getSchemaName() + Table.HOLIDAY +
                " WHERE OFFICE_ID = ? AND HOLIDAY_DATE > TO_DATE(?, 'YYYY-MM-DD') " +
                "ORDER BY HOLIDAY_DATE FETCH FIRST 1 ROWS ONLY";

        Object[] params = new Object[]{officeId, currentBusinessDate};
        return Map.of("query", query, "params", params);
    }

    @Override
    public Map<String, Object> getStagingProcessTrackerList(AuthUser authUser, String managementProcessId, String officeId) {
        String query = "SELECT * " +
                "FROM " + authUser.getSchemaName() + Table.STAGING_PROCESS_TRACKER +
                " WHERE MANAGEMENT_PROCESS_ID = ? AND OFFICE_ID = ? ";
        Object[] params = new Object[]{officeId, officeId};
        return Map.of("query", query, "params", params);
    }


}
