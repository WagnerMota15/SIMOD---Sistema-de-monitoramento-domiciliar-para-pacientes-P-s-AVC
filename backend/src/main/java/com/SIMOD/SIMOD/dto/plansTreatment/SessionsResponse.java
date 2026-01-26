package com.SIMOD.SIMOD.dto.plansTreatment;

import com.SIMOD.SIMOD.domain.enums.SessionsStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionsResponse(
        UUID id,
        LocalDateTime dateTime,
        Boolean remote,
        String place,
        String patientName,
        UUID patientId,
        String patientNameComplete,
        String professionalName,
        UUID professionalId,
        SessionsStatus status,
        String reasonChange,
        UUID caregiverId
) {}
