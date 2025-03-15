package com.pm.personnelmanagement.user.dto;

import java.util.UUID;

public record AddressDTO(
        UUID uuid,
        String countryIsoCode,
        String city,
        String streetName,
        String streetNumber,
        String houseNumber,
        String postalCode,
        String apartmentNumber
) {
}
