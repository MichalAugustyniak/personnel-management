package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkBreakDTO(
        UUID uuid,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID scheduleUUID,
        boolean isPaid
) {
}
