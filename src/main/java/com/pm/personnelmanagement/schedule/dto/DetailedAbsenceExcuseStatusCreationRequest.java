package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DetailedAbsenceExcuseStatusCreationRequest(
        @NotNull UUID absenceExcuseStatusUUID,
        @NotNull UUID absenceExcuseUUID,
        String message
) {
}
