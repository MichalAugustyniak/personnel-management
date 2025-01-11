package com.pm.personnelmanagement.schedule.exception;

public class CannotDeleteAbsenceExcuseStatus extends RuntimeException {
    public CannotDeleteAbsenceExcuseStatus() {
    }

    public CannotDeleteAbsenceExcuseStatus(String message) {
        super(message);
    }
}
