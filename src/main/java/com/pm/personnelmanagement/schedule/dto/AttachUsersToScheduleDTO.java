package com.pm.personnelmanagement.schedule.dto;

import java.util.List;
import java.util.UUID;

public record AttachUsersToScheduleDTO(UUID scheduleUUID, List<UUID> userUUIDs) {
}
