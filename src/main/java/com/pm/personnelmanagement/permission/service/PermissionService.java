package com.pm.personnelmanagement.permission.service;

import com.pm.personnelmanagement.permission.dto.*;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;

public interface PermissionService {
    PermissionDTO getPermission(AuthenticatedRequest<PermissionRequest> request);
    PermissionsResponse getPermissions(AuthenticatedRequest<PermissionsRequest> request);
    PermissionCreationResponse createPermission(AuthenticatedRequest<PermissionCreationRequest> request);
    void updatePermission(AuthenticatedRequest<PermissionEditionRequest> request);
    void deletePermission(AuthenticatedRequest<PermissionDeletionRequest> request);
}
