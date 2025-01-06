package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;

public record CreateWorkBreakDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        boolean isPaid
) {
}
