package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.SubstitutionNotFoundException;
import com.pm.personnelmanagement.schedule.model.Substitution;
import com.pm.personnelmanagement.schedule.repository.SubstitutionRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SubstitutionUtils {
    private final SubstitutionRepository substitutionRepository;

    public SubstitutionUtils(SubstitutionRepository substitutionRepository) {
        this.substitutionRepository = substitutionRepository;
    }

    public Substitution fetchSubstitution(@NotNull UUID uuid) {
        return substitutionRepository.findByUuid(uuid)
                .orElseThrow(() -> new SubstitutionNotFoundException(
                        String.format("Substitution of uuid %s not found", uuid)
                ));
    }
}
