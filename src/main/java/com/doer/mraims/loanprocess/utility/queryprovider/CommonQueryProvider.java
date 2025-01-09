package com.doer.mraims.loanprocess.utility.queryprovider;

import com.doer.mraims.loanprocess.auth.model.AuthUser;

import java.util.Map;

public interface CommonQueryProvider {

    Map<String, Object> getManagementProcessTrackerQuery(AuthUser authUser, String officeId);

    Map<String, Object> getNextBusinessCalenderData(AuthUser authUser, String officeId, String currentBusinessDate);

    Map<String, Object> getNextHolidayData(AuthUser authUser, String officeId, String currentBusinessDate);

    Map<String, Object> getStagingProcessTrackerList(AuthUser authUser, String managementProcessId, String officeId);
}
