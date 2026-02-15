package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.mensagens.Reminders;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RemindersRepository extends JpaRepository<Reminders, UUID> {
    List<Reminders> findByPatientIdUser(UUID patientId);

    List<Reminders> findByPatientIdUserAndActiveTrue(UUID patientId);
    // Buscar lembretes que devem disparar agora
    List<Reminders> findByActiveTrueAndScheduledAtLessThanEqual(LocalDateTime dateTime);

    Optional<Reminders> findByMedicineAndScheduledAtAndPatient(Medicines medicine, LocalDateTime localDateTime, Patient patient);

    Optional<Reminders> findByDietAndScheduledAtAndPatient(Diet diet, LocalDateTime localDateTime, Patient patient);

    Optional<Reminders> findByActivityAndScheduledAtAndPatient(Activities activity, LocalDateTime localDateTime, Patient patient);

    Optional<Reminders> findBySessionAndScheduledAtAndPatient(Sessions session, LocalDateTime localDateTime, Patient patient);
}
