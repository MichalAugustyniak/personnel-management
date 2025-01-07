package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.WorkBreak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkBreakRepository extends JpaRepository<WorkBreak, Long>, JpaSpecificationExecutor<WorkBreak> {
}
