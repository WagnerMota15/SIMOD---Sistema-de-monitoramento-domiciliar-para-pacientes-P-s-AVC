package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.endereço.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    Optional<Address> findByCepAndStreetAndNumberAndNeighborhoodAndCityAndState(
            String cep,
            String street,
            String number,
            String neighborhood,
            String city,
            String state
    );
}

