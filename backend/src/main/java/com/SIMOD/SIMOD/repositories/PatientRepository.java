package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByCpf(String cpf);

    Optional<Patient> findByIdUser(UUID idUser);
}
