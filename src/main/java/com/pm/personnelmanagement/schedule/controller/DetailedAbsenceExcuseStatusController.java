package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface DetailedAbsenceExcuseStatusController {
    ResponseEntity<DetailedAbsenceExcuseStatusDTO> getDetailedAbsenceExcuseStatus(UUID uuid);

    ResponseEntity<DetailedAbsenceExcuseStatusesDTO> getDetailedAbsenceExcuseStatuses(
            Integer pageNumber,
            Integer pageSize,
            Boolean isChecked,
            UUID absenceExcuseStatusUUID
    );

    ResponseEntity<DetailedAbsenceExcusesStatusCreationResponse> createDetailedAbsenceExcuseStatus(DetailedAbsenceExcuseStatusCreationRequest request);

    ResponseEntity<Void> updateDetailedAbsenceExcuseStatus(
            UUID uuid,
            DetailedAbsenceExcuseStatusUpdateBodyRequest request
    );

    ResponseEntity<Void> deleteDetailedAbsenceExcuseStatus(UUID uuid);
}
