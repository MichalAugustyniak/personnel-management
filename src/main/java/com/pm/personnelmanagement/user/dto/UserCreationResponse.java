package com.pm.personnelmanagement.user.dto;

import java.util.UUID;

public record UserCreationResponse(
        UUID userUUID
) {
}
