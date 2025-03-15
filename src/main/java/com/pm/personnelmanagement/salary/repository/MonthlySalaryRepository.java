package com.pm.personnelmanagement.salary.repository;

import com.pm.personnelmanagement.salary.model.MonthlySalary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface MonthlySalaryRepository extends MongoRepository<MonthlySalary, String> {
}
