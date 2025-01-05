package com.pm.personnelmanagement.job.salary.model;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "salaries")
public class Salary {
    @Id
    private String id;
    private UUID uuid;
    private String salaryType;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
