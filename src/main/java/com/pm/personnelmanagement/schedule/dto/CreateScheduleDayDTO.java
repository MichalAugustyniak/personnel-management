package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record CreateScheduleDayDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID shiftTypeUUID,
        Set<CreateWorkBreakDTO> workBreaks
) {
}
