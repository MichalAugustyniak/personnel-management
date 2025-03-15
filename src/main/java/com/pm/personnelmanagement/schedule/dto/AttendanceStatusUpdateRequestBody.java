package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;

public record AttendanceStatusUpdateRequestBody(
        @NotEmpty String name,
        @NotEmpty String description
) {
}
