package com.SIMOD.SIMOD.repositories;

import com.SIMOD.SIMOD.domain.model.diario.HealthDiary;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HealthDiaryRepository extends JpaRepository<HealthDiary, UUID> {

    Optional<HealthDiary> findByPatientAndDiaryDate(Patient patient, LocalDate diaryDate);

    Page<HealthDiary> findByPatientOrderByDiaryDateDesc(Patient patient, Pageable pageable);

    Page<HealthDiary> findByPatientInOrderByDiaryDateDesc(List<Patient> patients, Pageable pageable);

    Page<HealthDiary> findByPatientAndDiaryDateGreaterThanEqualOrderByDiaryDateDesc(
            Patient patient,
            LocalDate startDate,
            Pageable pageable
    );
}
