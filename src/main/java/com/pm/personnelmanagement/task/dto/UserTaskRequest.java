package com.pm.personnelmanagement.task.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserTaskRequest(@NotNull UUID userUUID) {
}
