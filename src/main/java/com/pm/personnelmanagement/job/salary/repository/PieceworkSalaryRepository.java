package com.pm.personnelmanagement.job.salary.repository;

import com.pm.personnelmanagement.job.salary.model.PieceworkSalary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceworkSalaryRepository extends MongoRepository<PieceworkSalary, String> {
}
