package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.mensagens.Reminders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RemindersRepository extends JpaRepository<Reminders, UUID> {
    List<Reminders> findByPatientId(UUID patientId);
    // Buscar lembretes ativos
    List<Reminders> findByPatientIdAndActiveTrue(UUID patientId);
    // Buscar lembretes que devem disparar agora
    List<Reminders> findByActiveTrueAndDateTimeLessThanEqual(LocalDateTime now);
}
