package com.pm.personnelmanagement.salary.repository;

import com.pm.personnelmanagement.salary.model.Salary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

//@Repository
public interface SalaryRepository extends MongoRepository<Salary, String> {
    Optional<Salary> findByUuid(UUID uuid);
}
