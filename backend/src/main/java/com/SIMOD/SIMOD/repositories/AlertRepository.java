package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.mensagens.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByPatientId(UUID patientId);
    List<Alert> findByPatientIdAndResolvedFalse(UUID patientId);
}
