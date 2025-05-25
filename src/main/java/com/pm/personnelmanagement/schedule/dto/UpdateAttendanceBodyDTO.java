package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateAttendanceBodyDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID attendanceStatus
) {
}
