package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CreateAbsenceExcuseDTO(
        @NotEmpty String name,
        @NotEmpty String description,
        @NotNull UUID fileUUID,
        @NotNull Set<@NotNull UUID> attendanceUUIDs
) {
}
