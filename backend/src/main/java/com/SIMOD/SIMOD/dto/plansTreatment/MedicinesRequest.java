package com.SIMOD.SIMOD.dto.plansTreatment;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MedicinesRequest(
        @NotBlank(message = "O nome do medicamento é obrigatório")
        String name,
        @NotNull(message = "A dosagem é obrigatória")
        @DecimalMin(value = "0.01", message = "A dosagem deve ser maior que zero")
        BigDecimal dosagem,
        @NotBlank(message = "A unidade é obrigatória (ex: mg, ml, comprimido)")
        String unity,
        @NotNull(message = "A frequência é obrigatória")
        @Positive(message = "A frequência deve ser maior que zero")
        Integer frequency,
        String description
) {}
