package com.pm.personnelmanagement.permission.dto;

import jakarta.validation.constraints.NotEmpty;

public record PermissionCreationRequest(
        @NotEmpty String accessType,
        @NotEmpty String contextType,
        @NotEmpty String contextId,
        @NotEmpty String resourceType,
        @NotEmpty String resourceId
) {
}
