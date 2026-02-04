    package com.SIMOD.SIMOD.repositories;

    import com.SIMOD.SIMOD.domain.enums.Status;
    import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
    import com.SIMOD.SIMOD.domain.model.paciente.Patient;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.List;
    import java.util.UUID;

    public interface MedicinesRepository extends JpaRepository<Medicines, UUID> {
        List<Medicines> findByPatientIdUserAndStatus(UUID patientId, Status status);

        Page<Medicines> findByPatientIdUserOrderByCreatedAtDesc(
                UUID patientUserId,
                Pageable pageable
        );

        Page<Medicines> findByPatientIdUserAndStatusOrderByCreatedAtDesc(
                UUID patientUserId,
                Status status,
                Pageable pageable
        );

        Page<Medicines> findByPatientOrderByCreatedAtDesc(
                Patient patient,
                Pageable pageable
        );

        Page<Medicines> findByPatientInOrderByCreatedAtDesc(
                List<Patient> patients,
                Pageable pageable
        );

        Page<Medicines> findByPatientInAndStatusOrderByCreatedAtDesc(
                List<Patient> patients,
                Status status,
                Pageable pageable
        );
    }
