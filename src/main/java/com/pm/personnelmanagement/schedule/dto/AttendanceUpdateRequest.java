package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AttendanceUpdateRequest(
        @NotNull UUID uuid,
        @NotNull UpdateAttendanceBodyDTO updateAttendanceBody
) {
}
