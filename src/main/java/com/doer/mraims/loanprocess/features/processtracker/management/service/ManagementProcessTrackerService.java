package com.doer.mraims.loanprocess.features.processtracker.management.service;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import com.doer.mraims.loanprocess.features.processtracker.management.dtos.ManagementRequestDto;
import com.doer.mraims.loanprocess.features.processtracker.management.dtos.ManagementResponseDto;
import com.doer.mraims.loanprocess.features.processtracker.management.repository.ManagementProcessTrackerAdapter;
import com.doer.mraims.loanprocess.utility.repository.CommonAdapter;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Date;

import static com.doer.mraims.loanprocess.core.utils.UtilService.convertToDate;

@Slf4j
@Service
public class ManagementProcessTrackerService {


    private final ManagementProcessTrackerAdapter managementProcessTrackerAdapter;
    private final CommonAdapter commonAdapter;
    private final Gson gson;

    public ManagementProcessTrackerService(ManagementProcessTrackerAdapter managementProcessTrackerAdapter,
                                           CommonAdapter commonAdapter, Gson gson) {
        this.managementProcessTrackerAdapter = managementProcessTrackerAdapter;
        this.commonAdapter = commonAdapter;
        this.gson = gson;
    }

    public CommonObjectResponseDTO<ManagementResponseDto> getManagementProcessTrackerByOffice(ManagementRequestDto request, AuthUser authUser) {
        if (authUser == null || authUser.getSchemaName() == null || authUser.getSchemaName().isEmpty()) {
            return new CommonObjectResponseDTO<>(false, 400, "Schema name cannot be null or empty", null);
        }
        try {
            JSONObject managementProcessTrackerByOffice = commonAdapter.getManagementProcessTrackerByOffice(authUser, request.getOfficeId());
            log.info("Management process tracker by office: {}", managementProcessTrackerByOffice);
            ManagementResponseDto managementResponseDto = gson.fromJson(managementProcessTrackerByOffice.toString(), ManagementResponseDto.class);
            return new CommonObjectResponseDTO<>(true, 200, "Data retrieved successfully", managementResponseDto);
        } catch (Exception e) {
            return new CommonObjectResponseDTO<>(false, 500, "Error retrieving data: " + e.getMessage(), null);
        }
    }

    public CommonObjectResponseDTO<ManagementResponseDto> getNextBusinessDate(ManagementRequestDto request, AuthUser authUser) {
        if (authUser == null || authUser.getSchemaName() == null || authUser.getSchemaName().isEmpty()) {
            return new CommonObjectResponseDTO<>(false, 400, "Schema name cannot be null or empty", null);
        }
        try {
            JSONObject calendarData = commonAdapter.getNextBusinessCalendarData(authUser, request.getOfficeId(), request.getBusinessDate());
            log.info("Fetched calendar data: {}", calendarData);
            Object calendarDateObj = calendarData.get("calendar_date");
            Date businessDate = convertToDate(calendarDateObj);
            log.info("Next business calendar date: {}", businessDate);

            JSONObject holidayData = commonAdapter.getNextHolidayEntry(authUser, request.getOfficeId(), request.getBusinessDate());
            log.info("Fetched holiday data: {}", holidayData);
            Object holidayDateObj = holidayData.get("holiday_date");
            Date holidayDate = convertToDate(holidayDateObj);
            log.info("Next holiday date: {}", holidayDate);

            while (holidayDate.equals(businessDate)) {
                log.info("Business date {} conflicts with holiday date. Fetching next available date.", businessDate);
                calendarData = commonAdapter.getNextBusinessCalendarData(authUser, request.getOfficeId(), String.valueOf(businessDate));
                calendarDateObj = calendarData.get("calendar_date");
                businessDate = convertToDate(calendarDateObj);
                holidayData = commonAdapter.getNextHolidayEntry(authUser, request.getOfficeId(), String.valueOf(businessDate));
                holidayDateObj = holidayData.get("holiday_date");
                holidayDate = convertToDate(holidayDateObj);
            }
            log.info("Resolved next business date: {}", businessDate);

            ManagementResponseDto responseDto = new ManagementResponseDto();
            responseDto.setBusinessDate(businessDate);
            return new CommonObjectResponseDTO<>(true, 200, "Next business date retrieved successfully", responseDto);
        } catch (Exception e) {
            log.error("Error while retrieving next business date for office: {}", request.getOfficeId(), e);
            return new CommonObjectResponseDTO<>(false, 500, "Error retrieving next business date: " + e.getMessage(), null);
        }
    }
}
