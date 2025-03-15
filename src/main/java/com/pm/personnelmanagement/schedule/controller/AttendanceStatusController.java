package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AttendanceStatusController {
    ResponseEntity<AttendanceStatusResponse> getAttendanceStatus(UUID uuid);

    ResponseEntity<AttendanceStatusesResponse> getAttendanceStatuses(Integer pageNumber, Integer pageSize);

    ResponseEntity<AttendanceStatusCreationResponse> createAttendanceStatus(AttendanceStatusCreationRequest dto);

    ResponseEntity<Void> updateAttendanceStatus(UUID uuid, AttendanceStatusUpdateRequestBody body);

    ResponseEntity<Void> deleteAttendanceStatus(UUID uuid);
}
