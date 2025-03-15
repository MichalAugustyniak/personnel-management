package com.pm.personnelmanagement.common.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PagedResponse<T>(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        @NotNull List<@NotNull T> content
) {
}
