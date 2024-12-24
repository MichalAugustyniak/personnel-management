package com.pm.personnelmanagement.security.repository;

import com.pm.personnelmanagement.security.model.GlobalPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalPermissionRepository extends JpaRepository<GlobalPermission, Long> {
}
