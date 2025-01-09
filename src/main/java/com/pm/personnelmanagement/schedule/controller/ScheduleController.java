package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ScheduleController {
    ResponseEntity<ScheduleUUIDDTO> createSchedule(CreateScheduleDTO dto);

    ResponseEntity<Void> updateSchedule(UUID uuid, UpdateScheduleBodyDTO dto);

    ResponseEntity<Void> deleteSchedule(UUID uuid);

    ResponseEntity<ScheduleDTO> getActiveSchedule(UUID uuid);

    ResponseEntity<ScheduleMetaListDTO> getSchedules(
            UUID userUUID,
            Boolean isActive,
            Integer pageSize,
            Integer pageNumber
    );

    ResponseEntity<ScheduleDTO> getSchedule(UUID uuid);

    ResponseEntity<Void> attachUsersToSchedule(UUID scheduleUUID, AttachUsersToScheduleRequest request);

    ResponseEntity<Void> detachUsersFromSchedule(UUID scheduleUUID, DetachUsersFromScheduleRequest request);

    ResponseEntity<ScheduleDTO> getActiveScheduleByUser(UUID userUUID);
}
