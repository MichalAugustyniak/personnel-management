package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AttendanceController {
    ResponseEntity<AttendanceDTO> getAttendance(UUID uuid);
    ResponseEntity<AttendanceListDTO> getAttendances(
            Integer pageNumber,
            Integer pageSize,
            UUID userUUID,
            UUID scheduleDayUUID,
            UUID absenceExcuseUUID,
            UUID attendanceStatusUUID
    );
    ResponseEntity<AttendanceUUIDResponse> createAttendance(CreateAttendanceDTO dto);
    ResponseEntity<Void> updateAttendance(UUID uuid, UpdateAttendanceBodyDTO dto);
    ResponseEntity<Void> deleteAttendance(UUID uuid);
}
