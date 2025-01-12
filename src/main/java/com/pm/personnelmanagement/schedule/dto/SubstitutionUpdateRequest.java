package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubstitutionUpdateRequest(
        @NotNull UUID uuid,
        @NotNull SubstitutionUpdateRequestBody body
) {
}
