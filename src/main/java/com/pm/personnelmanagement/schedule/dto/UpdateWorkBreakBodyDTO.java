package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;

public record UpdateWorkBreakBodyDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Boolean isPaid
) {
}
