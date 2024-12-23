package com.pm.personnelmanagement.taskevent.expcetion;

public class TaskEventNotFoundException extends RuntimeException {
    public TaskEventNotFoundException() {
    }

    public TaskEventNotFoundException(String message) {
        super(message);
    }
}
