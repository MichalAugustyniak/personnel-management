package com.pm.personnelmanagement.task.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record EditTaskRequest(
        Optional<String> name,
        Optional<String> description,
        Optional<LocalDateTime> startDateTime,
        Optional<LocalDateTime> endDateTime,
        Optional<Integer> color,
        Optional<Long> taskEventId
) {
}
