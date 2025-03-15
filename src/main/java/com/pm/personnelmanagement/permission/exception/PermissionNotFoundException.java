package com.pm.personnelmanagement.permission.exception;

public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException() {
    }

    public PermissionNotFoundException(String message) {
        super(message);
    }
}
