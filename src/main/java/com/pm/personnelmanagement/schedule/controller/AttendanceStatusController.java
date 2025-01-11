package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AttendanceStatusController {
    ResponseEntity<AttendanceStatusDTO> getAttendanceStatus(UUID uuid);

    ResponseEntity<AttendanceStatusesDTO> getAttendanceStatuses(Integer pageNumber, Integer pageSize);

    ResponseEntity<CreateAttendanceStatusResponse> createAttendanceStatus(CreateAttendanceStatusDTO dto);

    ResponseEntity<Void> updateAttendanceStatus(UUID uuid, UpdateAttendanceStatusBodyDTO dto);

    ResponseEntity<Void> deleteAttendanceStatus(UUID uuid);
}
