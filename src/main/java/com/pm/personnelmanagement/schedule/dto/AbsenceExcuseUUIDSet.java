package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record AbsenceExcuseUUIDSet(@NotNull Set<@NotNull UUID> absenceExcuseUUIDs) {
}
