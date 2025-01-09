package com.doer.mraims.loanprocess.features.processtracker.office.repository.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficeEventTracker {

    private String managementProcessId;
    private String officeEventTrackerId;
    private String officeId;
    private String officeEvent;
}
