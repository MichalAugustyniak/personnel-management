package com.pm.personnelmanagement.permission.controller;

import com.pm.personnelmanagement.permission.dto.*;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.UUID;

public interface PermissionController {
    ResponseEntity<PermissionDTO> getPermission(UUID uuid, Principal principal);
    ResponseEntity<PermissionsResponse> getPermissions(String contextType,
                                                       String contextId,
                                                       String resourceType,
                                                       String resourceId,
                                                       int pageNumber,
                                                       int pageSize,
                                                       Principal principal);
    ResponseEntity<PermissionCreationResponse> createPermission(PermissionCreationRequest request, Principal principal);
    ResponseEntity<Void> updatePermission(UUID uuid, PermissionEditionBodyRequest permission, Principal principal);
    ResponseEntity<Void> deletePermission(UUID uuid, Principal principal);
}
