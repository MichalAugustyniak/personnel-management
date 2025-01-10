package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.AttendanceDTO;
import com.pm.personnelmanagement.schedule.dto.AttendanceListDTO;
import com.pm.personnelmanagement.schedule.dto.AttendanceStatusDTO;
import com.pm.personnelmanagement.schedule.model.Attendance;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public class AttendanceMapper {
    public static AttendanceDTO map(@NotNull Attendance attendance) {
        Optional.ofNullable(attendance).orElseThrow(() -> new IllegalArgumentException("Argument attendance cannot be null"));
        Optional.ofNullable(attendance.getAttendanceStatus()).orElseThrow(() -> new IllegalArgumentException("Field attendance.attendanceStatus cannot be null"));
        return new AttendanceDTO(
                Optional.ofNullable(attendance.getUuid()).orElseThrow(() -> new IllegalArgumentException("Field uuid cannot be null")),
                Optional.ofNullable(Optional.ofNullable(attendance.getUser()).orElseThrow(() -> new IllegalArgumentException("Field user.uuid cannot be null")).getUuid())
                                .orElseThrow(() -> new IllegalArgumentException("Field user.uuid cannot be null")),
                Optional.ofNullable(Optional.ofNullable(attendance.getScheduleDay()).orElseThrow(() -> new IllegalArgumentException("Field scheduleDay field cannot be null")).getUuid())
                        .orElseThrow(() -> new IllegalArgumentException("Field scheduleDay.uuid cannot be null")),
                Optional.ofNullable(attendance.getStartDateTime()).orElseThrow(() -> new IllegalArgumentException("Field startDateTime cannot be null")),
                Optional.ofNullable(attendance.getEndDateTime()).orElseThrow(() -> new IllegalArgumentException("Field endDateTime cannot be null")),
                new AttendanceStatusDTO(
                        Optional.ofNullable(attendance.getAttendanceStatus().getUuid()).orElseThrow(() -> new IllegalArgumentException("Field attendance.attendanceStatus.uuid cannot be null")),
                        Optional.ofNullable(attendance.getAttendanceStatus().getName()).orElseThrow(() -> new IllegalArgumentException("Field attendance.attendanceStatus.name cannot be null")),
                        Optional.ofNullable(attendance.getAttendanceStatus().getDescription()).orElseThrow(() -> new IllegalArgumentException("Field attendance.attendanceStatus.description cannot be null"))
                )
        );
    }
    public static AttendanceListDTO map(@NotNull Page<Attendance> attendances) {
        Optional.ofNullable(attendances).orElseThrow(() -> new IllegalArgumentException("Argument attendances cannot be null"));
        return new AttendanceListDTO(
                attendances.getTotalElements(),
                attendances.getTotalPages(),
                attendances.getNumber(),
                attendances.getNumberOfElements(),
                attendances.getSize(),
                attendances.getContent().stream().map(AttendanceMapper::map).toList()
        );
    }
}
