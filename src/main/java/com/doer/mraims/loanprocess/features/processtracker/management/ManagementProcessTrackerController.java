package com.doer.mraims.loanprocess.features.processtracker.management;


import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.auth.model.UserInfo;
import com.doer.mraims.loanprocess.auth.service.TokenValidationService;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.features.processtracker.management.service.ManagementProcessTrackerService;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotBlank;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.doer.mraims.loanprocess.core.utils.RouteNames.*;



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
            @RequestParam @NotBlank(message = "Office ID must not be blank") String officeId,
            @RequestParam @NotBlank(message = "Schema name must not be blank") String schemaName, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            JSONObject validateAndFetchUser = tokenValidationService.validateAndFetchUser(authorizationHeader);
            AuthUser authUser = modelMapper.map(validateAndFetchUser, AuthUser.class);
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
