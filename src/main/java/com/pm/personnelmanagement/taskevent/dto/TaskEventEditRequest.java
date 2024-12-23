package com.pm.personnelmanagement.taskevent.dto;

import java.time.LocalDateTime;

public record TaskEventEditRequest(
        String name,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}
