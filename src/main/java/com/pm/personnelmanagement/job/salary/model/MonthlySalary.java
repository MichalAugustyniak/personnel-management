package com.pm.personnelmanagement.job.salary.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "salaries")
public class MonthlySalary extends Salary {
    private BigDecimal monthlyAmount;
}
