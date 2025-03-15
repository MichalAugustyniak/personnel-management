package com.pm.personnelmanagement.taskevent.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskEventUpdateRequest(@NotNull UUID uuid, @NotNull TaskEventUpdateRequestBody taskEvent) {
}
