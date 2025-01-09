package com.doer.mraims.loanprocess.features.stagingdata.repository;

import com.doer.mraims.loanprocess.core.dao.QueryExecutorDao;
import com.doer.mraims.loanprocess.features.stagingdata.repository.queryprovider.StagingDataQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StagingDataAdapter {

    private final QueryExecutorDao queryExecutorDao;
    private final StagingDataQueryProvider stagingDataQueryProvider;

    @Autowired
    public StagingDataAdapter(
            QueryExecutorDao queryExecutorDao,
            @Value("${spring.profiles.active}") String dbType,
            ApplicationContext applicationContext
    ) {
        this.queryExecutorDao = queryExecutorDao;
        String qualifier = dbType + "StagingDataQuery";
        if (applicationContext.containsBean(qualifier)) {
            this.stagingDataQueryProvider = (StagingDataQueryProvider) applicationContext.getBean(qualifier);
        } else {
            throw new IllegalArgumentException("No query provider found for database type: " + dbType);
        }
    }

}
