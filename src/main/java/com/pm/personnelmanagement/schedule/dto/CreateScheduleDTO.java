package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;
import java.util.UUID;

public record CreateScheduleDTO(
        String name,
        String description,
        int maxWorkingHoursPerDay,
        boolean enableHolidayAssignments,
        boolean enableWorkingSaturdays,
        boolean enableWorkingSundays,
        Set<UUID> userUUIDList,
        Set<CreateScheduleDayDTO> scheduleDays
) {
}
