package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.ScheduleDTO;
import com.pm.personnelmanagement.schedule.dto.ScheduleDayDTO;
import com.pm.personnelmanagement.schedule.dto.WorkBreakDTO;
import com.pm.personnelmanagement.schedule.model.Schedule;

import java.util.stream.Collectors;

public class ScheduleMapper {
    public static ScheduleDTO map(Schedule schedule) {
        return new ScheduleDTO(
                schedule.getUuid(),
                schedule.getName(),
                schedule.getDescription(),
                schedule.getMaxWorkingHoursPerDay(),
                schedule.getEnableHolidayAssignments(),
                schedule.getEnableWorkingSaturdays(),
                schedule.getEnableWorkingSundays(),
                schedule.getScheduleDays().stream().map(scheduleDay -> new ScheduleDayDTO(
                        scheduleDay.getUuid(),
                        scheduleDay.getSchedule().getUuid(),
                        scheduleDay.getStartDateTime(),
                        scheduleDay.getEndDateTime(),
                        scheduleDay.getWorkBreaks().stream().map(workBreak -> new WorkBreakDTO(
                                workBreak.getUuid(),
                                workBreak.getStartDateTime(),
                                workBreak.getEndDateTime(),
                                workBreak.getScheduleDay().getUuid(),
                                workBreak.getPaid()
                        )).collect(Collectors.toSet())
                )).collect(Collectors.toSet())
        );
    }
}
