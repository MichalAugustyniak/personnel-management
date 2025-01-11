package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAbsenceStatusDTO(@NotEmpty String name, @NotNull String description) {
}
