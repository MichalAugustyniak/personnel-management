package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.AttendanceStatusDTO;
import com.pm.personnelmanagement.schedule.dto.AttendanceStatusesDTO;
import com.pm.personnelmanagement.schedule.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class AttendanceStatusMapper {
    public static AttendanceStatusDTO map(@NotNull AttendanceStatus attendanceStatus) {
        return new AttendanceStatusDTO(
                attendanceStatus.getUuid(),
                attendanceStatus.getName(),
                attendanceStatus.getDescription()
        );
    }

    public static AttendanceStatusesDTO map(@NotNull Page<AttendanceStatus> attendanceStatuses) {
        return new AttendanceStatusesDTO(
                attendanceStatuses.getTotalElements(),
                attendanceStatuses.getTotalPages(),
                attendanceStatuses.getNumber(),
                attendanceStatuses.getNumberOfElements(),
                attendanceStatuses.getSize(),
                attendanceStatuses.getContent().stream().map(
                        attendanceStatus -> new AttendanceStatusDTO(
                                attendanceStatus.getUuid(),
                                attendanceStatus.getName(),
                                attendanceStatus.getDescription()
                        )
                ).collect(Collectors.toSet())
        );
    }
}
