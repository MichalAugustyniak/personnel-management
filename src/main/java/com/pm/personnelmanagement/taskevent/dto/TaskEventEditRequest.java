package com.pm.personnelmanagement.taskevent.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record TaskEventEditRequest(
        Optional<String> name,
        Optional<String> description,
        Optional<LocalDateTime> startDateTime,
        Optional<LocalDateTime> endDateTime
) {
}
