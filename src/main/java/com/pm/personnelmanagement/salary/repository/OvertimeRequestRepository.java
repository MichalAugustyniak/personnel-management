package com.pm.personnelmanagement.salary.repository;

import com.pm.personnelmanagement.schedule.model.OvertimeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OvertimeRequestRepository extends JpaRepository<OvertimeRequest, Long> {
}
