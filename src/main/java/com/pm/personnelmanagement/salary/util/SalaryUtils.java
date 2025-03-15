package com.pm.personnelmanagement.salary.util;

import com.pm.personnelmanagement.salary.exception.SalaryNotFoundException;
import com.pm.personnelmanagement.salary.model.Salary;
import com.pm.personnelmanagement.salary.repository.SalaryRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SalaryUtils {
    private final SalaryRepository salaryRepository;

    public SalaryUtils(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    public Salary fetchSalary(@NotNull UUID uuid) {
        return salaryRepository.findByUuid(uuid).orElseThrow(
                () -> new SalaryNotFoundException(
                        String.format("Salary of uuid %s not found", uuid)
                )
        );
    }
}
