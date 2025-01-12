package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record OvertimeHoursCreationRequest(
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime,
        @NotNull UUID approvedByUUID,
        @NotNull UUID userUUID,
        @NotNull UUID scheduleDayUUID,
        String description
) {
}
