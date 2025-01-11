package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.AbsenceExcuseStatusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/absence-excuse-statuses")
public class DefaultAbsenceExcuseStatusController implements AbsenceExcuseStatusController {
    private final AbsenceExcuseStatusService absenceExcuseStatusService;

    public DefaultAbsenceExcuseStatusController(AbsenceExcuseStatusService absenceExcuseStatusService) {
        this.absenceExcuseStatusService = absenceExcuseStatusService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<AbsenceExcuseStatusDTO> getAbsenceExcuseStatus(@NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(absenceExcuseStatusService.getAbsenceExcuseStatus(uuid));
    }

    @GetMapping
    @Override
    public ResponseEntity<AbsenceExcuseStatusesResponse> getAbsenceExcuses(
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize
    ) {
        return ResponseEntity.ok(absenceExcuseStatusService.getAbsenceExcuses(
                new FetchAbsenceExcuseStatusesFiltersDTO(pageNumber, pageSize)
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<CreateAbsenceExcuseStatusResponse> createAbsenceExcuseStatus(@NotNull @Valid @RequestBody CreateAbsenceStatusDTO dto) {
        return new ResponseEntity<>(new CreateAbsenceExcuseStatusResponse(
                absenceExcuseStatusService.createAbsenceExcuseStatus(dto)
        ), HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateAbsenceStatus(
            @NotNull @RequestParam UUID uuid,
            @NotNull @RequestBody UpdateAbsenceExcuseStatusBodyDTO body
    ) {
        absenceExcuseStatusService.updateAbsenceStatus(new UpdateAbsenceStatusDTO(uuid, body));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteAbsenceStatus(@NotNull @PathVariable UUID uuid) {
        absenceExcuseStatusService.deleteAbsenceStatus(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
