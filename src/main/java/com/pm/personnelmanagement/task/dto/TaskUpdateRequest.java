package com.pm.personnelmanagement.task.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskUpdateRequest(@NotNull UUID uuid, @NotNull TaskUpdateRuquestBody task) {
}
