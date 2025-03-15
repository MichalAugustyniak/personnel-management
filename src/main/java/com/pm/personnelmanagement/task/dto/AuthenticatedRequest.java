package com.pm.personnelmanagement.task.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenticatedRequest<T>(@NotEmpty String principalName, @NotNull T request) {
}
