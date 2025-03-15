package com.pm.personnelmanagement.task.dto;

import jakarta.validation.constraints.NotNull;

public record CreateTaskDTO(@NotNull String createdBy, @NotNull TaskCreationRequest task) {
}
