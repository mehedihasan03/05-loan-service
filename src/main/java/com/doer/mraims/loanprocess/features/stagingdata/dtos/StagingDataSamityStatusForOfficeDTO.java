package com.doer.mraims.loanprocess.features.stagingdata.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StagingDataSamityStatusForOfficeDTO {

    private String samityId;
    private String samityNameEn;
    private String samityNameBn;
    private String samityDay;
    private String samityType;

    private String fieldOfficerId;
    private String fieldOfficerNameEn;
    private String fieldOfficerNameBn;

    private Integer totalMember;
    private Integer totalMemberStaged;
    private Integer totalAccount;
    private Integer totalAccountStaged;

    private String status;
    private String remarks;
    private Date processStartTime;
    private Date processEndTime;

    private String isDownloaded;
    private String downloadedBy;
    private String isUploaded;
    private String uploadedBy;

    private String btnInvalidateEnabled;
    private String invalidatedBy;
    private String btnRegenerateEnabled;
    private String regeneratedBy;
}
