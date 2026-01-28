package com.SIMOD.SIMOD.dto.plansTreatment;

import java.util.UUID;

public record DietResponse(
    UUID id,
    Integer freq_meal,
    String schedules,
    String description,
    UUID patientId,
    UUID professionalId
){}
