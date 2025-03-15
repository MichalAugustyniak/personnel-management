package com.pm.personnelmanagement.taskevent.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskEventCreationRequest(
        @NotEmpty
        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
        String name,
        @Size(max = 500, message = "Max length is 500")
        String description,
        @NotNull
        LocalDateTime startDateTime,
        @NotNull
        LocalDateTime endDateTime
) {
}
