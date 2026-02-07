package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.endereço.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    // Consulta derivada do Spring Data JPA para buscar um endereço único
    // com base em todos os atributos estruturais.

    // Evitar duplicidade de endereços no banco
    // Retorna Optional para forçar tratamento explícito de ausência de dados,
    // evitando NullPointerException e alinhado a boas práticas de robustez.
    Optional<Address> findByCepAndStreetAndNumberAndNeighborhoodAndCityAndState(
            String cep,
            String street,
            String number,
            String neighborhood,
            String city,
            String state
    );
}

