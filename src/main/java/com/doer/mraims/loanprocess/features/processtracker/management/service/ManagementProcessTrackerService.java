package com.doer.mraims.loanprocess.features.processtracker.management.service;

import com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement.ManagementProcessTrackerRepository;
import com.doer.mraims.loanprocess.features.processtracker.management.response.ManagementResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagementProcessTrackerService {

    private final ManagementProcessTrackerRepository managementProcessTrackerRepository;


    public ManagementResponseDto getManagementProcessTrackerByOffice(String officeId, String schemaName) {
        return managementProcessTrackerRepository.getManagementProcessTrackerByOffice(officeId, schemaName);
    }
}
