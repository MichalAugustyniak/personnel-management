package com.pm.personnelmanagement.schedule.exception;

public class AttendanceNotFoundException extends RuntimeException {
    public AttendanceNotFoundException(String message) {
        super(message);
    }

    public AttendanceNotFoundException() {
    }
}
