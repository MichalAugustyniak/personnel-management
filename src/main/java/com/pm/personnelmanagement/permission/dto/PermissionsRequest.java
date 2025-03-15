package com.pm.personnelmanagement.permission.dto;

public record PermissionsRequest(
        int pageNumber,
        int pageSize,
        String contextType,
        String contextId,
        String resourceType,
        String resourceId
) {
}
