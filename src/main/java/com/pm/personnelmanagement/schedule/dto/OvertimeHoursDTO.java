package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OvertimeHoursDTO(
        UUID uuid,
        UUID userUUID,
        UUID approvedByUUID,
        //UUID scheduleDayUUID,
        UUID scheduleUUID,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        LocalDateTime createdAt,
        String description
) {
}
