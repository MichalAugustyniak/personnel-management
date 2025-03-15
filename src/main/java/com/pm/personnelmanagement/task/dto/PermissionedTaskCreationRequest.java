package com.pm.personnelmanagement.task.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PermissionedTaskCreationRequest(@NotNull UUID createdBy, @NotEmpty String username, @NotNull TaskCreationRequest task) {
}
