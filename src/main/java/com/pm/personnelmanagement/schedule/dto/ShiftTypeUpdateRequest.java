package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ShiftTypeUpdateRequest(
        @NotNull UUID uuid,
        @NotNull ShiftTypeUpdateRequestBody body
        ) {
}
