package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public record UpdateScheduleDayBodyDTO(
        Optional<LocalDateTime> startDateTime,
        Optional<LocalDateTime> endDateTime,
        Set<UpdateWorkBreakDTO> workBreaks
) {
}
