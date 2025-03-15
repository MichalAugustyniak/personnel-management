package com.pm.personnelmanagement.task.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDTO(
        String uuid,
        String name,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String color,
        String createdBy,
        UUID taskEventUUID,
        boolean isCompleted
) {
}
