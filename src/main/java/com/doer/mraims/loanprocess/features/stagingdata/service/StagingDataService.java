package com.doer.mraims.loanprocess.features.stagingdata.service;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.exceptions.CustomException;
import com.doer.mraims.loanprocess.core.utils.Constants;
import com.doer.mraims.loanprocess.core.utils.OfficeEvents;
import com.doer.mraims.loanprocess.core.utils.Status;
import com.doer.mraims.loanprocess.features.processtracker.management.dtos.ManagementResponseDto;
import com.doer.mraims.loanprocess.features.processtracker.office.repository.OfficeEventTrackerAdapter;
import com.doer.mraims.loanprocess.features.processtracker.office.repository.dtos.OfficeEventTracker;
import com.doer.mraims.loanprocess.features.processtracker.samity.repository.SamityEventTrackerAdapter;
import com.doer.mraims.loanprocess.features.processtracker.samity.repository.dtos.SamityEventTracker;
import com.doer.mraims.loanprocess.features.stagingdata.dtos.StagingDataRequestDto;
import com.doer.mraims.loanprocess.features.stagingdata.dtos.StagingDataResponseDTO;
import com.doer.mraims.loanprocess.features.stagingdata.dtos.StagingDataSamityStatusForOfficeDTO;
import com.doer.mraims.loanprocess.features.stagingdata.repository.StagingDataAdapter;
import com.doer.mraims.loanprocess.utility.repository.CommonAdapter;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.doer.mraims.loanprocess.core.utils.UtilService.checkNullOrEmpty;
import static com.doer.mraims.loanprocess.core.utils.UtilService.convertJSONArrayToList;

@Slf4j
@Service
public class StagingDataService {

    private final StagingDataAdapter stagingDataAdapter;
    private final SamityEventTrackerAdapter samityEventTrackerAdapter;
    private final OfficeEventTrackerAdapter officeEventTrackerAdapter;
    private final CommonAdapter commonAdapter;
    private final Gson gson;

