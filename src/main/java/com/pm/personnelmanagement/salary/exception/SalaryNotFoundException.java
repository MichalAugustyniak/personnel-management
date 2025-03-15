package com.pm.personnelmanagement.salary.exception;

public class SalaryNotFoundException extends RuntimeException {
    public SalaryNotFoundException() {
    }

    public SalaryNotFoundException(String message) {
        super(message);
    }
}
