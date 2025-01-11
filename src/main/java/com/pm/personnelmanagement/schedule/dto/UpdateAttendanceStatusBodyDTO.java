package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;

public record UpdateAttendanceStatusBodyDTO(
        @NotEmpty String name,
        @NotEmpty String description
) {
}
