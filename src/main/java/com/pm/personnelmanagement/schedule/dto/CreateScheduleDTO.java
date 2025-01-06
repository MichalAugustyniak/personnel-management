package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;
import java.util.UUID;

public record CreateScheduleDTO(
        String name,
        String description,
        Boolean maxWorkingHoursPerDay,
        Boolean enableHolidayAssignments,
        Boolean enableWorkingSaturdays,
        Boolean enableWorkingSundays,
        Set<UUID> userUUIDList,
        Set<CreateScheduleDayDTO> scheduleDays
) {
}
