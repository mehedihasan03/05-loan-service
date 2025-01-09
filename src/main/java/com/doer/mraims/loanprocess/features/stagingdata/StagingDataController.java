package com.doer.mraims.loanprocess.features.stagingdata;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.exceptions.CommonObjectResponseDTOV2;
import com.doer.mraims.loanprocess.core.exceptions.CustomException;
import com.doer.mraims.loanprocess.features.stagingdata.dtos.StagingDataRequestDto;
import com.doer.mraims.loanprocess.features.stagingdata.dtos.StagingDataResponseDTO;
import com.doer.mraims.loanprocess.features.stagingdata.service.StagingDataService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.doer.mraims.loanprocess.core.utils.RouteNames.STAGING_DATA;
import static com.doer.mraims.loanprocess.core.utils.RouteNames.V1_BY_OFFICE;

@Slf4j
@RestController
@RequestMapping(STAGING_DATA)
public class StagingDataController {

    private final StagingDataService stagingDataService;

    public StagingDataController(StagingDataService stagingDataService) {
        this.stagingDataService = stagingDataService;
    }

    @PostMapping(value = V1_BY_OFFICE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonObjectResponseDTOV2<StagingDataResponseDTO>> stagingDataGridView(
            @Valid @RequestBody StagingDataRequestDto request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        try {
            return ResponseEntity.ok(new CommonObjectResponseDTOV2<>(
                    true, HttpStatus.OK.value(),
                    "Successfully retrieved staging data grid view.",
                    stagingDataService.getStagingDataGridView(request, authUser))
            );
/*            response.setStatus(true);
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setData(stagingDataGridView);
            log.info("Response from stagingDataGridView: {}", response);
            return ResponseEntity.ok(response);*/
        } catch (CustomException ex) {
            log.error("Custom error: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error: {}", ex.getMessage());
            throw new CustomException("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
