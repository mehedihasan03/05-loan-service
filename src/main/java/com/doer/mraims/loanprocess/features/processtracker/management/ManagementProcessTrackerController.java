package com.doer.mraims.loanprocess.features.processtracker.management;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.features.processtracker.management.dtos.ManagementRequestDto;
import com.doer.mraims.loanprocess.features.processtracker.management.dtos.ManagementResponseDto;
import com.doer.mraims.loanprocess.features.processtracker.management.service.ManagementProcessTrackerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.doer.mraims.loanprocess.core.utils.RouteNames.*;

@Slf4j
@RestController
@RequestMapping(MANAGEMENT_PROCESS)
public class ManagementProcessTrackerController {

    private final ManagementProcessTrackerService managementProcessTrackerService;

    public ManagementProcessTrackerController(ManagementProcessTrackerService managementProcessTrackerService) {
        this.managementProcessTrackerService = managementProcessTrackerService;
    }

    @PostMapping(value = V1_BY_OFFICE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonObjectResponseDTO<ManagementResponseDto>> getManagementProcessTracker(
            @Valid @RequestBody ManagementRequestDto request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        CommonObjectResponseDTO<ManagementResponseDto> response =
                managementProcessTrackerService.getManagementProcessTrackerByOffice(request, authUser);
        log.info("Response from getManagementProcessTrackerByOffice: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = V1_NEXT_BUSINESS_DATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonObjectResponseDTO<ManagementResponseDto>> getNextBusinessDate(
            @Valid @RequestBody ManagementRequestDto request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        CommonObjectResponseDTO<ManagementResponseDto> response =
                managementProcessTrackerService.getNextBusinessDate(request, authUser);
        log.info("Response from getNextBusinessDate: {}", response);
        return ResponseEntity.ok().body(response);
    }
}
