package com.SIMOD.SIMOD.dto.diario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record HealthDiaryResponse(
        UUID id,
        String name,
        LocalDate diaryDate,
        Integer systolicBp,
        Integer diastolicBp,
        Integer heartRate,
        BigDecimal weight,
        String symptoms,
        BigDecimal glucose,
        List<HealthDiaryMedicineResponse> medicines,
        List<HealthDiaryDietResponse> diets,
        List<HealthDiaryActivityResponse> activities,
        List<HealthDiarySessionResponse> sessions,
        OffsetDateTime createdAt,
        UUID patientId,
        UUID caregiverId
) {}
