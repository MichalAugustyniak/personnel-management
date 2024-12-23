package com.pm.personnelmanagement.taskevent.dto;

import java.time.LocalDateTime;

public record TaskEventRequest(
        String name,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}
