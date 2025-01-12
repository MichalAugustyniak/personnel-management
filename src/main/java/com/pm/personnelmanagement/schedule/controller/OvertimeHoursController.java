package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OvertimeHoursController {
    ResponseEntity<OvertimeHoursDTO> getOvertimeHours(UUID uuid);

    ResponseEntity<OvertimeHoursPagedDTO> getOvertimeHoursPaged(
            Integer pageNumber,
            Integer pageSize,
            UUID approvedByUUID,
            UUID userUUID,
            UUID scheduleDayUUID,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );

    ResponseEntity<OvertimeHoursCreationResponse> createOvertimeHours(OvertimeHoursCreationRequest request);

    ResponseEntity<Void> updateOvertimeHours(UUID uuid, OvertimeHoursUpdateRequestBody body);

    ResponseEntity<Void> deleteOvertimeHours(UUID uuid);
}
