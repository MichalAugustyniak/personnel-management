package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OvertimeHoursUpdateRequestBody(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID userUUID,
        //UUID scheduleDayUUID,
        UUID scheduleUUID,
        String description,
        Boolean isCompleted
) {
}
