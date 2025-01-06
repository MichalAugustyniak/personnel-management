package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;
import java.util.UUID;

public record ScheduleDTO(
        UUID uuid,
        String name,
        String description,
        int maxWorkingHoursPerDay,
        boolean enableHolidayAssignments,
        boolean enableWorkingSaturdays,
        boolean enableWorkingSundays,
        Set<ScheduleDayDTO> days
) {
}
