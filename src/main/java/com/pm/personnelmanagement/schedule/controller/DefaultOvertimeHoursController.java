package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.OvertimeHoursService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/overtime-hours")
public class DefaultOvertimeHoursController implements OvertimeHoursController {
    private final OvertimeHoursService overtimeHoursService;

    public DefaultOvertimeHoursController(OvertimeHoursService overtimeHoursService) {
        this.overtimeHoursService = overtimeHoursService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<OvertimeHoursDTO> getOvertimeHours(@NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(overtimeHoursService.getOvertimeHours(new OvertimeHoursRequest(uuid)));
    }

    @GetMapping
    @Override
    public ResponseEntity<OvertimeHoursPagedDTO> getOvertimeHoursPaged(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) UUID approvedByUUID,
            @RequestParam(required = false) UUID userUUID,
            @RequestParam(required = false) UUID scheduleDayUUID,
            @RequestParam(required = false) LocalDateTime startDateTime,
            @RequestParam(required = false) LocalDateTime endDateTime
    ) {
        return ResponseEntity.ok(overtimeHoursService.getOvertimeHoursPaged(
                new OvertimeHoursRequestFilters(
                        pageNumber,
                        pageSize,
                        approvedByUUID,
                        userUUID,
                        scheduleDayUUID,
                        startDateTime,
                        endDateTime
                )
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<OvertimeHoursCreationResponse> createOvertimeHours(
            @NotNull @Valid @RequestBody OvertimeHoursCreationRequest request
    ) {
        overtimeHoursService.createOvertimeHours(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateOvertimeHours(
            @NotNull @PathVariable UUID uuid,
            @NotNull @Valid @RequestBody OvertimeHoursUpdateRequestBody body
    ) {
        overtimeHoursService.updateOvertimeHours(new OvertimeHoursUpdateRequest(uuid, body));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteOvertimeHours(@NotNull @PathVariable UUID uuid) {
        overtimeHoursService.deleteOvertimeHours(new OvertimeHoursDeletionRequest(uuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
