package com.doer.mraims.loanprocess.features.processtracker.management.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagementRequestDto {

    private String officeId;
    private String businessDate;
    private String managementProcessId;

}
