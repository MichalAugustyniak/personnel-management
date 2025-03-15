package com.pm.personnelmanagement.user.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record AddressCreationRequest(
    @NotEmpty @Length(min = 2, max = 2)
    String countryIsoCode,
    @NotEmpty @Length(min = 2, max = 30)
    String city,
    String region,
    @NotEmpty @Length(min = 1, max = 30)
    String postalCode,
    @NotEmpty @Length(min = 1, max = 30)
    String streetName,
    @NotEmpty @Length(min = 1, max = 30)
    String streetNumber,
    String houseNumber,
    String apartmentNumber
    ) {
}
