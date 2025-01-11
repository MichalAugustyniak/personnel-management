package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.AbsenceExcuseDTO;
import com.pm.personnelmanagement.schedule.dto.AbsenceExcuseListDTO;
import com.pm.personnelmanagement.schedule.dto.CreateAbsenceExcuseDTO;
import com.pm.personnelmanagement.schedule.dto.FetchAbsenceExcusesFiltersDTO;
import com.pm.personnelmanagement.schedule.dto.UpdateAbsenceExcuseDTO;
import com.pm.personnelmanagement.schedule.exception.AbsenceExcuseNotFoundException;
import com.pm.personnelmanagement.schedule.exception.AttendanceNotFoundException;
import com.pm.personnelmanagement.schedule.mapper.AbsenceExcuseMapper;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuse;
import com.pm.personnelmanagement.schedule.model.Attendance;
import com.pm.personnelmanagement.schedule.repository.AbsenceExcuseRepository;
import com.pm.personnelmanagement.schedule.repository.AttendanceRepository;
import com.pm.personnelmanagement.schedule.utils.AbsenceExcuseUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class DefaultAbsenceExcuseService implements AbsenceExcuseService {
    private final AbsenceExcuseRepository absenceExcuseRepository;
    private final AttendanceRepository attendanceRepository;
    private final AbsenceExcuseUtils absenceExcuseUtils;

    public DefaultAbsenceExcuseService(AbsenceExcuseRepository absenceExcuseRepository, AttendanceRepository attendanceRepository, AbsenceExcuseUtils absenceExcuseUtils) {
        this.absenceExcuseRepository = absenceExcuseRepository;
        this.attendanceRepository = attendanceRepository;
        this.absenceExcuseUtils = absenceExcuseUtils;
    }

    @Override
    public AbsenceExcuseDTO getAbsenceExcuse(@NotNull UUID uuid) {
        AbsenceExcuse absenceExcuse = absenceExcuseRepository.findByUuid(uuid).orElseThrow(
                () -> new AbsenceExcuseNotFoundException(String.format("Absence excuse of uuid %s not found", uuid))
        );
        return AbsenceExcuseMapper.map(absenceExcuse);
    }

    @Override
    public AbsenceExcuseListDTO getAbsenceExcuses(@NotNull FetchAbsenceExcusesFiltersDTO filters) {
        int pageNumber = Optional.ofNullable(filters.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(filters.pageSize()).orElse(10);
        Specification<AbsenceExcuse> specification = Specification.where(null);
        Optional.ofNullable(filters.attendanceUUID()).ifPresent(absenceExcuse -> {
            Specification<AbsenceExcuse> hasAbsenceExcuse = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("attendances").get("uuid"), absenceExcuse);
            specification.and(hasAbsenceExcuse);
        });
        Page<AbsenceExcuse> absenceExcuses = absenceExcuseRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return AbsenceExcuseMapper.map(absenceExcuses);
    }

    @Override
    public UUID createAbsenceExcuse(@Valid @NotNull CreateAbsenceExcuseDTO dto) {
        AbsenceExcuse absenceExcuse = new AbsenceExcuse();
        Set<Attendance> attendances = attendanceRepository.findAllByUuidIn(dto.attendanceUUIDs());
        if (attendances.size() != dto.attendanceUUIDs().size()) {
            throw new AttendanceNotFoundException("Some of attendances might not exist");
        }
        UUID uuid = UUID.randomUUID();
        absenceExcuse.setUuid(uuid);
        absenceExcuse.setName(dto.name());
        absenceExcuse.setFileUUID(dto.fileUUID());
        absenceExcuse.setDetailedAbsenceExcuseStatus(null);
        absenceExcuse.getAttendances().addAll(attendances);
        absenceExcuse.setDescription(dto.description());
        absenceExcuseRepository.save(absenceExcuse);
        return uuid;
    }

    @Override
    public void updateAbsenceExcuse(@Valid @NotNull UpdateAbsenceExcuseDTO dto) {
        AbsenceExcuse absenceExcuse = absenceExcuseUtils.fetchAbsenceExcuse(dto.uuid());
        absenceExcuse.setName(dto.body().name());
        absenceExcuse.setDescription(dto.body().description());
        absenceExcuse.setFileUUID(dto.body().fileUUID());
        absenceExcuseRepository.save(absenceExcuse);
    }

    @Override
    public void deleteAbsenceExcuse(@NotNull UUID uuid) {
        AbsenceExcuse absenceExcuse = absenceExcuseUtils.fetchAbsenceExcuse(uuid);
        absenceExcuseRepository.delete(absenceExcuse);
    }
}
