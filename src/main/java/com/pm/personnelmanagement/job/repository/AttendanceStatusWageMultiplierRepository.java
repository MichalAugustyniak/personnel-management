package com.pm.personnelmanagement.job.repository;

import com.pm.personnelmanagement.job.model.AttendanceStatusWageMultiplier;
import com.pm.personnelmanagement.schedule.model.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AttendanceStatusWageMultiplierRepository extends JpaRepository<AttendanceStatusWageMultiplier, Long> {
    Set<AttendanceStatusWageMultiplier> findAllByAttendanceStatusIn(Collection<AttendanceStatus> attendanceStatuses);

    Optional<AttendanceStatusWageMultiplier> findByAttendanceStatus(AttendanceStatus attendanceStatus);
}
