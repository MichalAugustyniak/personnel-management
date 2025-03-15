package com.pm.personnelmanagement.permission.repository;

import com.pm.personnelmanagement.permission.model.GlobalPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalPermissionRepository extends JpaRepository<GlobalPermission, Long> {
}
