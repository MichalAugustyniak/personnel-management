package com.pm.personnelmanagement.user.service;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.user.dto.*;
import com.pm.personnelmanagement.user.exception.InvalidValueException;
import com.pm.personnelmanagement.user.mapping.AddressMapper;
import com.pm.personnelmanagement.user.model.Address;
import com.pm.personnelmanagement.user.repository.AddressRepository;
import com.pm.personnelmanagement.user.util.AddressUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.pm.personnelmanagement.common.util.SpecificationUtils.addCriteria;

@Service
public class DefaultAddressService {
    private final AddressUtils addressUtils;
    private final AddressRepository addressRepository;

    public DefaultAddressService(AddressUtils addressUtils, AddressRepository addressRepository) {
        this.addressUtils = addressUtils;
        this.addressRepository = addressRepository;
    }

    public AddressDTO getAddress(@NotNull @Valid AddressRequest request) {
        return AddressMapper.map(addressUtils.fetchByUUID(request.uuid()));
    }

    public PagedResponse<AddressDTO> getAddresses(@NotNull @Valid AddressesRequest request) {
        Specification<Address> specification = Specification.where(null);
        specification = addCriteria(specification, request.countryIsoCode(), "countryIsoCode");
        specification = addCriteria(specification, request.postalCode(), "postalCode");
        specification = addCriteria(specification, request.region(), "region");
        specification = addCriteria(specification, request.city(), "city");
        specification = addCriteria(specification, request.streetName(), "streetName");
        specification = addCriteria(specification, request.streetNumber(), "streetNumber");
        specification = addCriteria(specification, request.houseNumber(), "houseNumber");
        specification = addCriteria(specification, request.apartmentNumber(), "apartmentNumber");
        Page<Address> addresses = addressRepository.findAll(specification, PageRequest.of(request.pageNumber(), request.pageSize()));
        return AddressMapper.map(addresses);
    }

    public AddressCreationResponse createAddress(@NotNull @Valid AddressCreationRequest request) {
        if (addressRepository.existsByCountryIsoCodeAndCityAndPostalCodeAndStreetNameAndStreetNumberAndHouseNumberAndApartmentNumberAndRegion(
                request.countryIsoCode(),
                request.city(),
                request.postalCode(),
                request.streetName(),
                request.streetNumber(),
                request.houseNumber(),
                request.apartmentNumber(),
                request.region())
        ) {
            throw new InvalidValueException("Cannot create address: already exists");
        }
        Address address = new Address();
        UUID uuid = UUID.randomUUID();
        address.setUuid(uuid);
        address.setCountryIsoCode(request.countryIsoCode());
        address.setCity(request.city());
        address.setPostalCode(request.postalCode());
        address.setRegion(request.region());
        address.setStreetName(request.streetName());
        address.setStreetNumber(request.streetNumber());
        address.setHouseNumber(request.houseNumber());
        address.setApartmentNumber(request.apartmentNumber());
        addressRepository.save(address);
        return new AddressCreationResponse(uuid);
    }

}
