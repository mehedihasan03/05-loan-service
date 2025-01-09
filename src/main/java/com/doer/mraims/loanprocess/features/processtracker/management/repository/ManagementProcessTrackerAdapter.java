package com.doer.mraims.loanprocess.features.processtracker.management.repository;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.dao.QueryExecutorDao;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryprovider.ManagementProcessTrackerQueryProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ManagementProcessTrackerAdapter {

/*    @Autowired
    private QueryExecutorDao queryExecutorDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${database.platform.active}")
    private String dbType;

    private ManagementProcessTrackerQueryProvider queryProvider;

    @PostConstruct
    private void init() {
        String qualifier = dbType + "ManagementProcessTrackerQuery";
        if (applicationContext.containsBean(qualifier)) {
            this.queryProvider = (ManagementProcessTrackerQueryProvider) applicationContext.getBean(qualifier);
        } else {
            throw new IllegalArgumentException("No query provider found for database type: " + dbType);
        }
    }*/

    private final QueryExecutorDao queryExecutorDao;
    private final ManagementProcessTrackerQueryProvider queryProvider;

    @Autowired
    public ManagementProcessTrackerAdapter(
            QueryExecutorDao queryExecutorDao,
            @Value("${spring.profiles.active}") String dbType,
            ApplicationContext applicationContext
    ) {
        this.queryExecutorDao = queryExecutorDao;
        String qualifier = dbType + "ManagementProcessTrackerQuery";
        if (applicationContext.containsBean(qualifier)) {
            this.queryProvider = (ManagementProcessTrackerQueryProvider) applicationContext.getBean(qualifier);
        } else {
            throw new IllegalArgumentException("No query provider found for database type: " + dbType);
        }
    }

    public JSONObject getManagementProcessTrackerByOffice(AuthUser authUser, String officeId) {
        try {
            Map<String, Object> objectMap = queryProvider.getManagementProcessTrackerQuery(authUser, officeId);
            String query = (String) objectMap.get("query");
            Object[] params = (Object[]) objectMap.get("params");
            return queryExecutorDao.getSingleRowData(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving management process tracker for officeId: " + officeId, e);
        }
    }

    public JSONObject getNextBusinessCalendarData(AuthUser authUser, String officeId, String currentBusinessDate) {
        try {
            Map<String, Object> objectMap = queryProvider.getNextBusinessCalenderData(authUser, officeId, currentBusinessDate);
            String query = (String) objectMap.get("query");
            Object[] params = new Object[]{officeId, currentBusinessDate};
            return queryExecutorDao.getSingleRowData(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving next business calendar data for officeId: " + officeId, e);
        }
    }

    public JSONObject getNextHolidayEntry(AuthUser authUser, String officeId, String currentBusinessDate) {
        try {
            Map<String, Object> objectMap = queryProvider.getNextHolidayData(authUser, officeId, currentBusinessDate);
            String query = (String) objectMap.get("query");
            Object[] params = new Object[]{officeId, currentBusinessDate};
            return queryExecutorDao.getSingleRowData(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving next holiday data for officeId: " + officeId, e);
        }
    }

}