    public StagingDataService(StagingDataAdapter stagingDataAdapter,
                              SamityEventTrackerAdapter samityEventTrackerAdapter,
                              OfficeEventTrackerAdapter officeEventTrackerAdapter,
                              CommonAdapter commonAdapter, Gson gson) {
        this.stagingDataAdapter = stagingDataAdapter;
        this.samityEventTrackerAdapter = samityEventTrackerAdapter;
        this.officeEventTrackerAdapter = officeEventTrackerAdapter;
        this.commonAdapter = commonAdapter;
        this.gson = gson;
    }

/*    public StagingDataResponseDTO getStagingDataGridView(StagingDataRequestDto request, AuthUser authUser) {
        ManagementResponseDto managementResponseDto;
        List<StagingDataSamityStatusForOfficeDTO> stagingDataProcessTrackers;
        JSONObject managementProcessTrackerByOffice = commonAdapter.getManagementProcessTrackerByOffice(authUser, request.getOfficeId());
        log.info("managementProcessTrackerByOffice: {}", managementProcessTrackerByOffice);
        if (managementProcessTrackerByOffice != null && !managementProcessTrackerByOffice.isEmpty()) {
            managementResponseDto = gson.fromJson(managementProcessTrackerByOffice.toString(), ManagementResponseDto.class);
        } else {
            throw new RuntimeException("Error retrieving management process tracker for officeId: " + request.getOfficeId());
        }

        JSONArray stagingDataProcessTracker = commonAdapter.getStagingDataProcessTracker(authUser, managementResponseDto.getManagementProcessId(), request.getOfficeId());
        log.info("stagingDataProcessTracker: {}", stagingDataProcessTracker);
        if (stagingDataProcessTracker != null && !stagingDataProcessTracker.isEmpty()) {
            stagingDataProcessTrackers = convertJSONArrayToList(stagingDataProcessTracker, StagingDataSamityStatusForOfficeDTO.class);
            stagingDataProcessTrackers.forEach(trackerData -> {
                if (trackerData.getSamityDay().equals(managementResponseDto.getBusinessDay())) {
                    trackerData.setSamityType("Regular");
                } else {
                    trackerData.setSamityType("Special");
                }
                log.info("trackerData: {}", trackerData);
            });
            log.info("stagingDataProcessTrackers: {}", stagingDataProcessTrackers);
        } else {
            throw new RuntimeException("Error retrieving staging data process tracker for officeId: " + request.getOfficeId());
        }
        JSONArray samityEventTrackers = samityEventTrackerAdapter.getSamityEventTrackers(authUser, managementResponseDto.getManagementProcessId(), request.getOfficeId());
        List<SamityEventTracker> samityEventTrackerList = convertJSONArrayToList(samityEventTrackers, SamityEventTracker.class);

        List<SamityEventTracker> filteredSamityList = samityEventTrackerList.stream()
                .filter(tracker -> !checkNullOrEmpty(tracker.getSamityEvent()))
                .toList();
        if (filteredSamityList.isEmpty()) {
            stagingDataProcessTrackers.forEach(trackerData -> {
                trackerData.setBtnInvalidateEnabled("No");
                trackerData.setBtnRegenerateEnabled("No");
            });
        } else {
            stagingDataProcessTrackers = stagingDataProcessTrackers.stream()
                    .map(this::setBtnStatusForSamityStagingDataGeneration)
                    .toList();
        }
        StagingDataResponseDTO stagingDataResponseDTO = generateResponse(managementResponseDto, stagingDataProcessTrackers);
        JSONArray officeEventTrackers = officeEventTrackerAdapter.getOfficeEventTrackers(authUser, managementResponseDto.getManagementProcessId(), request.getOfficeId());
        List<OfficeEventTracker> officeEventTrackerList = convertJSONArrayToList(officeEventTrackers, OfficeEventTracker.class);
        officeEventTrackerList.stream()
                .map(OfficeEventTracker::getOfficeEvent)
                .forEach(officeEvent -> {
                    if (officeEvent.equals(OfficeEvents.STAGING_DATA_GENERATION_COMPLETED.getValue())) {
                        stagingDataResponseDTO.setBtnStagingDataGenerateEnabled("No");
                        stagingDataResponseDTO.setBtnStartProcessEnabled("No");
                        stagingDataResponseDTO.setStatus(Status.STATUS_FINISHED.getValue());
                        stagingDataResponseDTO.setUserMessage("Staging Data Generation is Completed For Office");
                    } else {
                        if (stagingDataResponseDTO.getData().isEmpty()) {
                            stagingDataResponseDTO.setBtnStagingDataGenerateEnabled("Yes");
                            stagingDataResponseDTO.setBtnStartProcessEnabled("Yes");
                            stagingDataResponseDTO.setStatus(Status.STATUS_PENDING.getValue());
                            stagingDataResponseDTO.setUserMessage("Staging Data is Not Generated For Office");
                        } else if (stagingDataResponseDTO.getData().stream().map(StagingDataSamityStatusForOfficeDTO::getStatus).anyMatch(samityStatus ->
                                samityStatus.equals(Status.STATUS_WAITING.getValue()) || samityStatus.equals(Status.STATUS_PROCESSING.getValue()))) {
                            stagingDataResponseDTO.setBtnStagingDataGenerateEnabled("No");
                            stagingDataResponseDTO.setBtnStartProcessEnabled("No");
                            stagingDataResponseDTO.setStatus(Status.STATUS_PROCESSING.getValue());
                            stagingDataResponseDTO.setUserMessage("Staging Data Generation Process Is Running For Office");
                        } else {
                            stagingDataResponseDTO.setBtnStagingDataGenerateEnabled("No");
                            stagingDataResponseDTO.setBtnStartProcessEnabled("No");
                            stagingDataResponseDTO.setStatus(Status.STATUS_FINISHED.getValue());
                            stagingDataResponseDTO.setUserMessage("Staging Data Is Generated For Office");
                        }
                    }
                    if (stagingDataResponseDTO.getBtnStartProcessEnabled().equals("Yes")
                            || stagingDataResponseDTO.getBtnStagingDataGenerateEnabled().equals("Yes")) {
                        stagingDataResponseDTO.setBtnRefreshEnabled("No");
                    } else {
                        stagingDataResponseDTO.setBtnRefreshEnabled("Yes");
                    }
                });
        if (!stagingDataResponseDTO.getStatus().equals(Status.STATUS_FINISHED.getValue())) {
            stagingDataResponseDTO.setBtnDeleteEnabled("No");
        }
        if (!filteredSamityList.isEmpty()) {
            stagingDataResponseDTO.setBtnDeleteEnabled("No");
        } else if (stagingDataResponseDTO.getData().stream()
                .map(StagingDataSamityStatusForOfficeDTO::getIsDownloaded)
                .anyMatch(isDownloaded -> isDownloaded.equals("Yes"))) {
            stagingDataResponseDTO.setBtnDeleteEnabled("No");
        } else {
            if (stagingDataResponseDTO.getBtnStartProcessEnabled().equals("Yes")) {
                stagingDataResponseDTO.setBtnDeleteEnabled("No");
            } else {
                stagingDataResponseDTO.setBtnDeleteEnabled("Yes");
            }
        }
        return stagingDataResponseDTO;
    }*/


