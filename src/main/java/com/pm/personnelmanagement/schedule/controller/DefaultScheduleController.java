package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
public class DefaultScheduleController implements ScheduleController {
    private final ScheduleService scheduleService;

    public DefaultScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    @Override
    public ResponseEntity<ScheduleUUIDDTO> createSchedule(@RequestBody CreateScheduleDTO dto) {
        return new ResponseEntity<>(new ScheduleUUIDDTO(
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
        scheduleService.deleteSchedule(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/active/{uuid}")
    @Override
    public ResponseEntity<ScheduleDTO> getActiveSchedule(@PathVariable UUID uuid) {
        return ResponseEntity.ok(scheduleService.getActiveSchedule(uuid));
    }

    @GetMapping
    @Override
    public ResponseEntity<ScheduleMetaListDTO> getSchedules(
            @RequestParam(required = false) UUID userUUID,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNumber
    ) {
        return ResponseEntity.ok(scheduleService.getSchedules(
                new FetchSchedulesFiltersDTO(
                        userUUID,
                        isActive,
                        pageSize,
                        pageNumber
                )));
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable UUID uuid) {
        return ResponseEntity.ok(scheduleService.getSchedule(uuid));
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
                        request.userUUIDs()
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
                        request.userUUIDs()
                )
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/active")
    @Override
    public ResponseEntity<ScheduleDTO> getActiveScheduleByUser(@RequestParam UUID userUUID) {
        return ResponseEntity.ok(scheduleService.getActiveScheduleByUser(userUUID));
    }
}
