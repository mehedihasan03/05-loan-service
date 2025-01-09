package com.doer.mraims.loanprocess.features.processtracker.samity.repository;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.dao.QueryExecutorDao;
import com.doer.mraims.loanprocess.features.processtracker.samity.repository.queryprovider.SamityEventQueryProvider;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SamityEventTrackerAdapter {

    private final QueryExecutorDao queryExecutorDao;
    private final SamityEventQueryProvider queryProvider;

    @Autowired
    public SamityEventTrackerAdapter(
            QueryExecutorDao queryExecutorDao,
            @Value("${spring.profiles.active}") String dbType,
            ApplicationContext applicationContext
    ) {
        this.queryExecutorDao = queryExecutorDao;
        String qualifier = dbType + "SamityEventTrackerQuery";
        if (applicationContext.containsBean(qualifier)) {
            this.queryProvider = (SamityEventQueryProvider) applicationContext.getBean(qualifier);
        } else {
            throw new IllegalArgumentException("No query provider found for database type: " + dbType);
        }
    }

    public JSONArray getSamityEventTrackers(AuthUser authUser, String managementProcessId, String officeId) {
        try {
            Map<String, Object> objectMap = queryProvider.getSamityEventTrackerQuery(authUser, managementProcessId, officeId);
            String query = (String) objectMap.get("query");
            Object[] params = (Object[]) objectMap.get("params");
            return queryExecutorDao.getMultipleRowsData(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving samity event tracker data: " + e.getMessage(), e);
        }
    }


}
