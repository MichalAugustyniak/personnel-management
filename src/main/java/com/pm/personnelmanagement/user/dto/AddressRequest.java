package com.pm.personnelmanagement.user.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddressRequest(@NotNull UUID uuid) {
}
