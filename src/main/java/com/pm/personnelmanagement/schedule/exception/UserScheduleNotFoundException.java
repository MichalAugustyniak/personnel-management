package com.pm.personnelmanagement.schedule.exception;

public class UserScheduleNotFoundException extends RuntimeException {
    public UserScheduleNotFoundException() {
    }

    public UserScheduleNotFoundException(String message) {
        super(message);
    }
}
