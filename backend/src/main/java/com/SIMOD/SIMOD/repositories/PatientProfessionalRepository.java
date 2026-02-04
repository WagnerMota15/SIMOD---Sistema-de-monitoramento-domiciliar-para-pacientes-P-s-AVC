package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessionalId;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientProfessionalRepository extends JpaRepository<PatientProfessional, PatientProfessionalId> {
    boolean existsByPatientAndProfessionalAndStatus(Patient patient, Professional professional, VinculoStatus status);
    Optional<PatientProfessional> findByPatientAndProfessional(Patient patient, Professional professional);
    List<PatientProfessional> findByPatientAndStatus(Patient patient, VinculoStatus status);
    List<PatientProfessional> findByProfessionalAndStatus(Professional professional, VinculoStatus status);
}
