package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.common.NullChecker;
import com.pm.personnelmanagement.schedule.exception.AbsenceExcuseNotFoundException;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuse;
import com.pm.personnelmanagement.schedule.repository.AbsenceExcuseRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class AbsenceExcuseUtils {
    private final AbsenceExcuseRepository absenceExcuseRepository;

    public AbsenceExcuseUtils(AbsenceExcuseRepository absenceExcuseRepository) {
        this.absenceExcuseRepository = absenceExcuseRepository;
    }

    public Set<AbsenceExcuse> fetchAndValidateAbsenceExcuses(Set<UUID> absenceExcuseUUIDs) {
        Set<AbsenceExcuse> absenceExcuses = absenceExcuseRepository.findAllByUuidIn(absenceExcuseUUIDs);
        if (absenceExcuses.size() != absenceExcuseUUIDs.size()) {
            throw new IllegalArgumentException("Some of absenceExcuses might not exist");
        }
        return absenceExcuses;
    }

    public AbsenceExcuse fetchAbsenceExcuse(UUID uuid) {
        NullChecker.validate(uuid, "Argument uuid");
        return absenceExcuseRepository.findByUuid(uuid).orElseThrow(
                () -> new AbsenceExcuseNotFoundException(String.format("Absence excuse of uuid %s not found", uuid))
        );
    }
}
