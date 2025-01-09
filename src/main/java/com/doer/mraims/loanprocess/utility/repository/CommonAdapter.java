package com.doer.mraims.loanprocess.utility.repository;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.dao.QueryExecutorDao;
import com.doer.mraims.loanprocess.utility.queryprovider.CommonQueryProvider;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class CommonAdapter {

    private final QueryExecutorDao queryExecutorDao;
    private final CommonQueryProvider commonQueryProvider;

    @Autowired
    public CommonAdapter(
            QueryExecutorDao queryExecutorDao,
            @Value("${spring.profiles.active}") String dbType,
            ApplicationContext applicationContext
    ) {
        this.queryExecutorDao = queryExecutorDao;
        String qualifier = dbType + "CommonQuery";
        if (applicationContext.containsBean(qualifier)) {
            this.commonQueryProvider = (CommonQueryProvider) applicationContext.getBean(qualifier);
        } else {
            throw new IllegalArgumentException("No query provider found for database type: " + dbType);
        }
    }

    public JSONObject getManagementProcessTrackerByOffice(AuthUser authUser, String officeId) {
        try {
            Map<String, Object> objectMap = commonQueryProvider.getManagementProcessTrackerQuery(authUser, officeId);
            String query = (String) objectMap.get("query");
            Object[] params = (Object[]) objectMap.get("params");
            return queryExecutorDao.getSingleRowData(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving management process tracker for officeId: " + officeId, e);
        }
    }

    public JSONObject getNextBusinessCalendarData(AuthUser authUser, String officeId, String currentBusinessDate) {
        try {
            Map<String, Object> objectMap = commonQueryProvider.getNextBusinessCalenderData(authUser, officeId, currentBusinessDate);
            String query = (String) objectMap.get("query");
            Object[] params = new Object[]{officeId, currentBusinessDate};
            return queryExecutorDao.getSingleRowData(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving next business calendar data for officeId: " + officeId, e);
        }
    }

    public JSONObject getNextHolidayEntry(AuthUser authUser, String officeId, String currentBusinessDate) {
        try {
            Map<String, Object> objectMap = commonQueryProvider.getNextHolidayData(authUser, officeId, currentBusinessDate);
            String query = (String) objectMap.get("query");
            Object[] params = new Object[]{officeId, currentBusinessDate};
            return queryExecutorDao.getSingleRowData(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving next holiday data for officeId: " + officeId, e);
        }
    }

    public JSONArray getStagingDataProcessTracker(AuthUser authUser, String managementProcessId, String officeId) {
        try {
            Map<String, Object> objectMap = commonQueryProvider.getStagingProcessTrackerList(authUser, managementProcessId, officeId);
            String query = (String) objectMap.get("query");
            Object[] params = (Object[]) objectMap.get("params");
            JSONArray multipleRowsData = queryExecutorDao.getMultipleRowsData(query, params);
            log.info("Staging data process tracker for officeId: {} is: {}", officeId, multipleRowsData);
            return multipleRowsData;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving staging data process tracker for officeId: " + officeId, e);
        }
    }
}
