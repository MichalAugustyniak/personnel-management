package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DetailedAbsenceExcuseStatusUpdateRequest(
        @NotNull UUID uuid,
        @NotNull DetailedAbsenceExcuseStatusUpdateBodyRequest body
) {
}
