package com.doer.mraims.loanprocess.features.processtracker.management.service;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.ManagementProcessTrackerRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagementProcessTrackerService {

    @Autowired
    private ManagementProcessTrackerRepository managementProcessTrackerRepository;

    public ManagementProcessTrackerService(ManagementProcessTrackerRepository managementProcessTrackerRepository) {
        this.managementProcessTrackerRepository = managementProcessTrackerRepository;
    }

    public CommonObjectResponseDTO<JSONObject> getManagementProcessTrackerByOffice(AuthUser authUser, String officeId) {

        if (authUser == null || authUser.getSchemaName() == null || authUser.getSchemaName().isEmpty()) {
            return new CommonObjectResponseDTO<JSONObject>(false, 400, "Schema name cannot be null or empty", null);
        }
        if (officeId == null || officeId.isEmpty()) {
            return new CommonObjectResponseDTO<JSONObject>(false, 400, "Office ID cannot be null or empty", null);
        }
        try {
            JSONObject data = managementProcessTrackerRepository.getManagementProcessTrackerByOffice(authUser, officeId);
            return new CommonObjectResponseDTO<JSONObject>(true, 200, "Data retrieved successfully", data);
        } catch (Exception e) {
            return new CommonObjectResponseDTO<JSONObject>(false, 500, "Error retrieving data: " + e.getMessage(), null);
        }
    }
}
