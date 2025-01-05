package com.pm.personnelmanagement.job.salary.repository;

import com.pm.personnelmanagement.job.salary.model.Salary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends MongoRepository<Salary, String> {
}
