package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAttendanceStatusRequest(
        @NotNull String name,
        @NotEmpty String description
) {
}
