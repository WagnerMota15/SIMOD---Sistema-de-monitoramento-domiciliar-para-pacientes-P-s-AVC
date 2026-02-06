package com.SIMOD.SIMOD.dto.diario;

import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;

import java.time.LocalDate;
import java.util.UUID;

public record ReminderCompletedResponse(
        UUID id,
        String patienteName,
        boolean success,
        String message
) {}
