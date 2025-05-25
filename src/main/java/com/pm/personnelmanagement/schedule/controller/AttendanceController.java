package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AttendanceController {
    ResponseEntity<AttendanceResponse> getAttendance(UUID uuid);
    ResponseEntity<AttendancesResponse> getAttendances(
            Integer pageNumber,
            Integer pageSize,
            String user,
            UUID scheduleDayUUID,
            UUID attendanceStatusUUID
    );
    ResponseEntity<AttendanceCreationResponse> createAttendance(AttendanceCreationRequest dto);
    ResponseEntity<Void> updateAttendance(UUID uuid, UpdateAttendanceBodyDTO dto);
    ResponseEntity<Void> deleteAttendance(UUID uuid);
}
