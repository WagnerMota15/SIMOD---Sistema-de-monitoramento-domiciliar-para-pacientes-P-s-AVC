package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.endereço.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
