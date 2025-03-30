package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.AttendanceService;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<AttendanceResponse> getAttendance(@PathVariable UUID uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(attendanceService.getAttendance(
                new AuthenticatedRequest<>(
                        authentication.getName(),
                        new AttendanceRequest(uuid)
                ))
        );
    }

    @GetMapping
    @Override
    public ResponseEntity<AttendancesResponse> getAttendances(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) UUID scheduleDayUUID,
            @RequestParam(required = false) UUID attendanceStatusUUID
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(attendanceService.getAttendances(
                new AuthenticatedRequest<>(
                        authentication.getName(),
                        new AttendancesRequest(
                                pageNumber,
                                pageSize,
                                user,
                                scheduleDayUUID,
                                attendanceStatusUUID
                        )
                )
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<AttendanceCreationResponse> createAttendance(@RequestBody AttendanceCreationRequest dto) {
        System.out.println(dto);
        return new ResponseEntity<>(
                attendanceService.createAttendance(dto),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateAttendance(@PathVariable UUID uuid, @RequestBody UpdateAttendanceBodyDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        attendanceService.updateAttendance(
                new AuthenticatedRequest<>(
                        authentication.getName(),
                        new AttendanceUpdateRequest(uuid, dto)
                )
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteAttendance(@PathVariable UUID uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        attendanceService.deleteAttendance(
                new AuthenticatedRequest<>(
                        authentication.getName(),
                        new AttendanceDeleteRequest(uuid)
                )
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
