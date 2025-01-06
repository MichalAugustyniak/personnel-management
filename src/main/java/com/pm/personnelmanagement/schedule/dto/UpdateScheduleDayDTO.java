package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;
import java.util.UUID;

public record UpdateScheduleDayDTO(
        UUID scheduleDayUUID,
        CreateScheduleDayDTO scheduleDay,
        Set<UpdateWorkBreakDTO> workBreaks
) {
}
