package com.pm.personnelmanagement.permission.repository;

import com.pm.personnelmanagement.permission.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByUuid(UUID uuid);

    Page<Permission> findAll(Specification<Permission> specification, Pageable pageable);

    Page<Permission> findAllByResourceType(String resourceType, Pageable pageable);

    @Query("select p from Permission p where p.contextType = :contextType and p.resourceType = :resourceType")
    Set<Permission> foo(@Param("resourceType") String resourceType, @Param("contextType") String contextType, @Param("username") String username);
}
