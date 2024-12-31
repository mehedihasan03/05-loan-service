package com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.dao.QueryExecutorInterface;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.core.utils.UtilService;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.oracle.OracleManagementProcessTrackerQuery;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.postgres.PostgresManagementProcessTrackerQuery;
import com.doer.mraims.loanprocess.features.processtracker.management.response.ManagementResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ManagementProcessTrackerRepository {

    @Value("${database.platform.active}")
    private String dbType;

    @Autowired
    private ApplicationContext applicationContext;
    private final QueryExecutorInterface queryExecutorInterface;
    private ManagementProcessTrackerQueryProvider queryProvider;

    public ManagementProcessTrackerRepository(QueryExecutorInterface queryExecutorInterface) {
        this.queryExecutorInterface = queryExecutorInterface;
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
        return queryExecutorInterface.getSingleRow(objectMap);
    }

}
