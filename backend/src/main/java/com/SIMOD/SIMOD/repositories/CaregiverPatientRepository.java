package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatientId;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CaregiverPatientRepository extends JpaRepository<CaregiverPatient, CaregiverPatientId> {
    boolean existsByCaregiverAndPatientAndStatus(Caregiver caregiver, Patient patient, VinculoStatus status);
    Optional<CaregiverPatient> findByCaregiverAndPatient(Caregiver caregiver, Patient patient);
    List<CaregiverPatient> findByPatientAndStatus(Patient patient, VinculoStatus status);
    List<CaregiverPatient> findByCaregiverAndStatus(Caregiver caregiver, VinculoStatus status);
}
