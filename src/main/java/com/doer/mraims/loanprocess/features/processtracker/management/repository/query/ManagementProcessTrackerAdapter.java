package com.doer.mraims.loanprocess.features.processtracker.management.repository.query;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.dao.QueryExecutorDao;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ManagementProcessTrackerAdapter {

    @Value("${database.platform.active}")
    private String dbType;

    private final ApplicationContext applicationContext;
    private final QueryExecutorDao queryExecutorDao;
    private ManagementProcessTrackerQueryProvider queryProvider;

    public ManagementProcessTrackerAdapter(ApplicationContext applicationContext, QueryExecutorDao queryExecutorDao) {
        this.applicationContext = applicationContext;
        this.queryExecutorDao = queryExecutorDao;
    }

    @PostConstruct
    public void init() {
        String qualifier = dbType + "ManagementProcessTrackerQuery";
        if (applicationContext.containsBean(qualifier)) {
            queryProvider = (ManagementProcessTrackerQueryProvider) applicationContext.getBean(qualifier);
        } else {
            throw new IllegalArgumentException("No query provider found for database type: " + dbType);
        }
    }

    public JSONObject getManagementProcessTrackerByOffice(AuthUser authUser, String officeId) {
        Map<String, Object> objectMap = queryProvider.getManagementProcessTrackerQuery(authUser, officeId);
        return queryExecutorDao.getSingleRow(objectMap);
    }

}
