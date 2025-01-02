package com.doer.mraims.loanprocess.features.processtracker.management.service;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.query.ManagementProcessTrackerAdapter;
import com.doer.mraims.loanprocess.features.processtracker.management.response.ManagementResponseDto;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ManagementProcessTrackerService {


    private final ManagementProcessTrackerAdapter managementProcessTrackerAdapter;
    private final Gson gson;

    public ManagementProcessTrackerService(ManagementProcessTrackerAdapter managementProcessTrackerAdapter, Gson gson) {
        this.managementProcessTrackerAdapter = managementProcessTrackerAdapter;
        this.gson = gson;
    }

    public CommonObjectResponseDTO<ManagementResponseDto> getManagementProcessTrackerByOffice(AuthUser authUser, String officeId) {
        if (authUser == null || authUser.getSchemaName() == null || authUser.getSchemaName().isEmpty()) {
            return new CommonObjectResponseDTO<>(false, 400, "Schema name cannot be null or empty", null);
        }
        if (officeId == null || officeId.isEmpty()) {
            return new CommonObjectResponseDTO<>(false, 400, "Office ID cannot be null or empty", null);
        }
        try {
            JSONObject managementProcessTrackerByOffice = managementProcessTrackerAdapter.getManagementProcessTrackerByOffice(authUser, officeId);
            log.info("Management process tracker by office: {}", managementProcessTrackerByOffice);
            ManagementResponseDto managementResponseDto = gson.fromJson(managementProcessTrackerByOffice.toString(), ManagementResponseDto.class);
            return new CommonObjectResponseDTO<>(true, 200, "Data retrieved successfully", managementResponseDto);
        } catch (Exception e) {
            return new CommonObjectResponseDTO<>(false, 500, "Error retrieving data: " + e.getMessage(), null);
        }
    }
}
