package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.AttendanceStatusResponse;
import com.pm.personnelmanagement.schedule.dto.AttendanceStatusesResponse;
import com.pm.personnelmanagement.schedule.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class AttendanceStatusMapper {
    public static AttendanceStatusResponse map(@NotNull AttendanceStatus attendanceStatus) {
        return new AttendanceStatusResponse(
                attendanceStatus.getUuid(),
                attendanceStatus.getName(),
                attendanceStatus.getDescription(),
                attendanceStatus.getExcusable()
        );
    }

    public static AttendanceStatusesResponse map(@NotNull Page<AttendanceStatus> attendanceStatuses) {
        return new AttendanceStatusesResponse(
                attendanceStatuses.getTotalElements(),
                attendanceStatuses.getTotalPages(),
                attendanceStatuses.getNumber(),
                attendanceStatuses.getNumberOfElements(),
                attendanceStatuses.getSize(),
                attendanceStatuses.getContent().stream().map(
                        AttendanceStatusMapper::map
                ).collect(Collectors.toSet())
        );
    }
}
