package com.SIMOD.SIMOD.dto.diario;

import com.SIMOD.SIMOD.domain.enums.ActivitiesTypes;
import com.SIMOD.SIMOD.domain.enums.PlansTreatment;

import java.util.UUID;

public record HealthDiaryActivityResponse(
        UUID id,
        String name,
        ActivitiesTypes type,
        boolean status,
        String note
) {}

