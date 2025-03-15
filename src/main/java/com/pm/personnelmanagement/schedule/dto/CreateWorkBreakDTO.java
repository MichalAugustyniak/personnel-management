package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateWorkBreakDTO(
        @NotNull
        LocalDateTime startDateTime,
        @NotNull
        LocalDateTime endDateTime,
        boolean isPaid
) {
}
