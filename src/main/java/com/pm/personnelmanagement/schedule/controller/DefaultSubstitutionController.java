package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.SubstitutionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/substitutions")
public class DefaultSubstitutionController implements SubstitutionController {
    private final SubstitutionService substitutionService;

    public DefaultSubstitutionController(SubstitutionService substitutionService) {
        this.substitutionService = substitutionService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<SubstitutionDTO> getSubstitution(@NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(substitutionService.getSubstitution(new SubstitutionRequest(uuid)));
    }

    @GetMapping
    @Override
    public ResponseEntity<SubstitutionsDTO> getSubstitutions(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) UUID scheduleDayUUID,
            @RequestParam(required = false) UUID substitutedUserUUID,
            @RequestParam(required = false) UUID substitutingUserUUID,
            @RequestParam(required = false) LocalDateTime startDateTime,
            @RequestParam(required = false) LocalDateTime endDateTime
    ) {
        return ResponseEntity.ok(substitutionService.getSubstitutions(new FetchSubstitutionsFiltersDTO(
                pageNumber, pageSize, scheduleDayUUID, substitutedUserUUID, substitutingUserUUID, startDateTime, endDateTime))
        );
    }

    @PostMapping
    @Override
    public ResponseEntity<SubstitutionCreationResponse> createSubstitution(@NotNull @Valid @RequestBody SubstitutionCreationRequest request) {
        return new ResponseEntity<>(substitutionService.createSubstitution(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateSubstitution(@NotNull @PathVariable UUID uuid, @NotNull @Valid @RequestBody SubstitutionUpdateRequestBody body) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteSubstitution(@NotNull @PathVariable UUID uuid) {
        substitutionService.deleteSubstitution(new SubstitutionDeletionRequest(uuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
