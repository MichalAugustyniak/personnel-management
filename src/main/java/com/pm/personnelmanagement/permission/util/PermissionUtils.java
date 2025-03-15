package com.pm.personnelmanagement.permission.util;

import com.pm.personnelmanagement.permission.exception.PermissionNotFoundException;
import com.pm.personnelmanagement.permission.model.Permission;
import com.pm.personnelmanagement.permission.repository.PermissionRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PermissionUtils {
    private final PermissionRepository permissionRepository;

    public PermissionUtils(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission fetchPermission(@NotNull UUID uuid) {
        return permissionRepository.findByUuid(uuid).orElseThrow(
                () -> new PermissionNotFoundException(
                        String.format("Permission of uuid %s not found", uuid)
                )
        );
    }

    public Page<Permission> fetchPermissions(@NotNull Specification<Permission> specification, int pageNumber, int pageSize) {
        return permissionRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
    }

}
