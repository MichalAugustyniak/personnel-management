package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalTime;
import java.util.UUID;

public record ShiftTypeDTO(
        UUID uuid,
        String name,
        String description,
        LocalTime startTime,
        LocalTime endTime
) {
}
