package com.SIMOD.SIMOD.dto.diario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record HealthDiaryRequest(
        LocalDate diaryDate,
        Integer systolicBp,
        Integer diastolicBp,
        Integer heartRate,
        BigDecimal weight,
        String symptoms,
        BigDecimal glucose,
        List<DiaryMedicineRequest> medicines,
        List<DiaryDietRequest> diets,
        List<DiaryActivityRequest> activities,
        List<DiarySessionRequest> sessions
) {}
