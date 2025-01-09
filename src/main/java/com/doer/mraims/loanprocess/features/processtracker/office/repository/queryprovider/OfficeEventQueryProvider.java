package com.doer.mraims.loanprocess.features.processtracker.office.repository.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;

import java.util.Map;

public interface OfficeEventQueryProvider {

    Map<String, Object> getOfficeEventTrackerQuery(AuthUser authUser, String managementProcessId, String officeId);
}
