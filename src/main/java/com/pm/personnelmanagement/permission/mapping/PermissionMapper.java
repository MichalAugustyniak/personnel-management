package com.pm.personnelmanagement.permission.mapping;

import com.pm.personnelmanagement.permission.dto.PermissionDTO;
import com.pm.personnelmanagement.permission.dto.PermissionsResponse;
import com.pm.personnelmanagement.permission.model.Permission;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public class PermissionMapper {
    public static PermissionDTO map(@NotNull Permission permission) {
        return new PermissionDTO(
                permission.getUuid(),
                permission.getAccessType(),
                permission.getContextType(),
                permission.getContextId(),
                permission.getResourceType(),
                permission.getResourceId()
        );
    }

    public static PermissionsResponse map(@NotNull Page<Permission> permissions) {
        return new PermissionsResponse(
                permissions.getTotalElements(),
                permissions.getTotalPages(),
                permissions.getNumber(),
                permissions.getNumberOfElements(),
                permissions.getSize(),
                permissions.getContent().stream().map(PermissionMapper::map).toList()
        );
    }
}