    public StagingDataResponseDTO getStagingDataGridView(StagingDataRequestDto request, AuthUser authUser) {
        ManagementResponseDto managementResponseDto = getManagementProcessTracker(authUser, request.getOfficeId());
        List<StagingDataSamityStatusForOfficeDTO> stagingDataProcessTrackers = getStagingDataProcessTrackers(authUser, managementResponseDto);
        List<SamityEventTracker> samityEventTrackerList = getSamityEventTrackers(authUser, managementResponseDto);
        List<SamityEventTracker> filteredSamityList = samityEventTrackerList.stream()
                .filter(tracker -> !checkNullOrEmpty(tracker.getSamityEvent()))
                .toList();
        if (filteredSamityList.isEmpty()) {
            stagingDataProcessTrackers.forEach(trackerData -> {
                trackerData.setBtnInvalidateEnabled("No");
                trackerData.setBtnRegenerateEnabled("No");
            });
        } else {
            stagingDataProcessTrackers = stagingDataProcessTrackers.stream()
                    .map(this::setBtnStatusForSamityStagingDataGeneration)
                    .toList();
        }
        StagingDataResponseDTO stagingDataResponseDTO = generateResponse(managementResponseDto, stagingDataProcessTrackers);
        updateResponseButtons(authUser, managementResponseDto, stagingDataResponseDTO, filteredSamityList);
        return stagingDataResponseDTO;
    }

    private ManagementResponseDto getManagementProcessTracker(AuthUser authUser, String officeId) {
        try {
            JSONObject managementProcessTrackerByOffice = commonAdapter.getManagementProcessTrackerByOffice(authUser, officeId);
            if (managementProcessTrackerByOffice != null && !managementProcessTrackerByOffice.isEmpty()) {
                return gson.fromJson(managementProcessTrackerByOffice.toString(), ManagementResponseDto.class);
            } else {
                throw new RuntimeException("Error retrieving management process tracker for officeId: " + officeId);
            }
        } catch (RuntimeException e) {
            throw new CustomException("Error retrieving management process tracker for officeId: " + officeId, HttpStatus.NOT_FOUND.value());
        }
    }

