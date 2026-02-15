package com.SIMOD.SIMOD.dto.diario;

import java.util.UUID;

public record HealthDiaryDietResponse(
        UUID dietId,
        boolean followed,
        String note
) {}
