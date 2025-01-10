package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceStatusRepository extends JpaRepository<AttendanceStatus, Long> {
    Optional<AttendanceStatus> findByUuid(UUID uuid);
}
