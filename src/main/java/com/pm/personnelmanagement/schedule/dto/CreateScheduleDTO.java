package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record CreateScheduleDTO(
        @Size(min = 4, max = 30)
        String name,
        String description,
        int maxWorkingHoursPerDay,
        boolean enableHolidayAssignments,
        boolean enableWorkingSaturdays,
        boolean enableWorkingSundays,
        @NotNull
        Set<@NotEmpty String> users,
        @NotNull
        Set<@NotNull CreateScheduleDayDTO> scheduleDays
) {
}
