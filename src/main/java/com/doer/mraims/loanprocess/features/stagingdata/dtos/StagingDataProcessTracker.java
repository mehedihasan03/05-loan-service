package com.doer.mraims.loanprocess.features.stagingdata.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StagingDataProcessTracker {

    private String oid;
    private String managementProcessId;
    private String processId;
    private String officeId;
    private String samityId;
    private String samityNameEn;
    private String samityNameBn;
    private String fieldOfficerId;
    private String fieldOfficerNameEn;
    private String fieldOfficerNameBn;
    private String samityDay;
    private Integer totalMember;
    private Integer totalMemberStaged;
    private Integer totalAccount;
    private Integer totalAccountStaged;
    private String stagingDataIds;
    private String isDownloaded;
    private LocalDateTime downloadedOn;
    private String downloadedBy;
    private String isUploaded;
    private LocalDateTime uploadedOn;
    private String uploadedBy;
    private String status;
    private String remarks;
    private Integer currentVersion;
    private String exceptionMarkedBy;
    private String invalidatedBy;
    private LocalDateTime invalidatedOn;
    private String regeneratedBy;
    private LocalDateTime regeneratedOn;
    private LocalDateTime processStartTime;
    private LocalDateTime processEndTime;
    private String createdBy;
    private LocalDateTime createdOn;
}
