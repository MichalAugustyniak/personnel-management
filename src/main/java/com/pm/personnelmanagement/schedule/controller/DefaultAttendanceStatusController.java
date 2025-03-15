package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.AttendanceStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/attendance-statuses")
public class DefaultAttendanceStatusController implements AttendanceStatusController {
    private final AttendanceStatusService attendanceStatusService;

    public DefaultAttendanceStatusController(AttendanceStatusService attendanceStatusService) {
        this.attendanceStatusService = attendanceStatusService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<AttendanceStatusResponse> getAttendanceStatus(@PathVariable UUID uuid) {
        return ResponseEntity.ok(attendanceStatusService.getAttendanceStatus(
                new AttendanceStatusRequest(uuid))
        );
    }

    @GetMapping
    @Override
    public ResponseEntity<AttendanceStatusesResponse> getAttendanceStatuses(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(attendanceStatusService.getAttendanceStatuses(
                new FetchAttendanceStatusesFiltersDTO(pageNumber, pageSize)
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<AttendanceStatusCreationResponse> createAttendanceStatus(
            @RequestBody AttendanceStatusCreationRequest dto
    ) {
        return new ResponseEntity<>(
                attendanceStatusService.createAttendanceStatus(dto),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateAttendanceStatus(
            @PathVariable UUID uuid,
            @RequestBody AttendanceStatusUpdateRequestBody body
    ) {
        attendanceStatusService.updateAttendanceStatus(new UpdateAttendanceStatusDTO(uuid, body));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteAttendanceStatus(@PathVariable UUID uuid) {
        attendanceStatusService.deleteAttendanceStatus(new DeleteAttendanceStatusDTO(uuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
