package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAttendanceDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID userUUID,
        UUID attendanceStatusUUID,
        UUID scheduleUUID
) {
}