    private List<StagingDataSamityStatusForOfficeDTO> getStagingDataProcessTrackers(AuthUser authUser, ManagementResponseDto managementResponseDto) {
        JSONArray stagingDataProcessTracker = null;
        try {
            stagingDataProcessTracker = commonAdapter.getStagingDataProcessTracker(authUser, managementResponseDto.getManagementProcessId(), managementResponseDto.getOfficeId());
        } catch (Exception e) {
            throw new CustomException("Error retrieving staging data process tracker data: " + e.getMessage(), HttpStatus.NOT_FOUND.value());
        }
        if (stagingDataProcessTracker != null && !stagingDataProcessTracker.isEmpty()) {
            List<StagingDataSamityStatusForOfficeDTO> trackers = convertJSONArrayToList(stagingDataProcessTracker, StagingDataSamityStatusForOfficeDTO.class);
            trackers.forEach(trackerData -> {
                if (trackerData.getSamityDay().equals(managementResponseDto.getBusinessDay())) {
                    trackerData.setSamityType("Regular");
                } else {
                    trackerData.setSamityType("Special");
                }
            });
            return trackers;
        } else {
            throw new CustomException("Error retrieving staging data process tracker for officeId: " + managementResponseDto.getOfficeId(), HttpStatus.NOT_FOUND.value());
        }
    }

    private List<SamityEventTracker> getSamityEventTrackers(AuthUser authUser, ManagementResponseDto managementResponseDto) {
        try {
            JSONArray samityEventTrackers = samityEventTrackerAdapter.getSamityEventTrackers(authUser, managementResponseDto.getManagementProcessId(), managementResponseDto.getOfficeId());
            return convertJSONArrayToList(samityEventTrackers, SamityEventTracker.class);
        } catch (Exception e) {
            throw new CustomException("Error retrieving samity event tracker data: " + e.getMessage(), HttpStatus.NOT_FOUND.value());
        }
    }

    private void updateResponseButtons(AuthUser authUser, ManagementResponseDto managementResponseDto, StagingDataResponseDTO stagingDataResponseDTO, List<SamityEventTracker> filteredSamityList) {
        List<OfficeEventTracker> officeEventTrackerList = null;
        try {
            JSONArray officeEventTrackers = officeEventTrackerAdapter.getOfficeEventTrackers(authUser, managementResponseDto.getManagementProcessId(), managementResponseDto.getOfficeId());
            officeEventTrackerList = convertJSONArrayToList(officeEventTrackers, OfficeEventTracker.class);
        } catch (Exception e) {
            throw new CustomException("Error retrieving Office event tracker data: " + e.getMessage(), HttpStatus.NOT_FOUND.value());
        }
        officeEventTrackerList.stream()
                .map(OfficeEventTracker::getOfficeEvent)
                .forEach(officeEvent -> {
                    if (officeEvent.equals(OfficeEvents.STAGING_DATA_GENERATION_COMPLETED.getValue())) {
                        stagingDataResponseDTO.setBtnStagingDataGenerateEnabled("No");
                        stagingDataResponseDTO.setBtnStartProcessEnabled("No");
                        stagingDataResponseDTO.setStatus(Status.STATUS_FINISHED.getValue());
                        stagingDataResponseDTO.setUserMessage("Staging Data Generation is Completed For Office");
                    } else {
                        updateButtonsBasedOnDataStatus(stagingDataResponseDTO);
                    }

                    stagingDataResponseDTO.setBtnRefreshEnabled(stagingDataResponseDTO.getBtnStartProcessEnabled().equals("Yes")
                            || stagingDataResponseDTO.getBtnStagingDataGenerateEnabled().equals("Yes") ? "No" : "Yes");
                });
        updateDeleteButton(stagingDataResponseDTO, filteredSamityList);
    }

