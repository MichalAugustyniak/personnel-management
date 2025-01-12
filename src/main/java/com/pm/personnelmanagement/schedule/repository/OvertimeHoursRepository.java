package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.OvertimeHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OvertimeHoursRepository extends JpaRepository<OvertimeHours, Long>, JpaSpecificationExecutor<OvertimeHours> {
    Optional<OvertimeHours> findByUuid(UUID uuid);

    @Query("SELECT oh " +
            "FROM OvertimeHours oh " +
            "JOIN FETCH oh.user u " +
            "JOIN FETCH oh.approvedBy a " +
            "JOIN FETCH oh.scheduleDay s " +
            "WHERE oh.uuid = :uuid")
    Optional<OvertimeHours> findByUuidWithUserAndApprovedByAndScheduleDay(@Param("uuid") UUID uuid);
}
