package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.DetailedAbsenceExcuseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DetailedAbsenceExcuseStatusRepository extends JpaRepository<DetailedAbsenceExcuseStatus, Long>, JpaSpecificationExecutor<DetailedAbsenceExcuseStatus> {
    Optional<DetailedAbsenceExcuseStatus> findByUuid(UUID uuid);
}
