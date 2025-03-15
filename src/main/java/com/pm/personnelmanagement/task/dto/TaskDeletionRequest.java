package com.pm.personnelmanagement.task.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskDeletionRequest(@NotNull UUID uuid) {
}
