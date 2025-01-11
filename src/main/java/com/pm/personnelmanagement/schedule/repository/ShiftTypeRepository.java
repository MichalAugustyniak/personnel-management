package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ShiftTypeRepository extends JpaRepository<ShiftType, Long> {
    Set<ShiftType> findAllByUuidIn(Collection<UUID> uuids);

    Optional<ShiftType> findByUuid(UUID uuid);
}
