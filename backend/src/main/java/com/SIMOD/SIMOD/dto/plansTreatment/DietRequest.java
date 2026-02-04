package com.SIMOD.SIMOD.dto.plansTreatment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DietRequest(
        @NotNull(message = "A frequência de refeições é obrigatória")
        @Positive(message = "A frequência deve ser maior que zero")
        Integer freq_meal,
        @NotBlank(message = "O horário das refeições é obrigatório")
        String schedules,
        @NotBlank(message = "A descrição da dieta é obrigatória")
        String description
) {}
