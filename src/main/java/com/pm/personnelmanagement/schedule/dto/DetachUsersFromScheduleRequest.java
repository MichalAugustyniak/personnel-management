package com.pm.personnelmanagement.schedule.dto;

import java.util.List;
import java.util.UUID;

public record DetachUsersFromScheduleRequest(List<UUID> userUUIDs) {
}
