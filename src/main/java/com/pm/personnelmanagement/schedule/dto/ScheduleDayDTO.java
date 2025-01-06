package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ScheduleDayDTO(
        UUID uuid,
        UUID scheduleUUID,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Set<WorkBreakDTO> workBreaks
) {
}
