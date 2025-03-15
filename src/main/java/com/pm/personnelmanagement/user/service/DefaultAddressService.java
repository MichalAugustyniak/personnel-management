package com.pm.personnelmanagement.user.service;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.user.dto.*;
import com.pm.personnelmanagement.user.exception.InvalidValueException;
import com.pm.personnelmanagement.user.mapping.AddressMapper;
import com.pm.personnelmanagement.user.model.Address;
import com.pm.personnelmanagement.user.repository.AddressRepository;
import com.pm.personnelmanagement.user.util.AddressUtils;
import com.pm.personnelmanagement.user.util.UserUtils;
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
    private final UserUtils userUtils;
    private final AddressRepository addressRepository;

    public DefaultAddressService(AddressUtils addressUtils, UserUtils userUtils, AddressRepository addressRepository) {
        this.addressUtils = addressUtils;
        this.userUtils = userUtils;
        this.addressRepository = addressRepository;
    }

    public AddressDTO getAddress(@NotNull @Valid AddressRequest request) {
        return AddressMapper.map(addressUtils.fetchByUUID(request.uuid()));
    }

    public PagedResponse<AddressDTO> getAddresses(@NotNull @Valid AddressesRequest request) {
        System.out.println(request);
        Specification<Address> specification = Specification.where(null);
        specification = addCriteria(specification, request.countryIsoCode(), "countryIsoCode");
        specification = addCriteria(specification, request.postalCode(), "postalCode");
        specification = addCriteria(specification, request.region(), "region");
        specification = addCriteria(specification, request.city(), "city");
        specification = addCriteria(specification, request.streetName(), "streetName");
        specification = addCriteria(specification, request.streetNumber(), "streetNumber");
        specification = addCriteria(specification, request.houseNumber(), "houseNumber");
        specification = addCriteria(specification, request.apartmentNumber(), "apartmentNumber");

        /*
        if (request.countryIsoCode() != null) {
            Specification<Address> hasCountry = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("countryIsoCode"), request.countryIsoCode());
            specification = specification.and(hasCountry);
        }

        if (request.postalCode() != null) {
            Specification<Address> hasPostalCode = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("postalCode"), request.postalCode());
             specification = specification.and(hasPostalCode);
        }

        if (request.region() != null) {
            Specification<Address> hasRegion = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("region"), request.region());
            specification = specification.and(hasRegion);
        }

        if (request.city() != null) {
            Specification<Address> hasCity = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("city"), cityName);
            specification = specification.and(hasCity);
        }

        if (request.streetName() != null) {
            Specification<Address> hasStreetName = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("streetName"), request.streetName());
            specification = specification.and(hasStreetName);
        }

        if (request.streetName() != null) {
            Specification<Address> hasStreetName = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("streetName"), request.streetName());
            specification = specification.and(hasStreetName);
        }

         */

//        Optional.ofNullable(request.streetName()).ifPresent(streetName -> {
//            Specification<Address> hasStreetName = (root, query, criteriaBuilder) ->
//                    criteriaBuilder.equal(root.get("streetName"), streetName);
//            specification.and(hasStreetName);
//        });
//        Optional.ofNullable(request.streetNumber()).ifPresent(streetNumber -> {
//            Specification<Address> hasStreetNumber = (root, query, criteriaBuilder) ->
//                    criteriaBuilder.equal(root.get("streetNumber"), streetNumber);
//            specification.and(hasStreetNumber);
//        });
//        Optional.ofNullable(request.houseNumber()).ifPresent(houseNumber -> {
//            Specification<Address> hasHouseNumber = (root, query, criteriaBuilder) ->
//                    criteriaBuilder.equal(root.get("houseNumber"), houseNumber);
//            specification.and(hasHouseNumber);
//        });
//        Optional.ofNullable(request.postalCode()).ifPresent(postalCode -> {
//            Specification<Address> hasPostalCode = (root, query, criteriaBuilder) ->
//                    criteriaBuilder.equal(root.get("postalCode"), postalCode);
//            specification.and(hasPostalCode);
//        });
//        Optional.ofNullable(request.apartmentNumber()).ifPresent(apartmentNumber -> {
//            Specification<Address> hasApartmentNumber = (root, query, criteriaBuilder) ->
//                    criteriaBuilder.equal(root.get("apartmentNumber"), apartmentNumber);
//            specification.and(hasApartmentNumber);
//        });
//        Optional.ofNullable(request.region()).ifPresent(region -> {
//            System.out.println("hasRegion");
//            Specification<Address> hasRegion = (root, query, criteriaBuilder) ->
//                    criteriaBuilder.equal(root.get("region"), region);
//            specification.and(hasRegion);
//        });
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
