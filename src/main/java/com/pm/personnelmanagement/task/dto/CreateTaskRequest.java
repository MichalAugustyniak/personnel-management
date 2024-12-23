package com.pm.personnelmanagement.task.dto;

import java.time.LocalDateTime;

public record CreateTaskRequest(
        String name,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Integer color,
        long taskEventId
) {
}
