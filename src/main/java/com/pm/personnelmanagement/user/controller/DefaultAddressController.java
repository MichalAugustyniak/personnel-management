package com.pm.personnelmanagement.user.controller;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.user.dto.*;
import com.pm.personnelmanagement.user.service.DefaultAddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
public class DefaultAddressController {
    private final DefaultAddressService addressService;

    public DefaultAddressController(DefaultAddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<AddressDTO> getAddress(@NotNull @PathVariable UUID uuid) {
        return ResponseEntity.ok(
                addressService.getAddress(new AddressRequest(uuid))
        );
    }

    @GetMapping
    public ResponseEntity<PagedResponse<AddressDTO>> getAddresses(
            @RequestParam(required = false) String countryIsoCode,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String streetName,
            @RequestParam(required = false) String streetNumber,
            @RequestParam(required = false) String houseNumber,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String apartmentNumber,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "50") int pageSize
    ) {
        System.out.println("IsoCode" + countryIsoCode);
        return ResponseEntity.ok(
                addressService.getAddresses(new AddressesRequest(
                        countryIsoCode,
                        city,
                        streetName,
                        streetNumber,
                        houseNumber,
                        postalCode,
                        apartmentNumber,
                        region,
                        pageNumber,
                        pageSize
                ))
        );
    }

    @PostMapping
    public ResponseEntity<AddressCreationResponse> createAddress(@NotNull @Valid @RequestBody AddressCreationRequest request) {
        return new ResponseEntity<>(addressService.createAddress(request), HttpStatus.CREATED);
    }
}
