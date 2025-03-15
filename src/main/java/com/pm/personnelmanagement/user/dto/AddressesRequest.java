package com.pm.personnelmanagement.user.dto;

public record AddressesRequest(
        String countryIsoCode,
        String city,
        String streetName,
        String streetNumber,
        String houseNumber,
        String postalCode,
        String apartmentNumber,
        String region,
        int pageNumber,
        int pageSize
) {
}
