package com.doer.mraims.loanprocess.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser {
    private String userId;
    private String userName;
    private String userType;
    private String userRole;
    private String mfiId;
    private String instituteOid;
    private String schemaName;
    private String officeId;
}
