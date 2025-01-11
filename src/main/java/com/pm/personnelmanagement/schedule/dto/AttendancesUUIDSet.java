package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record AttendancesUUIDSet(@NotNull Set<@NotNull UUID> attendancesUUIDs) {
}
