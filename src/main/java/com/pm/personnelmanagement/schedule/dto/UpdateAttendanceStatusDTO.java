package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateAttendanceStatusDTO(
        @NotNull UUID uuid,
        @NotNull AttendanceStatusUpdateRequestBody body
        ) {
}
