package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.enums.SessionsStatus;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface SessionsRepository extends JpaRepository<Sessions, UUID> {
    Page<Sessions> findByProfessionalId(UUID professionalId, Pageable pageable);

    Page<Sessions> findByPatientId(UUID patientId, Pageable pageable);
}
