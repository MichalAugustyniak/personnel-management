package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.AttendanceNotFoundException;
import com.pm.personnelmanagement.schedule.model.Attendance;
import com.pm.personnelmanagement.schedule.repository.AttendanceRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class AttendanceUtils {
    private final AttendanceRepository attendanceRepository;

    public AttendanceUtils(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance fetchAttendance(UUID uuid) {
        return attendanceRepository.findByUuid(uuid).orElseThrow(
                () -> new AttendanceNotFoundException(String.format("Attendance of uuid %s not found", uuid))
        );
    }

    public Set<Attendance> fetchAndValidateAttendances(Set<UUID> attendanceUUIDs) {
        Set<Attendance> attendances = attendanceRepository.findAllByUuidIn(attendanceUUIDs);
        if (attendances.size() != attendanceUUIDs.size()) {
            throw new AttendanceNotFoundException("Some of attendances might not exist");
        }
        return attendances;
    }
}
