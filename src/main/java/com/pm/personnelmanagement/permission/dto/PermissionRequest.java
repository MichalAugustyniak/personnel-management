package com.pm.personnelmanagement.permission.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PermissionRequest(@NotNull UUID uuid) {
}
