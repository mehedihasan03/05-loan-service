package com.doer.mraims.loanprocess.features.processtracker.management.repository.queryManagement;

import com.doer.mraims.loanprocess.auth.model.AuthUser;

import java.util.Map;

public interface ManagementProcessTrackerQueryProvider {
    Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId);
}
