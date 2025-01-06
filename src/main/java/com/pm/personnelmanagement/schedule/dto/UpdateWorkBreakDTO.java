package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record UpdateWorkBreakDTO(UUID workBreakUUID, CreateWorkBreakDTO workBreak) {
}
