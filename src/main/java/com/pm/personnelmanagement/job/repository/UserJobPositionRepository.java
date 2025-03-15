package com.pm.personnelmanagement.job.repository;

import com.pm.personnelmanagement.job.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJobPositionRepository extends JpaRepository<Contract, Long> {
}
