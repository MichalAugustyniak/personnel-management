package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.DetailedAbsenceExcuseStatusDTO;
import com.pm.personnelmanagement.schedule.dto.DetailedAbsenceExcuseStatusesDTO;
import com.pm.personnelmanagement.schedule.model.DetailedAbsenceExcuseStatus;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class DetailedAbsenceExcuseStatusMapper {
    public static DetailedAbsenceExcuseStatusDTO map(DetailedAbsenceExcuseStatus detailedAbsenceExcuseStatus) {
        return new DetailedAbsenceExcuseStatusDTO(
                detailedAbsenceExcuseStatus.getUuid(),
                detailedAbsenceExcuseStatus.getMessage(),
                detailedAbsenceExcuseStatus.getChecked(),
                detailedAbsenceExcuseStatus.getAbsenceExcuseStatus().getUuid()
        );
    }

    public static DetailedAbsenceExcuseStatusesDTO map(Page<DetailedAbsenceExcuseStatus> detailedAbsenceExcuseStatuses) {
        return new DetailedAbsenceExcuseStatusesDTO(
                detailedAbsenceExcuseStatuses.getTotalElements(),
                detailedAbsenceExcuseStatuses.getTotalPages(),
                detailedAbsenceExcuseStatuses.getNumber(),
                detailedAbsenceExcuseStatuses.getNumberOfElements(),
                detailedAbsenceExcuseStatuses.getSize(),
                detailedAbsenceExcuseStatuses.getContent().stream().map(
                        detailedAbsenceExcuseStatus -> new DetailedAbsenceExcuseStatusDTO(
                                detailedAbsenceExcuseStatus.getUuid(),
                                detailedAbsenceExcuseStatus.getMessage(),
                                detailedAbsenceExcuseStatus.getChecked(),
                                detailedAbsenceExcuseStatus.getAbsenceExcuseStatus().getUuid()
                        )
                ).collect(Collectors.toSet())
        );
    }
}
