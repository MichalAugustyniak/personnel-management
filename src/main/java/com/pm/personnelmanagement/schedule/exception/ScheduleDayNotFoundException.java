package com.pm.personnelmanagement.schedule.exception;

public class ScheduleDayNotFoundException extends RuntimeException {
    public ScheduleDayNotFoundException() {
    }

    public ScheduleDayNotFoundException(String message) {
        super(message);
    }
}
