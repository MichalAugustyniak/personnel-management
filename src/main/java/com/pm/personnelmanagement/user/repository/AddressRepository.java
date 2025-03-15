package com.pm.personnelmanagement.user.repository;

import com.pm.personnelmanagement.user.model.Address;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    Optional<Address> findById(long id);

    Optional<Address> findByUuid(UUID uuid);

    boolean existsByCountryIsoCodeAndCityAndPostalCodeAndStreetNameAndStreetNumberAndHouseNumberAndApartmentNumberAndRegion(@NotEmpty @Length(min = 2, max = 2) String s, @NotEmpty @Length(min = 2, max = 30) String city, @NotEmpty @Length(min = 3, max = 30) String s1, @NotEmpty @Length(min = 3, max = 30) String s2, @NotEmpty @Length(min = 3, max = 30) String s3, String s4, String s5, String region);
}
