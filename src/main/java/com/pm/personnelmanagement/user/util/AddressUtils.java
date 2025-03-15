package com.pm.personnelmanagement.user.util;

import com.pm.personnelmanagement.user.exception.AddressNotFoundException;
import com.pm.personnelmanagement.user.model.Address;
import com.pm.personnelmanagement.user.repository.AddressRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddressUtils {
    private final AddressRepository addressRepository;

    public AddressUtils(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address fetchByUUID(@NotNull UUID uuid) {
        return addressRepository.findByUuid(uuid)
                .orElseThrow(() -> new AddressNotFoundException(
                        String.format("Address of uuid %s not found", uuid)
                ));
    }

    public Address fetchById(long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(
                        String.format("Address of id %s not found", id)
                ));
    }

    public Page<Address> fetchAddresses(@NotNull Specification<Address> specification, int pageNumber, int pageSize) {
        return addressRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
    }
}
