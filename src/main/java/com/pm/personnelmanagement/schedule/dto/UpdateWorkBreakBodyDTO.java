package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateWorkBreakBodyDTO(
        Optional<LocalDateTime> startDateTime,
        Optional<LocalDateTime> endDateTime,
        Optional<Boolean> isPaid
) {
}
