package com.SIMOD.SIMOD.dto.diario;

import java.math.BigDecimal;
import java.util.UUID;

public record DiaryMedicineRequest(
        UUID medicineId,
        boolean taken,
        BigDecimal doseTaken,
        String unityTaken,
        String timeTaken, // HH:mm
        String note
) {}
