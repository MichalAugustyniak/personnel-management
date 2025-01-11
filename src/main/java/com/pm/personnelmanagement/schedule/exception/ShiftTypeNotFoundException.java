package com.pm.personnelmanagement.schedule.exception;

public class ShiftTypeNotFoundException extends RuntimeException {
    public ShiftTypeNotFoundException() {
    }

    public ShiftTypeNotFoundException(String message) {
        super(message);
    }
}
