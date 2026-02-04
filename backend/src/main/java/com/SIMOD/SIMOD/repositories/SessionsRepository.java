package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.enums.SessionsStatus;
import com.SIMOD.SIMOD.domain.enums.Status;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SessionsRepository extends JpaRepository<Sessions, UUID> {
    List<Sessions> findByPatientIdUserAndStatusIn(UUID patientId, List<SessionsStatus> status);

    Page<Sessions> findByProfessionalIdUser(UUID professionalId, Pageable pageable);

    Page<Sessions> findByPatientIdUser(UUID patientId, Pageable pageable);

    Page<Sessions> findByPatientIdUserAndStatus( UUID patientId, SessionsStatus status, Pageable pageable);
}
