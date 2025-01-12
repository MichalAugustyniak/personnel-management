package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.SubstitutionDTO;
import com.pm.personnelmanagement.schedule.dto.SubstitutionsDTO;
import com.pm.personnelmanagement.schedule.model.Substitution;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class SubstitutionMapper {
    public static SubstitutionDTO map(@NotNull Substitution substitution) {
        return new SubstitutionDTO(
                substitution.getReason(),
                substitution.getCreatedAt(),
                substitution.getUuid(),
                substitution.getScheduleDay().getUuid(),
                substitution.getSubstitutedUser().getUuid(),
                substitution.getSubstitutingUser().getUuid()
        );
    }

    public static SubstitutionsDTO map(@NotNull Page<Substitution> substitution) {
        return new SubstitutionsDTO(
                substitution.getTotalElements(),
                substitution.getTotalPages(),
                substitution.getNumber(),
                substitution.getNumberOfElements(),
                substitution.getSize(),
                substitution.getContent().stream().map(
                        SubstitutionMapper::map
                ).collect(Collectors.toSet())
        );
    }
}
