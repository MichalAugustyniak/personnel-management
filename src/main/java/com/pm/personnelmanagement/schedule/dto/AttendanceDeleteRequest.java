package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AttendanceDeleteRequest(@NotNull UUID uuid) {
}
