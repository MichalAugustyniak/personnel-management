package com.pm.personnelmanagement.permission.dto;

import java.util.List;

public record PermissionsResponse(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        List<PermissionDTO> permissions
) {
}
