package com.doer.mraims.loanprocess.features.processtracker.management.dtos;

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
public class ManagementResponseDto {
    private String managementProcessId;
    private String officeId;
    private String officeNameEn;
    private String officeNameBn;
    private Date businessDate;
    private String businessDay;
    private String btnCreateTransactionEnabled;
    private String mfiId;
    private List<String> samityIds;
    private String userMessage;
}
