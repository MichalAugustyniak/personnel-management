package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record DetachUsersFromScheduleDTO(
        @NotNull
        UUID scheduleUUID,
        @NotNull
        List<@NotEmpty String> users
) {
}
