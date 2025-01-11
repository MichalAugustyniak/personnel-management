package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.DetailedAbsenceExcuseStatusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/detailed-absence-excuse-statuses")
public class DefaultDetailedAbsenceExcuseStatusController implements DetailedAbsenceExcuseStatusController {
    private final DetailedAbsenceExcuseStatusService detailedAbsenceExcuseStatusService;

    public DefaultDetailedAbsenceExcuseStatusController(DetailedAbsenceExcuseStatusService detailedAbsenceExcuseStatusService) {
        this.detailedAbsenceExcuseStatusService = detailedAbsenceExcuseStatusService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<DetailedAbsenceExcuseStatusDTO> getDetailedAbsenceExcuseStatus(@NotNull @Valid @PathVariable UUID uuid) {
        return ResponseEntity.ok(detailedAbsenceExcuseStatusService.getDetailedAbsenceExcuseStatus(
                new DetailedAbsenceExcuseStatusRequest(uuid))
        );
    }

    @GetMapping
    @Override
    public ResponseEntity<DetailedAbsenceExcuseStatusesDTO> getDetailedAbsenceExcuseStatuses(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Boolean isChecked,
            @RequestParam(required = false) UUID absenceExcuseStatusUUID
    ) {
        return ResponseEntity.ok(detailedAbsenceExcuseStatusService.getDetailedAbsenceExcuseStatuses(
                new DetailedAbsenceStatusesFiltersDTO(pageNumber, pageSize, isChecked, absenceExcuseStatusUUID)
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<DetailedAbsenceExcusesStatusCreationResponse> createDetailedAbsenceExcuseStatus(
            @NotNull @Valid @RequestBody DetailedAbsenceExcuseStatusCreationRequest request) {
        return new ResponseEntity<>(
                detailedAbsenceExcuseStatusService.createDetailedAbsenceExcuseStatus(request),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateDetailedAbsenceExcuseStatus(
            @NotNull @PathVariable UUID uuid,
            @NotNull @Valid @RequestBody DetailedAbsenceExcuseStatusUpdateBodyRequest request
    ) {
        detailedAbsenceExcuseStatusService.updateDetailedAbsenceExcuseStatus(
                new DetailedAbsenceExcuseStatusUpdateRequest(uuid, request)
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteDetailedAbsenceExcuseStatus(@NotNull @PathVariable UUID uuid) {
        detailedAbsenceExcuseStatusService.deleteDetailedAbsenceExcuseStatus(
                new DetailedAbsenceExcuseStatusDeleteRequest(uuid)
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
