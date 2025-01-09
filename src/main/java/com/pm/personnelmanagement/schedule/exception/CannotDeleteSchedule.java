package com.pm.personnelmanagement.schedule.exception;

public class CannotDeleteSchedule extends RuntimeException {
    public CannotDeleteSchedule() {
    }

    public CannotDeleteSchedule(String message) {
        super(message);
    }
}
