package com.doer.mraims.loanprocess.features.processtracker.management;


import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.auth.service.TokenValidationService;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.features.processtracker.management.service.ManagementProcessTrackerService;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.doer.mraims.loanprocess.core.utils.RouteNames.*;


@Slf4j
@RestController
@RequestMapping(BASE_URL + V_1)
public class ManagementProcessTrackerController {
@Autowired
private TokenValidationService tokenValidationService;
@Autowired
private ModelMapper modelMapper;
private ManagementProcessTrackerService managementProcessTrackerService;

    public ManagementProcessTrackerController(ManagementProcessTrackerService managementProcessTrackerService) {
        this.managementProcessTrackerService = managementProcessTrackerService;
    }

    @PostMapping(MANAGEMENT_PROCESS + BY_OFFICE)
    public ResponseEntity<CommonObjectResponseDTO<JSONObject>> getManagementProcessTracker(
            @RequestParam String officeId,
            @RequestParam String schemaName,
            @RequestHeader("Authorization") String authorizationHeader) {

        log.info("Request received to get management process tracker by office: officeId={}, schemaName={}", officeId, schemaName);
        log.info("Authorization header: {}", authorizationHeader);
        try {
            AuthUser authUser = tokenValidationService.validateAndFetchUser(authorizationHeader);
            authUser.setOfficeId(officeId);

            CommonObjectResponseDTO<JSONObject> response =
                    managementProcessTrackerService.getManagementProcessTrackerByOffice(authUser, officeId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonObjectResponseDTO<>(false, 400, "Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new CommonObjectResponseDTO<>(false, 500, "Internal server error: " + e.getMessage(), null));
        }
    }
}
