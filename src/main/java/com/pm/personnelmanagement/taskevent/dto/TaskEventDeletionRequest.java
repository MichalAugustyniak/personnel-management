package com.pm.personnelmanagement.taskevent.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskEventDeletionRequest(@NotNull UUID uuid) {
}
