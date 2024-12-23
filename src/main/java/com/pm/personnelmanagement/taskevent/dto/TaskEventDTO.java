package com.pm.personnelmanagement.taskevent.dto;

import java.time.LocalDateTime;

public record TaskEventDTO(
        long id,
        String name,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}
