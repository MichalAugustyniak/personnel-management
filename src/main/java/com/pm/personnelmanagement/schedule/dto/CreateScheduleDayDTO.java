package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record CreateScheduleDayDTO(
        @NotNull
        LocalDateTime startDateTime,
        @NotNull
        LocalDateTime endDateTime,
        @NotNull
        UUID shiftTypeUUID,
        @NotNull
        Set<@NotNull CreateWorkBreakDTO> workBreaks
) {
}
