package com.pm.personnelmanagement.user.repository;

import com.pm.personnelmanagement.user.model.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(long id);

    Optional<Role> findByUuid(UUID uuid);

    Optional<Role> findByName(String name);

    Set<Role> findAll(Specification<Role> specification, Pageable pageable);
}
