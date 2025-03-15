package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.ScheduleService;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/schedules")
public class DefaultScheduleController implements ScheduleController {
    private final ScheduleService scheduleService;

    public DefaultScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    @Override
    public ResponseEntity<ScheduleCreationResponse> createSchedule(@RequestBody CreateScheduleDTO dto) {
        return new ResponseEntity<>(new ScheduleCreationResponse(
                scheduleService.createSchedule(dto)
        ), HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateSchedule(@PathVariable UUID uuid, @RequestBody UpdateScheduleBodyDTO dto) {
        scheduleService.updateSchedule(new UpdateScheduleDTO(uuid, dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID uuid) {
        scheduleService.deleteSchedule(new ScheduleDeleteRequest(uuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @Override
    public ResponseEntity<SchedulesResponse> getSchedules(
            @RequestParam(required = false) String user,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNumber
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(scheduleService.getSchedules(
                new AuthenticatedRequest<>(
                        authentication.getName(),
                        new SchedulesRequest(
                                user,
                                isActive,
                                pageSize,
                                pageNumber
                        ))
                )
        );
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable UUID uuid) {
        return ResponseEntity.ok(scheduleService.getSchedule(new ScheduleRequest(uuid)));
    }

    @PostMapping("/{scheduleUUID}/users")
    @Override
    public ResponseEntity<Void> attachUsersToSchedule(
            @PathVariable UUID scheduleUUID,
            @RequestBody AttachUsersToScheduleRequest request
    ) {
        scheduleService.attachUsersToSchedule(
                new AttachUsersToScheduleDTO(
                        scheduleUUID,
                        request.users()
                )
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleUUID}/users")
    @Override
    public ResponseEntity<Void> detachUsersFromSchedule(
            @PathVariable UUID scheduleUUID,
            @RequestBody DetachUsersFromScheduleRequest request
    ) {
        scheduleService.detachUsersFromSchedule(
                new DetachUsersFromScheduleDTO(
                        scheduleUUID,
                        request.users()
                )
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
