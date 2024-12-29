package com.doer.mraims.loanprocess.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private String userId;
    private String userName;
    private String instituteOid;
    private String schemaName;
}
