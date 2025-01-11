package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.AbsenceExcuseUUIDSet;
import com.pm.personnelmanagement.schedule.dto.AttendancesUUIDSet;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AttendanceAbsenceExcuseController {
    ResponseEntity<Void> attachAttendanceToAbsenceExcuses(UUID attendanceUUID, AbsenceExcuseUUIDSet absenceExcuseUUIDs);

    ResponseEntity<Void> detachAttendanceFromAbsenceExcuses(UUID attendanceUUID, AbsenceExcuseUUIDSet absenceExcuseUUIDs);

    ResponseEntity<Void> attachAbsenceExcuseToAttendances(UUID absenceExcuseUUID, AttendancesUUIDSet attendancesUUIDs);

    ResponseEntity<Void> detachAbsenceExcuseFromAttendances(UUID absenceExcuseUUID, AttendancesUUIDSet attendancesUUIDs);
}
