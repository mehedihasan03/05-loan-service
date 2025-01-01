package com.doer.mraims.loanprocess.features.processtracker.management;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.features.processtracker.management.service.ManagementProcessTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
    public ResponseEntity<CommonObjectResponseDTO<JSONObject>> getManagementProcessTracker(
            @RequestParam String officeId,
            @RequestParam String schemaName,
            @RequestHeader("Authorization") String authorizationHeader,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        String userId = authUser.getUserId();
        String userRole = authUser.getUserRole();
        log.info("User ID: {}", userId);
        log.info("User Role: {}", userRole);


        log.info("Request received to get management process tracker by office: officeId={}, schemaName={}", officeId, schemaName);
        log.info("Authorization header: {}", authorizationHeader);
        try {
            CommonObjectResponseDTO<JSONObject> response = managementProcessTrackerService.getManagementProcessTrackerByOffice(authUser, officeId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonObjectResponseDTO<>(false, 400, "Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new CommonObjectResponseDTO<>(false, 500, "Internal server error: " + e.getMessage(), null));
        }
    }
}
