package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ScheduleService {
    UUID createSchedule(CreateScheduleDTO dto);

    void updateSchedule(UpdateScheduleDTO dto);

    void deleteSchedule(UUID uuid);

    ScheduleDTO getActiveSchedule(UUID uuid);

    Page<ScheduleUUIDDTO> getSchedules(FetchSchedulesFiltersDTO filters);
}
