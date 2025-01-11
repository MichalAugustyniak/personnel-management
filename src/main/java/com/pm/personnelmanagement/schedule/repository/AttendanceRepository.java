package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {
    Optional<Attendance> findByUuid(UUID uuid);

    Set<Attendance> findAllByUuidIn(Collection<UUID> uuids);
}
