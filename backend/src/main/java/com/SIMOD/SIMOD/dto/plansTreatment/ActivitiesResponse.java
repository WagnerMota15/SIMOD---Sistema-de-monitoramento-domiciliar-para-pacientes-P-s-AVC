package com.SIMOD.SIMOD.dto.plansTreatment;

import com.SIMOD.SIMOD.domain.enums.ActivitiesTypes;
import com.SIMOD.SIMOD.domain.enums.Status;

import java.util.UUID;

public record ActivitiesResponse(
        UUID id,
        String name,
        String description,
        ActivitiesTypes type_exercise,
        Integer freq_recommended,
        String video_url,
        Status status,
        UUID patientId,
        UUID professionalId
) {}
