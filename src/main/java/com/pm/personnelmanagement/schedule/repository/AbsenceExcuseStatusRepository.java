package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.AbsenceExcuseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbsenceExcuseStatusRepository extends JpaRepository<AbsenceExcuseStatus, Long> {
    Optional<AbsenceExcuseStatus> findByUuid(UUID uuid);
}
