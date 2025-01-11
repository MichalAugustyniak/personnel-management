package com.pm.personnelmanagement.schedule.exception;

public class CannotDeleteShiftType extends RuntimeException {
    public CannotDeleteShiftType() {
    }

    public CannotDeleteShiftType(String message) {
        super(message);
    }
}
