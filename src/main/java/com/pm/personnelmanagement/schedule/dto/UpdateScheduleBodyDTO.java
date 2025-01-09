package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;
import java.util.UUID;

public record UpdateScheduleBodyDTO(
        String name,
        String description,
        Integer maxWorkingHoursPerDay,
        Boolean enableHolidayAssignments,
        Boolean enableWorkingSaturdays,
        Boolean enableWorkingSundays,
        Set<UpdateScheduleDayDTO> existingScheduleDays,
        Set<CreateScheduleDayDTO> newScheduleDays,
        Set<UUID> deletedScheduleDays
) {
}
