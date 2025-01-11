package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ShiftTypeCreationRequest(
        @NotEmpty String name,
        @NotNull String description,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime
) {
}
