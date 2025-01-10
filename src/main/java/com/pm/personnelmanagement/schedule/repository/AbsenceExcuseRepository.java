package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.AbsenceExcuse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbsenceExcuseRepository extends JpaRepository<AbsenceExcuse, Long> {
    Optional<AbsenceExcuse> findByUuid(UUID uuid);
}
