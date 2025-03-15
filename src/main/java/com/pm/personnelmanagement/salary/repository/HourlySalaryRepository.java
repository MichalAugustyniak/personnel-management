package com.pm.personnelmanagement.salary.repository;

import com.pm.personnelmanagement.salary.model.HourlySalary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface HourlySalaryRepository extends MongoRepository<HourlySalary, String> {
}
