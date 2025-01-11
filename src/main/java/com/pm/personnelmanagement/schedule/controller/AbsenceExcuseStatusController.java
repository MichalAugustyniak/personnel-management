package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AbsenceExcuseStatusController {
    ResponseEntity<AbsenceExcuseStatusDTO> getAbsenceExcuseStatus(UUID uuid);

    ResponseEntity<AbsenceExcuseStatusesResponse> getAbsenceExcuses(Integer pageNumber, Integer pageSize);

    ResponseEntity<CreateAbsenceExcuseStatusResponse> createAbsenceExcuseStatus(CreateAbsenceStatusDTO dto);

    ResponseEntity<Void> updateAbsenceStatus(UUID uuid, UpdateAbsenceExcuseStatusBodyDTO body);

    ResponseEntity<Void> deleteAbsenceStatus(UUID uuid);
}
