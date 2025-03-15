package com.pm.personnelmanagement.schedule.dto;

import com.pm.personnelmanagement.salary.calculate.TimeRecord;
import com.pm.personnelmanagement.schedule.model.AttendanceStatus;
import com.pm.personnelmanagement.schedule.model.ScheduleDay;

import java.util.Map;

public record ScheduleDayTypeAttendanceTime(Class<? extends ScheduleDay> type, Map<AttendanceStatus, TimeRecord> attendanceTime) {
}
