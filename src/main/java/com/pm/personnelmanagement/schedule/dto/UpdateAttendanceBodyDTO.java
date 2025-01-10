package com.pm.personnelmanagement.schedule.dto;

import com.pm.personnelmanagement.schedule.model.AttendanceStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateAttendanceBodyDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID attendanceStatus
) {
}
