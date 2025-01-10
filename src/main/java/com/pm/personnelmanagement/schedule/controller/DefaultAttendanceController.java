package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/attendances")
public class DefaultAttendanceController implements AttendanceController {
    private final AttendanceService attendanceService;

    public DefaultAttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<AttendanceDTO> getAttendance(@PathVariable UUID uuid) {
        return ResponseEntity.ok(attendanceService.getAttendance(uuid));
    }

    @GetMapping
    @Override
    public ResponseEntity<AttendanceListDTO> getAttendances(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) UUID userUUID,
            @RequestParam(required = false) UUID scheduleDayUUID,
            @RequestParam(required = false) UUID absenceExcuseUUID,
            @RequestParam(required = false) UUID attendanceStatusUUID
    ) {
        return ResponseEntity.ok(attendanceService.getAttendances(
                new FetchAttendancesFiltersDTO(
                        pageNumber,
                        pageSize,
                        userUUID,
                        scheduleDayUUID,
                        absenceExcuseUUID,
                        attendanceStatusUUID
                )
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<AttendanceUUIDResponse> createAttendance(CreateAttendanceDTO dto) {
        return new ResponseEntity<>(
                new AttendanceUUIDResponse(attendanceService.createAttendance(dto)),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateAttendance(@PathVariable UUID uuid, UpdateAttendanceBodyDTO dto) {
        attendanceService.updateAttendance(new UpdateAttendanceDTO(uuid, dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteAttendance(@PathVariable UUID uuid) {
        attendanceService.deleteAttendance(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
