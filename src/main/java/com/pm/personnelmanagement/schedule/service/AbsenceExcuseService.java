package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

import java.util.UUID;

public interface AbsenceExcuseService {
    AbsenceExcuseDTO getAbsenceExcuse(UUID uuid);

    AbsenceExcuseListDTO getAbsenceExcuses(FetchAbsenceExcusesFiltersDTO filters);

    UUID createAbsenceExcuse(CreateAbsenceExcuseDTO dto);

    void updateAbsenceExcuse(UpdateAbsenceExcuseDTO dto);

    void deleteAbsenceExcuse(UUID uuid);
}
