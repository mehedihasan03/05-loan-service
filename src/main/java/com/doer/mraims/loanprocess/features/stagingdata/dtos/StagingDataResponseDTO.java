package com.doer.mraims.loanprocess.features.stagingdata.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StagingDataResponseDTO {

    private String btnStagingDataGenerateEnabled;
    private String btnStartProcessEnabled;
    private String btnRefreshEnabled;
    private String btnDeleteEnabled;
    private String mfiId;
    private String officeId;
    private String officeNameEn;
    private String officeNameBn;
    private Date businessDate;
    private String businessDay;
    private String status;
    private String userMessage;
    private List<StagingDataSamityStatusForOfficeDTO> data;
    private Integer totalCount;
}
