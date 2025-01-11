package com.pm.personnelmanagement.schedule.exception;

public class CannotDeleteAttendanceStatusException extends RuntimeException {
    public CannotDeleteAttendanceStatusException() {
    }

    public CannotDeleteAttendanceStatusException(String message) {
        super(message);
    }
}
