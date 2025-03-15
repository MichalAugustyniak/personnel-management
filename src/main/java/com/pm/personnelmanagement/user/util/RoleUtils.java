package com.pm.personnelmanagement.user.util;

import com.pm.personnelmanagement.user.exception.RoleNotFoundException;
import com.pm.personnelmanagement.user.model.Role;
import com.pm.personnelmanagement.user.repository.RoleRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class RoleUtils {
    private final RoleRepository roleRepository;

    public RoleUtils(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role fetchRoleByUUID(@NotNull UUID uuid) {
        return roleRepository.findByUuid(uuid).orElseThrow(
                () -> new RoleNotFoundException(String.format(
                        "Role of uuid %s not found", uuid
                ))
        );
    }

    public Role fetchRoleById(long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new RoleNotFoundException(String.format(
                        "Role of id %s not found", id
                ))
        );
    }

    public Role fetchRoleByName(@NotEmpty String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new RoleNotFoundException(String.format(
                        "Role of name %s not found", name
                ))
        );
    }

    public Set<Role> fetchRoles(@NotNull Specification<Role> specification, int pageSize, int pageNumber) {
        return roleRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
    }
}
