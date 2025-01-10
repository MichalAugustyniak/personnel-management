package com.pm.personnelmanagement.schedule.exception;

public class AttendanceStatusNotFoundException extends RuntimeException {
    public AttendanceStatusNotFoundException() {
    }

    public AttendanceStatusNotFoundException(String message) {
        super(message);
    }
}
