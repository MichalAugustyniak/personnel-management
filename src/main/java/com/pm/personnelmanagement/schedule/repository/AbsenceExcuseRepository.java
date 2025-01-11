package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.AbsenceExcuse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AbsenceExcuseRepository extends JpaRepository<AbsenceExcuse, Long>, JpaSpecificationExecutor<AbsenceExcuse> {
    Optional<AbsenceExcuse> findByUuid(UUID uuid);

    Set<AbsenceExcuse> findAllByUuidIn(Collection<UUID> uuids);
}
