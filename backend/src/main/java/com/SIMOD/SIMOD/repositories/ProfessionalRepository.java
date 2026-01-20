package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {
    Optional<Professional> findByCpf(String cpf);
}