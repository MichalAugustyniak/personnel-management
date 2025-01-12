package com.pm.personnelmanagement.schedule.exception;

public class OvertimeHoursNotFoundException extends RuntimeException {
    public OvertimeHoursNotFoundException() {
    }

    public OvertimeHoursNotFoundException(String message) {
        super(message);
    }
}
