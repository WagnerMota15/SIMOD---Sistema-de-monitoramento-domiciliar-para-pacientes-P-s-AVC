package com.SIMOD.SIMOD.dto.diario;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record HealthDiaryMedicineResponse(
        UUID medicineId,
        String medicineName,
        boolean taken,
        BigDecimal doseTaken,
        String unityTaken,
        LocalTime timeTaken,
        String note
) {}

