package com.doer.mraims.loanprocess.features.processtracker.samity.service;

import com.doer.mraims.loanprocess.features.processtracker.management.service.ManagementProcessTrackerService;
import com.doer.mraims.loanprocess.features.processtracker.samity.repository.SamityEventTrackerAdapter;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SamityEventTrackerService {


    private final SamityEventTrackerAdapter samityEventTrackerAdapter;
    private final Gson gson;
    private final ManagementProcessTrackerService managementProcessTrackerService;

    public SamityEventTrackerService(SamityEventTrackerAdapter samityEventTrackerAdapter, Gson gson, ManagementProcessTrackerService managementProcessTrackerService) {
        this.samityEventTrackerAdapter = samityEventTrackerAdapter;
        this.gson = gson;
        this.managementProcessTrackerService = managementProcessTrackerService;
    }

    /*public CommonObjectResponseDTO<ManagementResponseDto> getManagementProcessTrackerByOffice(ManagementRequestDto request, String managementProcessTracker, AuthUser authUser) {
        if (authUser == null || authUser.getSchemaName() == null || authUser.getSchemaName().isEmpty()) {
            return new CommonObjectResponseDTO<>(false, 400, "Schema name cannot be null or empty", null);
        }
        try {
            JSONArray jsonArray = samityEventTrackerAdapter.getSamityEventTrackers(authUser, managementProcessTracker, request.getOfficeId());
            log.info("Management process tracker by office: {}", jsonArray);
            ManagementResponseDto managementResponseDto = gson.fromJson(managementProcessTrackerByOffice.toString(), ManagementResponseDto.class);
            return new CommonObjectResponseDTO<>(true, 200, "Data retrieved successfully", managementResponseDto);
        } catch (Exception e) {
            return new CommonObjectResponseDTO<>(false, 500, "Error retrieving data: " + e.getMessage(), null);
        }
    }*/
}
