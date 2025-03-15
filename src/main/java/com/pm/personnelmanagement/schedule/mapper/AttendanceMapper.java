package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.AttendanceResponse;
import com.pm.personnelmanagement.schedule.dto.AttendancesResponse;
import com.pm.personnelmanagement.schedule.dto.AttendanceStatusResponse;
import com.pm.personnelmanagement.schedule.model.Attendance;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public class AttendanceMapper {
    public static AttendanceResponse map(@NotNull Attendance attendance) {
        return new AttendanceResponse(
                attendance.getUuid(),
                attendance.getUser().getUsername(),
                attendance.getScheduleDay().getUuid(),
                attendance.getStartDateTime(),
                attendance.getEndDateTime(),
                new AttendanceStatusResponse(
                        attendance.getAttendanceStatus().getUuid(),
                        attendance.getAttendanceStatus().getName(),
                        attendance.getAttendanceStatus().getDescription(),
                        attendance.getAttendanceStatus().getExcusable()
                )
        );
    }
    public static AttendancesResponse map(@NotNull Page<Attendance> attendances) {
        return new AttendancesResponse(
                attendances.getTotalElements(),
                attendances.getTotalPages(),
                attendances.getNumber(),
                attendances.getNumberOfElements(),
                attendances.getSize(),
                attendances.getContent().stream().map(AttendanceMapper::map).toList()
        );
    }
}
