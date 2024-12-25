package com.pm.personnelmanagement.common.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiException(
        String message,
        int status,
        LocalDateTime timestamp
) {
}
