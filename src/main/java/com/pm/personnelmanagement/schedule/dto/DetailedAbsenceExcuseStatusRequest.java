package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DetailedAbsenceExcuseStatusRequest(
        @NotNull UUID uuid
) {
}
