package com.doer.mraims.loanprocess.features.processtracker.management;


import com.doer.mraims.loanprocess.features.processtracker.management.response.ManagementResponseDto;
import com.doer.mraims.loanprocess.features.processtracker.management.service.ManagementProcessTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.doer.mraims.loanprocess.core.utils.RouteNames.*;



@RestController
@RequestMapping(BASE_URL + V_1)
@RequiredArgsConstructor
public class ManagementProcessTrackerController {

    private final ManagementProcessTrackerService managementProcessTrackerService;

    @PostMapping(MANAGEMENT_PROCESS + BY_OFFICE)
    public ManagementResponseDto getManagementProcessTracker(@RequestParam String officeId, @RequestParam String schemaName) {
        return managementProcessTrackerService.getManagementProcessTrackerByOffice(officeId, schemaName);
    }
}
