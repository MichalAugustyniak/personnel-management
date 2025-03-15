package com.pm.personnelmanagement.task.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCreationRequest(
        @Length(min = 3, max = 20) @NotEmpty String name,
        String description,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime,
        @NotNull String color,
        UUID taskEventUUID,
        boolean isCompleted
        ) {
}
