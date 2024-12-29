package com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.oracle;

public class OracleManagementProcessTrackerQuery {

    public String getManagementProcessTrackerQuery(String schemaName) {
        return "SELECT " +
                "    MPT.MANAGEMENT_PROCESS_ID, " +
                "    MPT.OFFICE_ID, " +
                "    O.OFFICE_NAME_EN, " +
                "    O.OFFICE_NAME_BN, " +
                "    MPT.BUSINESS_DATE, " +
                "    TO_CHAR(MPT.BUSINESS_DATE, 'DAY') AS BUSINESS_DAY, " +
                "FROM " +
                "    " + schemaName + ".MANAGEMENT_PROCESS_TRACKER MPT " +
                "    JOIN " + schemaName + ".OFFICE O ON MPT.OFFICE_ID = O.OFFICE_ID " +
                "WHERE " +
                "    MPT.MANAGEMENT_PROCESS_ID = ? ";

    }
}
