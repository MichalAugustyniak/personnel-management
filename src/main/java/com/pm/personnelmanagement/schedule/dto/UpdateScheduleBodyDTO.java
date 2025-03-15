package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record UpdateScheduleBodyDTO(
        String name,
        String description,
        Integer maxWorkingHoursPerDay,
        Boolean enableHolidayAssignments,
        Boolean enableWorkingSaturdays,
        Boolean enableWorkingSundays,
        Set<@NotNull UpdateScheduleDayDTO> existingScheduleDays,
        Set<@NotNull CreateScheduleDayDTO> newScheduleDays,
        Set<@NotNull UUID> deletedScheduleDays
) {
}
