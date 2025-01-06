package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateScheduleDayDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Set<CreateWorkBreakDTO> workBreaks
) {
}
