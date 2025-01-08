package com.pm.personnelmanagement.schedule.dto;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record UpdateScheduleBodyDTO(
        Optional<String> name,
        Optional<String> description,
        Optional<Integer> maxWorkingHoursPerDay,
        Optional<Boolean> enableHolidayAssignments,
        Optional<Boolean> enableWorkingSaturdays,
        Optional<Boolean> enableWorkingSundays,
        Set<UpdateScheduleDayDTO> existingScheduleDays,
        Set<CreateScheduleDayDTO> newScheduleDays,
        Set<UUID> deletedScheduleDays
) {
}
