package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleController {
    ScheduleUUIDDTO createSchedule(CreateScheduleDTO dto);

    void updateSchedule(UUID uuid, UpdateScheduleBodyDTO dto);

    void deleteSchedule(UUID uuid);

    ScheduleDTO getActiveSchedule(UUID uuid);

    ScheduleMetaListDTO getSchedules(
            UUID userUUID,
            Boolean isActive,
            Integer pageSize,
            Integer pageNumber
    );

    ScheduleDTO getSchedule(UUID uuid);
}
