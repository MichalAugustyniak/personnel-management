package com.pm.personnelmanagement.permission.dto;

import java.util.UUID;

public record PermissionDTO(
        UUID uuid,
        String access_type,
        String contextType,
        String contextId,
        String resourceType,
        String resourceId
) {
}
