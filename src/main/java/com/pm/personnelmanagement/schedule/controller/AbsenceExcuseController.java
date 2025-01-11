package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AbsenceExcuseController {
    ResponseEntity<AbsenceExcuseDTO> getAbsenceExcuse(UUID uuid);

    ResponseEntity<AbsenceExcuseListDTO> getAbsenceExcuses(
            Integer pageNumber,
            Integer pageSize,
            UUID attendanceUUID
    );

    ResponseEntity<CreateAbsenceExcuseResponse> createAbsenceExcuse(CreateAbsenceExcuseDTO dto);

    ResponseEntity<Void> updateAbsenceExcuse(UUID uuid, UpdateAbsenceExcuseBodyDTO body);

    ResponseEntity<Void> deleteAbsenceExcuse(UUID uuid);
}
