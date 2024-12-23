package com.pm.personnelmanagement.task.dto;

import java.time.LocalDateTime;

public record TaskDTO(
        String name,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Integer color,
        String createdBy,
        long taskEventId
) {
}
