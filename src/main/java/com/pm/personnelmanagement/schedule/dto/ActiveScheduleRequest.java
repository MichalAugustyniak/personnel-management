package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;

public record ActiveScheduleRequest(@NotEmpty String user) {
}
