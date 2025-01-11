package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

import java.util.UUID;

public interface AbsenceExcuseStatusService {
    AbsenceExcuseStatusDTO getAbsenceExcuseStatus(UUID uuid);

    AbsenceExcuseStatusesResponse getAbsenceExcuses(FetchAbsenceExcuseStatusesFiltersDTO filters);

    UUID createAbsenceExcuseStatus(CreateAbsenceStatusDTO dto);

    void updateAbsenceStatus(UpdateAbsenceStatusDTO dto);

    void deleteAbsenceStatus(UUID uuid);
}
