package com.pm.personnelmanagement.config;

public class AppConfigPropertyNotFoundException extends RuntimeException {
    public AppConfigPropertyNotFoundException() {
    }

    public AppConfigPropertyNotFoundException(String message) {
        super(message);
    }
}
