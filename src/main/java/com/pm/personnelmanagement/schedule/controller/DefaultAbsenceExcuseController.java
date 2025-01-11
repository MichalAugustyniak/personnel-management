package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.AbsenceExcuseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/absence-excuses")
public class DefaultAbsenceExcuseController implements AbsenceExcuseController {
    private final AbsenceExcuseService absenceExcuseService;

    public DefaultAbsenceExcuseController(AbsenceExcuseService absenceExcuseService) {
        this.absenceExcuseService = absenceExcuseService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<AbsenceExcuseDTO> getAbsenceExcuse(@NotNull @PathVariable UUID uuid) {
        return null;
    }

    @GetMapping
    @Override
    public ResponseEntity<AbsenceExcuseListDTO> getAbsenceExcuses(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) UUID attendanceUUID
    ) {
        return ResponseEntity.ok(absenceExcuseService.getAbsenceExcuses(
                new FetchAbsenceExcusesFiltersDTO(
                        pageNumber,
                        pageSize,
                        attendanceUUID
                )
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<CreateAbsenceExcuseResponse> createAbsenceExcuse(@NotNull @Valid @RequestBody CreateAbsenceExcuseDTO dto) {
        return new ResponseEntity<>(new CreateAbsenceExcuseResponse(
                absenceExcuseService.createAbsenceExcuse(dto)
        ), HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateAbsenceExcuse(@NotNull @PathVariable UUID uuid, @NotNull @Valid @RequestBody UpdateAbsenceExcuseBodyDTO body) {
        absenceExcuseService.updateAbsenceExcuse(new UpdateAbsenceExcuseDTO(uuid, body));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteAbsenceExcuse(@NotNull @PathVariable UUID uuid) {
        absenceExcuseService.deleteAbsenceExcuse(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
