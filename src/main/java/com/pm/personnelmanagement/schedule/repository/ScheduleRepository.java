package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    Optional<Schedule> findByUuid(UUID uuid);

    @Query("SELECT s FROM Schedule s " +
            "JOIN FETCH s.scheduleDays sd " +
            "LEFT JOIN FETCH sd.workBreaks wb " +
            "WHERE s.uuid = :uuid " +
            "ORDER BY sd.startDateTime ASC, wb.startDateTime ASC")
    Optional<Schedule> findByUuidWithSortedDaysAndBreaks(UUID uuid);
}
