package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.ScheduleDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDayRepository extends JpaRepository<ScheduleDay, Long>, JpaSpecificationExecutor<ScheduleDay> {
}
