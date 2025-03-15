package com.pm.personnelmanagement.taskevent.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskEventRequest(@NotNull UUID uuid) {
}
