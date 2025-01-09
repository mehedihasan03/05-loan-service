package com.doer.mraims.loanprocess.features.processtracker.samity.repository.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;

import java.util.Map;

public interface SamityEventQueryProvider {

    Map<String, Object> getSamityEventTrackerQuery(AuthUser authUser, String managementProcessId, String officeId);
}
