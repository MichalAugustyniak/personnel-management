package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.AttendanceStatusNotFoundException;
import com.pm.personnelmanagement.schedule.model.AttendanceStatus;
import com.pm.personnelmanagement.schedule.repository.AttendanceStatusRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AttendanceStatusUtils {
    private final AttendanceStatusRepository attendanceStatusRepository;

    public AttendanceStatusUtils(AttendanceStatusRepository attendanceStatusRepository) {
        this.attendanceStatusRepository = attendanceStatusRepository;
    }

    public AttendanceStatus fetchAttendanceStatus(@NotNull UUID uuid) {
        return attendanceStatusRepository.findByUuid(uuid).orElseThrow(
                () -> new AttendanceStatusNotFoundException(
                        String.format("Attendance status of uuid %s not found", uuid)
                )
        );
    }
}
