package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AttendanceCreationRequest(
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime,
        @NotNull String user,
        @NotNull UUID attendanceStatusUUID,
        @NotNull UUID scheduleDayUUID
) {
}
