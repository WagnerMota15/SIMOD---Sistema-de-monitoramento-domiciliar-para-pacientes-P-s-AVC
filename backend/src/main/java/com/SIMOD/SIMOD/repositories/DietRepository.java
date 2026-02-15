package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.enums.Status;
import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DietRepository extends JpaRepository<Diet, UUID> {
    List<Diet> findByPatientIdUserAndStatus(UUID patientId, Status status);

    Page<Diet> findByPatientIdUserOrderByCreatedAtDesc(
            UUID patientUserId,
            Pageable pageable
    );

    Page<Diet> findByPatientIdUserAndStatusOrderByCreatedAtDesc(
            UUID patientUserId,
            Status status,
            Pageable pageable
    );

    Page<Diet> findByPatientOrderByCreatedAtDesc(
            Patient patient,
            Pageable pageable
    );

    Page<Diet> findByPatientInOrderByCreatedAtDesc(
            List<Patient> patients,
            Pageable pageable
    );

    Page<Diet> findByPatientInAndStatusOrderByCreatedAtDesc(
            List<Patient> patients,
            Status status,
            Pageable pageable
    );
}
