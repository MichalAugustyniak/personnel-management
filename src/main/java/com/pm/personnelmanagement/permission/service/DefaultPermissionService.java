package com.pm.personnelmanagement.permission.service;

import com.pm.personnelmanagement.permission.constant.AccessTypes;
import com.pm.personnelmanagement.permission.dto.*;
import com.pm.personnelmanagement.permission.exception.PermissionNotFoundException;
import com.pm.personnelmanagement.permission.exception.UnauthorizedException;
import com.pm.personnelmanagement.permission.handler.ContextTypeHandler;
import com.pm.personnelmanagement.permission.handler.ContextTypeHandlerManager;
import com.pm.personnelmanagement.permission.mapping.PermissionMapper;
import com.pm.personnelmanagement.permission.model.Permission;
import com.pm.personnelmanagement.permission.repository.PermissionRepository;
import com.pm.personnelmanagement.permission.util.PermissionUtils;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import com.pm.personnelmanagement.user.constant.DefaultRoleNames;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.util.UserUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultPermissionService implements PermissionService {
    private final PermissionUtils permissionUtils;
    private final PermissionRepository permissionRepository;
    private final UserUtils userUtils;
    private final ContextTypeHandlerManager contextTypeHandlerManager;

    public DefaultPermissionService(PermissionUtils permissionUtils, PermissionRepository permissionRepository, UserUtils userUtils, ContextTypeHandlerManager contextTypeHandlerManager) {
        this.permissionUtils = permissionUtils;
        this.permissionRepository = permissionRepository;
        this.userUtils = userUtils;
        this.contextTypeHandlerManager = contextTypeHandlerManager;
    }

    @Override
    public PermissionDTO getPermission(@NotNull @Valid AuthenticatedRequest<PermissionRequest> request) {
        User user = userUtils.fetchUserByUsername(request.principalName());
        Permission permission = permissionUtils.fetchPermission(request.request().uuid());
        ContextTypeHandler contextTypeHandler = contextTypeHandlerManager.getContextTypeHandler(permission.getContextType());
        if (!user.getRole().getName().equals(DefaultRoleNames.ADMIN)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        else if (!contextTypeHandler.isUserAssociated(user, permission)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        return PermissionMapper.map(
                permissionUtils.fetchPermission(request.request().uuid())
        );
    }

    @Override
    public PermissionsResponse getPermissions(@NotNull @Valid AuthenticatedRequest<PermissionsRequest> request) {
        Specification<Permission> specification = Specification.where(null);
        Optional.ofNullable(request.request().contextId()).ifPresent(contextId -> {
            Specification<Permission> hasContextId = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("contextId"), contextId);
            specification.and(hasContextId);
        });
        Optional.ofNullable(request.request().contextType()).ifPresent(contextType -> {
            Specification<Permission> hasContextType = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("contextType"), contextType);
            specification.and(hasContextType);
        });
        Optional.ofNullable(request.request().resourceId()).ifPresent(resourceId -> {
            Specification<Permission> hasResourceId = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("resourceId"), resourceId);
            specification.and(hasResourceId);
        });
        Optional.ofNullable(request.request().resourceType()).ifPresent(resourceType -> {
            Specification<Permission> hasResourceType = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("resourceType"), resourceType);
            specification.and(hasResourceType);
        });
        Page<Permission> permissions = permissionUtils.fetchPermissions(specification, request.request().pageNumber(), request.request().pageSize());
        return PermissionMapper.map(permissions);
    }

    @Override
    public PermissionCreationResponse createPermission(@NotNull @Valid AuthenticatedRequest<PermissionCreationRequest> request) {
        checkAccessTypeOrElseThrowException(request.request().accessType());
        User user = userUtils.fetchUserByUsername(request.principalName());
        Permission handledPermission = new Permission();
        handledPermission.setResourceType(request.request().contextType());
        handledPermission.setContextId(request.request().contextId());
        ContextTypeHandler contextTypeHandler = contextTypeHandlerManager.getContextTypeHandler(request.request().contextType());
        if (!user.getRole().getName().equals(DefaultRoleNames.ADMIN) && !contextTypeHandler.isUserAssociated(user, handledPermission)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        UUID uuid = UUID.randomUUID();
        Permission permission = new Permission();
        permission.setUuid(uuid);
        permission.setAccessType(request.request().accessType());
        permission.setContextType(request.request().contextType());
        permission.setContextId(request.request().contextId());
        permission.setResourceType(request.request().resourceType());
        permission.setResourceId(request.request().resourceId());
        permissionRepository.save(permission);
        return new PermissionCreationResponse(uuid);
    }

    @Override
    public void updatePermission(@NotNull @Valid AuthenticatedRequest<PermissionEditionRequest> request) {
        User user = userUtils.fetchUserByUsername(request.principalName());
        Permission permission = permissionUtils.fetchPermission(request.request().uuid());
        ContextTypeHandler contextTypeHandler = contextTypeHandlerManager.getContextTypeHandler(permission.getContextType());
        if (!user.getRole().getName().equals(DefaultRoleNames.ADMIN) && !contextTypeHandler.isUserAssociated(user, permission)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        checkAccessTypeOrElseThrowException(request.request().permission().accessType());
        Optional.ofNullable(request.request().permission().accessType()).ifPresent(permission::setAccessType);
        permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(@NotNull @Valid AuthenticatedRequest<PermissionDeletionRequest> request) {
        User user = userUtils.fetchUserByUsername(request.principalName());
        Permission permission = permissionUtils.fetchPermission(request.request().uuid());
        ContextTypeHandler contextTypeHandler = contextTypeHandlerManager.getContextTypeHandler(permission.getContextType());
        if (!user.getRole().getName().equals(DefaultRoleNames.ADMIN) && !contextTypeHandler.isUserAssociated(user, permission)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        permissionRepository.delete(permission);
    }

    private boolean checkAccessType(String accessTypeName) {
        String[] accessTypes = {AccessTypes.OWNER, AccessTypes.MEMBER, AccessTypes.VIEWER};
        for (var accessType : accessTypes) {
            if (accessType.equals(accessTypeName)) {
                return true;
            }
        }
        return false;
    }

    private void checkAccessTypeOrElseThrowException(String accessType) {
        if (!checkAccessType(accessType)) {
            throw new PermissionNotFoundException(String.format(
                    "Access type '%s' does not exist", accessType
            ));
        }
    }
}
