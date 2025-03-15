package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.service.ShiftTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/shift-types")
public class DefaultShiftTypeController implements ShiftTypeController{
    private final ShiftTypeService shiftTypeService;

    public DefaultShiftTypeController(ShiftTypeService shiftTypeService) {
        this.shiftTypeService = shiftTypeService;
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<ShiftTypeDTO> getShiftType(@PathVariable UUID uuid) {
        return ResponseEntity.ok(shiftTypeService.getShiftType(
                new ShiftTypeRequest(uuid)
        ));
    }

    @GetMapping
    @Override
    public ResponseEntity<ShiftTypesDTO> getShiftTypes(
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize
    ) {
        return ResponseEntity.ok(shiftTypeService.getShiftTypes(
                new FetchShiftTypesFiltersDTO(pageNumber, pageSize)
        ));
    }

    @PostMapping
    @Override
    public ResponseEntity<ShiftTypeCreationResponse> createShiftType(@RequestBody ShiftTypeCreationRequest dto) {
        System.out.println(dto);
        return new ResponseEntity<>(shiftTypeService.createShiftType(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> updateShiftType(
            @PathVariable UUID uuid,
            @RequestBody ShiftTypeUpdateRequestBody dto
    ) {
        shiftTypeService.updateShiftType(new ShiftTypeUpdateRequest(uuid, dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteFilter(@PathVariable UUID uuid) {
        shiftTypeService.deleteFilter(new ShiftTypeDeletionRequest(uuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
