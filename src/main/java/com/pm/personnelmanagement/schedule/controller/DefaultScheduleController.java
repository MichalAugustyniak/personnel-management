package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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
    public ScheduleUUIDDTO createSchedule(@RequestBody CreateScheduleDTO dto) {
        return new ScheduleUUIDDTO(scheduleService.createSchedule(dto));
    }

    @PatchMapping("/{uuid}")
    @Override
    public void updateSchedule(@PathVariable UUID uuid, @RequestBody UpdateScheduleBodyDTO dto) {
        scheduleService.updateSchedule(new UpdateScheduleDTO(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    @Override
    public void deleteSchedule(@PathVariable UUID uuid) {
        scheduleService.deleteSchedule(uuid);
    }

    @GetMapping("/active/{uuid}")
    @Override
    public ScheduleDTO getActiveSchedule(@PathVariable UUID uuid) {
        return scheduleService.getActiveSchedule(uuid);
    }

    @GetMapping
    @Override
    public ScheduleMetaListDTO getSchedules(
            @RequestParam(required = false) UUID userUUID,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNumber
    ) {
        return scheduleService.getSchedules(new FetchSchedulesFiltersDTO(
                Optional.ofNullable(userUUID),
                Optional.ofNullable(isActive),
                Optional.ofNullable(pageSize),
                Optional.ofNullable(pageNumber)
        ));
    }

    @GetMapping("/{uuid}")
    @Override
    public ScheduleDTO getSchedule(@PathVariable UUID uuid) {
        return scheduleService.getSchedule(uuid);
    }
}
