package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

import java.util.UUID;

public interface ScheduleService {
    UUID createSchedule(CreateScheduleDTO dto);

    void updateSchedule(UpdateScheduleDTO dto);

    void deleteSchedule(UUID uuid);

    ScheduleDTO getActiveSchedule(UUID uuid);

    ScheduleMetaListDTO getSchedules(FetchSchedulesFiltersDTO filters);

    ScheduleDTO getSchedule(UUID uuid);
}
