package com.pm.personnelmanagement.permission.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PermissionEditionRequest(
        @NotNull UUID uuid,
        @NotNull PermissionEditionBodyRequest permission
) {
}
