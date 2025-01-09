package com.doer.mraims.loanprocess.features.processtracker.samity.repository.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SamityEventTracker {

    private String oid;
    private String managementProcessId;
    private String samityEventTrackerId;
    private String officeId;
    private String samityId;
    private String samityEvent;
    private String remarks;
    private Date createdOn;
    private String createdBy;
}
