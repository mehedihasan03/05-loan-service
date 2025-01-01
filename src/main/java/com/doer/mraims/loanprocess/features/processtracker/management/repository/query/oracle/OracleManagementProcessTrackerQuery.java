package com.doer.mraims.loanprocess.features.processtracker.management.repository.query.oracle;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.query.ManagementProcessTrackerQueryProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("OracleManagementProcessTrackerQuery")
public class OracleManagementProcessTrackerQuery implements ManagementProcessTrackerQueryProvider {

    public Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId) {
        String query = "SELECT " +
                "    MPT.MANAGEMENT_PROCESS_ID, " +
                "    MPT.OFFICE_ID, " +
                "    O.OFFICE_NAME_EN, " +
                "    O.OFFICE_NAME_BN, " +
                "    MPT.BUSINESS_DATE, " +
                "    TO_CHAR(MPT.BUSINESS_DATE, 'DAY') AS BUSINESS_DAY " +
                "FROM " +
                "    " + authUser.getSchemaName() + ".MANAGEMENT_PROCESS_TRACKER MPT " +
                "    JOIN " + authUser.getSchemaName() + ".OFFICE O ON MPT.OFFICE_ID = O.OFFICE_ID " +
                "WHERE " +
                "    MPT.OFFICE_ID = :officeId";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("officeId", officeId);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("query", query);
        objectMap.put("params", paramMap);
        return objectMap;
    }
}
