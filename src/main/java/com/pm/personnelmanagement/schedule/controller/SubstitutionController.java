package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SubstitutionController {
    ResponseEntity<SubstitutionDTO> getSubstitution(UUID uuid);

    ResponseEntity<SubstitutionsDTO> getSubstitutions(
            Integer pageNumber,
            Integer pageSize,
            UUID scheduleDayUUID,
            UUID substitutedUserUUID,
            UUID substitutingUserUUID,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );

    ResponseEntity<SubstitutionCreationResponse> createSubstitution(SubstitutionCreationRequest request);

    ResponseEntity<Void> updateSubstitution(UUID uuid, SubstitutionUpdateRequestBody body);

    ResponseEntity<Void> deleteSubstitution(UUID uuid);
}
