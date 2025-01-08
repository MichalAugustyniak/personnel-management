package com.pm.personnelmanagement.schedule.exception;

public class ScheduleDayNotFound extends RuntimeException {
    public ScheduleDayNotFound() {
    }

    public ScheduleDayNotFound(String message) {
        super(message);
    }
}
