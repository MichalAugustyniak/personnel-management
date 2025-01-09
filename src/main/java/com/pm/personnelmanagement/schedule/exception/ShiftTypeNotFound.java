package com.pm.personnelmanagement.schedule.exception;

public class ShiftTypeNotFound extends RuntimeException {
    public ShiftTypeNotFound() {
    }

    public ShiftTypeNotFound(String message) {
        super(message);
    }
}
