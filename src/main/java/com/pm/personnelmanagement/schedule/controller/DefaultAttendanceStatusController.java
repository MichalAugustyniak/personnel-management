package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.AttendanceStatusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/attendance-statuses")
public class DefaultAttendanceStatusController implements AttendanceStatusController {
    private final AttendanceStatusService attendanceStatusService;

    public DefaultAttendanceStatusController(AttendanceStatusService attendanceStatusService) {
        this.attendanceStatusService = attendanceStatusService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<AttendanceStatusDTO> getAttendanceStatus(@NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(attendanceStatusService.getAttendanceStatus(
                new AttendanceStatusRequest(uuid))
        );
    }

    @GetMapping
    @Override
    public ResponseEntity<AttendanceStatusesDTO> getAttendanceStatuses(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(attendanceStatusService.getAttendanceStatuses(
                new FetchAttendanceStatusesFiltersDTO(pageNumber, pageSize)
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<CreateAttendanceStatusResponse> createAttendanceStatus(
            @NotNull @Valid @RequestBody CreateAttendanceStatusDTO dto
    ) {
        return new ResponseEntity<>(
                attendanceStatusService.createAttendanceStatus(dto),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateAttendanceStatus(
            @NotNull @Valid @PathVariable UUID uuid,
            @NotNull @Valid @RequestBody UpdateAttendanceStatusBodyDTO dto
    ) {
        attendanceStatusService.updateAttendanceStatus(new UpdateAttendanceStatusDTO(uuid, dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteAttendanceStatus(@NotNull @PathVariable UUID uuid) {
        attendanceStatusService.deleteAttendanceStatus(new DeleteAttendanceStatusDTO(uuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
