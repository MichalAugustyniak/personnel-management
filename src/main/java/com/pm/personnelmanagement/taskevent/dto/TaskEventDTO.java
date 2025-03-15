package com.pm.personnelmanagement.taskevent.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskEventDTO(
        UUID uuid,
        String name,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String createdBy,
        LocalDateTime createdAt
) {
}
