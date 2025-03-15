package com.pm.personnelmanagement.user.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserEditionRequest(
        @NotNull UUID uuid,
        @NotNull UserEditionRequestBody request
) {
}
