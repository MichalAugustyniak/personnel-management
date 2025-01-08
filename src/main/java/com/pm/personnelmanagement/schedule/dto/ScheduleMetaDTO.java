package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record ScheduleMetaDTO(
        UUID uuid,
        String name,
        String description,
        int maxWorkingHoursPerDay,
        boolean enableHolidayAssignments,
        boolean enableWorkingSaturdays,
        boolean enableWorkingSundays
) {
}
