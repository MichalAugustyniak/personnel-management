package com.pm.personnelmanagement.task.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskUpdateRuquestBody(
        String name,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String color,
        UUID taskEventUUID,
        Boolean isCompleted
) {
}
