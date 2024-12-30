package com.doer.mraims.loanprocess.features.processtracker.management.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private List<String> samityIdList;
    private String userMessage;
}
