package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AttendanceResponse(
        UUID uuid,
        String user,
        UUID scheduleDayUUID,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        AttendanceStatusResponse attendanceStatus
) {
}
