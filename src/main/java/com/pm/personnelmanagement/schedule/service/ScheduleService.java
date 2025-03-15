package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;

import java.util.UUID;

public interface ScheduleService {
    UUID createSchedule(CreateScheduleDTO dto);

    void updateSchedule(UpdateScheduleDTO dto);

    void deleteSchedule(ScheduleDeleteRequest request);

    //ScheduleDTO getActiveSchedule(UUID uuid);

    SchedulesResponse getSchedules(AuthenticatedRequest<SchedulesRequest> request);

    ScheduleDTO getSchedule(ScheduleRequest request);

    void attachUsersToSchedule(AttachUsersToScheduleDTO dto);

    void detachUsersFromSchedule(DetachUsersFromScheduleDTO dto);

    //ScheduleDTO getActiveScheduleByUser(String user);
}
