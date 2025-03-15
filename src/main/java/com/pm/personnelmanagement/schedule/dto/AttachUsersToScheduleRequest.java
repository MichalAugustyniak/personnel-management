package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AttachUsersToScheduleRequest(@NotNull List<@NotEmpty String> users) {
}
