package com.pm.personnelmanagement.user.mapping;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.user.dto.AddressDTO;
import com.pm.personnelmanagement.user.model.Address;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public class AddressMapper {
    public static AddressDTO map(@NotNull Address address) {
        return new AddressDTO(
                address.getUuid(),
                address.getCountryIsoCode(),
                address.getCity(),
                address.getStreetName(),
                address.getStreetNumber(),
                address.getHouseNumber(),
                address.getPostalCode(),
                address.getApartmentNumber()
                );
    }

    public static PagedResponse<AddressDTO> map(@NotNull Page<Address> addresses) {
        return new PagedResponse<>(
                addresses.getTotalElements(),
                addresses.getTotalPages(),
                addresses.getNumber(),
                addresses.getNumberOfElements(),
                addresses.getSize(),
                addresses.getContent().stream().map(AddressMapper::map).toList()
        );
    }
}
