package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CaregiverRepository extends JpaRepository<Caregiver, UUID> {
    Optional<Caregiver> findByCpf(String cpf);
    Optional<Caregiver> findByIdUser(UUID idUser);
}
