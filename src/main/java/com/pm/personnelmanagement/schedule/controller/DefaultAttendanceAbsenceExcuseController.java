package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.AttendanceAbsenceExcuseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DefaultAttendanceAbsenceExcuseController implements AttendanceAbsenceExcuseController {
    private final AttendanceAbsenceExcuseService attendanceAbsenceExcuseService;

    public DefaultAttendanceAbsenceExcuseController(AttendanceAbsenceExcuseService absenceExcuseService) {
        this.attendanceAbsenceExcuseService = absenceExcuseService;
    }

    @PostMapping("/attendance/{attendanceUUID}/absence-excuses")
    @Override
    public ResponseEntity<Void> attachAttendanceToAbsenceExcuses(
            @NotNull @PathVariable UUID attendanceUUID,
            @NotNull @RequestBody AbsenceExcuseUUIDSet absenceExcuseUUIDs
    ) {
        attendanceAbsenceExcuseService.attachAttendanceToAbsenceExcuses(new AttachAttendanceToAbsenceExcusesDTO(
                        attendanceUUID,
                        absenceExcuseUUIDs.absenceExcuseUUIDs()
                )
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/attendance/{attendanceUUID}/absence-excuses")
    @Override
    public ResponseEntity<Void> detachAttendanceFromAbsenceExcuses(
            @NotNull @PathVariable UUID attendanceUUID,
            @Valid @NotNull @RequestBody AbsenceExcuseUUIDSet absenceExcuseUUIDs
    ) {
        attendanceAbsenceExcuseService.detachAttendanceFromAbsenceExcuses(new DetachAttendanceFromAbsenceExcusesDTO(
                        attendanceUUID,
                        absenceExcuseUUIDs.absenceExcuseUUIDs()
                )
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/absence-excuses/{absenceExcuseUUID}/attendances")
    @Override
    public ResponseEntity<Void> attachAbsenceExcuseToAttendances(
            @NotNull @PathVariable UUID absenceExcuseUUID,
            @Valid @NotNull @RequestBody AttendancesUUIDSet attendancesUUIDs
    ) {
        attendanceAbsenceExcuseService.attachAbsenceExcuseToAttendances(new AttachAbsenceExcuseToAttendancesDTO(
                absenceExcuseUUID,
                attendancesUUIDs.attendancesUUIDs())
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/absence-excuses/{absenceExcuseUUID}/attendances")
    @Override
    public ResponseEntity<Void> detachAbsenceExcuseFromAttendances(
            @NotNull @PathVariable UUID absenceExcuseUUID,
            @Valid @NotNull @RequestBody AttendancesUUIDSet attendancesUUIDs
    ) {
        attendanceAbsenceExcuseService.detachAbsenceExcuseFromAttendances(new DetachAbsenceExcuseFromAttendancesDTO(
                        absenceExcuseUUID,
                        attendancesUUIDs.attendancesUUIDs()
                )
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
