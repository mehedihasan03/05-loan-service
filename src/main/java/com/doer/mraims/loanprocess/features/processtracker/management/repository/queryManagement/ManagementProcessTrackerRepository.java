package com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement;

import com.doer.mraims.loanprocess.core.dao.QueryExecutorInterface;
import com.doer.mraims.loanprocess.core.utils.UtilService;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.oracle.OracleManagementProcessTrackerQuery;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.postgres.PostgresManagementProcessTrackerQuery;
import com.doer.mraims.loanprocess.features.processtracker.management.response.ManagementResponseDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagementProcessTrackerRepository {

    @Value("${database.platform.active}")
    private String dbType;

    private final UtilService utilService;


    private final PostgresManagementProcessTrackerQuery postgresQueryProvider;
    private final OracleManagementProcessTrackerQuery oracleQueryProvider;
    private final QueryExecutorInterface queryExecutorInterface;
    private final ModelMapper modelMapper;

    @Qualifier("${database.platform.active}" + "ManagementProcessTrackerQuery")
    private ManagementProcessTrackerQueryProvider queryProvider;

    public ManagementResponseDto getManagementProcessTrackerByOffice(String officeId, String schemaName) {

        Map<String, ManagementProcessTrackerQueryProvider> queryProviders = Map.of(
                "postgres", postgresQueryProvider,
                "oracle", oracleQueryProvider
        );

        ManagementProcessTrackerQueryProvider queryProvider = queryProviders.get(dbType.toLowerCase());

        if (queryProvider == null) {
            throw new IllegalArgumentException("No query provider found for dbType: " + dbType);
        }

        String sql = queryProvider.getManagementProcessTrackerQuery(schemaName);
        Object[] params = {officeId};

        JSONObject singleRow = queryExecutorInterface.getSingleRow(sql, params);

        Map<String, Object> camelCaseKeys = UtilService.toCamelCaseKeys(singleRow.toMap());

        return modelMapper.map(camelCaseKeys, ManagementResponseDto.class);
    }

}
