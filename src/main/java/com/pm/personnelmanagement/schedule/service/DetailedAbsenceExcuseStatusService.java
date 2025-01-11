package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

public interface DetailedAbsenceExcuseStatusService {
    DetailedAbsenceExcuseStatusDTO getDetailedAbsenceExcuseStatus(DetailedAbsenceExcuseStatusRequest dto);

    DetailedAbsenceExcuseStatusesDTO getDetailedAbsenceExcuseStatuses(DetailedAbsenceStatusesFiltersDTO dto);

    DetailedAbsenceExcusesStatusCreationResponse createDetailedAbsenceExcuseStatus(DetailedAbsenceExcuseStatusCreationRequest dto);

    void updateDetailedAbsenceExcuseStatus(DetailedAbsenceExcuseStatusUpdateRequest dto);

    void deleteDetailedAbsenceExcuseStatus(DetailedAbsenceExcuseStatusDeleteRequest dto);
}
