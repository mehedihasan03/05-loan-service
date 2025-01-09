package com.doer.mraims.loanprocess.features.stagingdata.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StagingDataRequestDto {

    @NotBlank(message = "Office ID cannot be null or empty.")
    private String officeId;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Business date must be in YYYY-MM-DD format.")
    private String businessDate;
    private String managementProcessId;
    private String instituteOid;
    private String mfiId;
    private String loginId;
    private String samityId;
    private String fieldOfficerId;
    private String memberId;
    private String accountId;
    private String employeeId;
    private String userRole;
    private List<String> samityIdList;
    private String remarks;
    private Boolean isScheduledRequest;
}
