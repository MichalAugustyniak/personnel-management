package com.pm.personnelmanagement.schedule.dto;

import java.util.List;
import java.util.UUID;

public record AttachUsersToScheduleRequest(List<UUID> userUUIDs) {
}
