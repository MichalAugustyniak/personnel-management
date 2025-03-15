package com.pm.personnelmanagement.permission.controller;

import com.pm.personnelmanagement.permission.dto.*;
import com.pm.personnelmanagement.permission.service.PermissionService;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
public class DefaultPermissionController implements PermissionController {
    private final PermissionService permissionService;

    public DefaultPermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<PermissionDTO> getPermission(@NotNull @PathVariable UUID uuid, @NotNull Principal principal) {
        return ResponseEntity.ok(permissionService.getPermission(new AuthenticatedRequest<>(principal.getName(), new PermissionRequest(uuid))));
    }

    @GetMapping
    @Override
    public ResponseEntity<PermissionsResponse> getPermissions(
            @RequestParam String contextType,
            @RequestParam String contextId,
            @RequestParam String resourceType,
            @RequestParam String resourceId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            Principal principal
    ) {
        return ResponseEntity.ok(permissionService.getPermissions(
                new AuthenticatedRequest<>(principal.getName(), new PermissionsRequest(
                        pageNumber,
                        pageSize,
                        contextType,
                        contextId,
                        resourceType,
                        resourceId
                ))
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<PermissionCreationResponse> createPermission(@NotNull @Valid @RequestBody PermissionCreationRequest request, @NotNull Principal principal) {
        permissionService.createPermission(new AuthenticatedRequest<>(principal.getName(), new PermissionCreationRequest(
                request.accessType(),
                request.contextType(),
                request.contextId(),
                request.resourceType(),
                request.resourceId()
        )));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updatePermission(@NotNull @PathVariable UUID uuid, @NotNull @Valid @RequestBody PermissionEditionBodyRequest permission, @NotNull Principal principal) {
        permissionService.updatePermission(new AuthenticatedRequest<>(principal.getName(), new PermissionEditionRequest(uuid, permission)));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deletePermission(@NotNull @PathVariable UUID uuid, @NotNull Principal principal) {
        permissionService.deletePermission(new AuthenticatedRequest<>(principal.getName(), new PermissionDeletionRequest(uuid)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
