package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.enums.Status;
import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivitiesRepository extends JpaRepository<Activities, UUID> {
    Page<Activities> findByPatientIdUserOrderByCreatedAtDesc(
            UUID patientUserId,
            Pageable pageable
    );

    Page<Activities> findByPatientIdUserAndStatusOrderByCreatedAtDesc(
            UUID patientUserId,
            Status status,
            Pageable pageable
    );

    Page<Activities> findByPatientOrderByCreatedAtDesc(
            Patient patient,
            Pageable pageable
    );

    Page<Activities> findByPatientInOrderByCreatedAtDesc(
            List<Patient> patients,
            Pageable pageable
    );

    Page<Activities> findByPatientInAndStatusOrderByCreatedAtDesc(
            List<Patient> patients,
            Status status,
            Pageable pageable
    );
}
