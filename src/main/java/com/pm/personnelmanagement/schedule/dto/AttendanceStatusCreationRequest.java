package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AttendanceStatusCreationRequest(
        @NotEmpty String name,
        @NotNull String description,
        boolean isExcusable
) {
}
