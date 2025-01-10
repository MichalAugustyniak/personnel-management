package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AttendanceDTO(
        UUID uuid,
        UUID userUUID,
        UUID scheduleDayUUID,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        AttendanceStatusDTO attendanceStatus
) {
}
