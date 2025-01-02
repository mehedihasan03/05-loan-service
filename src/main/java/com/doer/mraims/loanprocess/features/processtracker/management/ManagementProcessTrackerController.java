package com.doer.mraims.loanprocess.features.processtracker.management;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.features.processtracker.management.response.ManagementResponseDto;
import com.doer.mraims.loanprocess.features.processtracker.management.service.ManagementProcessTrackerService;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.doer.mraims.loanprocess.core.utils.RouteNames.BY_OFFICE;
import static com.doer.mraims.loanprocess.core.utils.RouteNames.MANAGEMENT_PROCESS;

@Slf4j
@RestController
@RequestMapping(MANAGEMENT_PROCESS)
public class ManagementProcessTrackerController {

    private final ManagementProcessTrackerService managementProcessTrackerService;

    public ManagementProcessTrackerController(ManagementProcessTrackerService managementProcessTrackerService) {
        this.managementProcessTrackerService = managementProcessTrackerService;
    }

    @PostMapping(BY_OFFICE)
    public ResponseEntity<CommonObjectResponseDTO<ManagementResponseDto>> getManagementProcessTracker(
            @RequestParam String officeId,
            @RequestParam String schemaName,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        try {
            CommonObjectResponseDTO<ManagementResponseDto> response = managementProcessTrackerService.getManagementProcessTrackerByOffice(authUser, officeId);
            log.info("Response: {}", response);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonObjectResponseDTO<>(false, 400, "Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new CommonObjectResponseDTO<>(false, 500, "Internal server error: " + e.getMessage(), null));
        }
    }
}