    private void updateButtonsBasedOnDataStatus(StagingDataResponseDTO stagingDataResponseDTO) {
        if (stagingDataResponseDTO.getData().isEmpty()) {
            stagingDataResponseDTO.setBtnStagingDataGenerateEnabled("Yes");
            stagingDataResponseDTO.setBtnStartProcessEnabled("Yes");
            stagingDataResponseDTO.setStatus(Status.STATUS_PENDING.getValue());
            stagingDataResponseDTO.setUserMessage("Staging Data is Not Generated For Office");
        } else if (stagingDataResponseDTO.getData().stream().map(StagingDataSamityStatusForOfficeDTO::getStatus).anyMatch(samityStatus ->
                samityStatus.equals(Status.STATUS_WAITING.getValue()) || samityStatus.equals(Status.STATUS_PROCESSING.getValue()))) {
            stagingDataResponseDTO.setBtnStagingDataGenerateEnabled("No");
            stagingDataResponseDTO.setBtnStartProcessEnabled("No");
            stagingDataResponseDTO.setStatus(Status.STATUS_PROCESSING.getValue());
            stagingDataResponseDTO.setUserMessage("Staging Data Generation Process Is Running For Office");
        } else {
            stagingDataResponseDTO.setBtnStagingDataGenerateEnabled("No");
            stagingDataResponseDTO.setBtnStartProcessEnabled("No");
            stagingDataResponseDTO.setStatus(Status.STATUS_FINISHED.getValue());
            stagingDataResponseDTO.setUserMessage("Staging Data Is Generated For Office");
        }
    }

    private void updateDeleteButton(StagingDataResponseDTO stagingDataResponseDTO, List<SamityEventTracker> filteredSamityList) {
        if (!stagingDataResponseDTO.getStatus().equals(Status.STATUS_FINISHED.getValue())
                || !filteredSamityList.isEmpty()
                || stagingDataResponseDTO.getData().stream().map(StagingDataSamityStatusForOfficeDTO::getIsDownloaded).anyMatch(isDownloaded -> isDownloaded.equals("Yes"))
                || stagingDataResponseDTO.getBtnStartProcessEnabled().equals("Yes")) {
            stagingDataResponseDTO.setBtnDeleteEnabled("No");
        } else {
            stagingDataResponseDTO.setBtnDeleteEnabled("Yes");
        }
    }

    private StagingDataResponseDTO generateResponse(ManagementResponseDto managementProcessTracker, List<StagingDataSamityStatusForOfficeDTO> stagingDataProcessTrackers) {
        return StagingDataResponseDTO.builder()
                .mfiId(managementProcessTracker.getMfiId())
                .officeId(managementProcessTracker.getOfficeId())
                .officeNameEn(managementProcessTracker.getOfficeNameEn())
                .officeNameBn(managementProcessTracker.getOfficeNameBn())
                .businessDate(managementProcessTracker.getBusinessDate())
                .businessDay(managementProcessTracker.getBusinessDay())
                .data(stagingDataProcessTrackers)
                .totalCount(stagingDataProcessTrackers.size())
                .build();
    }

    private StagingDataSamityStatusForOfficeDTO setBtnStatusForSamityStagingDataGeneration(StagingDataSamityStatusForOfficeDTO samityStatus) {
        if (samityStatus.getIsDownloaded().equals("Yes")) {
            samityStatus.setBtnInvalidateEnabled("No");
            samityStatus.setBtnRegenerateEnabled("No");
        } else {
            if (samityStatus.getStatus().equals(Constants.STATUS_FINISHED) || samityStatus.getStatus().equals(Constants.STATUS_REGENERATED)) {
                samityStatus.setBtnInvalidateEnabled("Yes");
                samityStatus.setBtnRegenerateEnabled("No");
            } else if (samityStatus.getStatus().equals(Constants.STATUS_FAILED) || samityStatus.getStatus().equals(Constants.STATUS_INVALIDATED)) {
                samityStatus.setBtnInvalidateEnabled("No");
                samityStatus.setBtnRegenerateEnabled("Yes");
            } else {
                samityStatus.setBtnInvalidateEnabled("No");
                samityStatus.setBtnRegenerateEnabled("No");
            }
        }
        return samityStatus;
    }
}
