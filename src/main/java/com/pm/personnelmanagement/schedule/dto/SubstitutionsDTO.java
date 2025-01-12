package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record SubstitutionsDTO(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        @NotNull Set<@NotNull SubstitutionDTO> substitutions
) {
}
