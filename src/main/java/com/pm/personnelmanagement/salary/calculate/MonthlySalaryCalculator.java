package com.pm.personnelmanagement.salary.calculate;

import com.pm.personnelmanagement.user.model.User;

import java.math.BigDecimal;
import java.time.YearMonth;


public interface MonthlySalaryCalculator {
    BigDecimal calculate(User user, YearMonth yearMonth);
}
