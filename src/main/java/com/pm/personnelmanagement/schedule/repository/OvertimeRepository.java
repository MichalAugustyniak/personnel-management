package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.Overtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OvertimeRepository extends JpaRepository<Overtime, Long> {
}
