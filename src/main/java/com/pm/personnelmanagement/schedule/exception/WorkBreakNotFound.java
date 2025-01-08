package com.pm.personnelmanagement.schedule.exception;

public class WorkBreakNotFound extends RuntimeException {
    public WorkBreakNotFound() {
    }

    public WorkBreakNotFound(String message) {
        super(message);
    }
}
