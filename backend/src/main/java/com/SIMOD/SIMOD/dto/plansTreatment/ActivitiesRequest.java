package com.SIMOD.SIMOD.dto.plansTreatment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ActivitiesRequest(
        @NotBlank(message = "O nome da atividade é obrigatório")
        String name,
        @NotBlank(message = "A descrição é obrigatória")
        String description,
        @NotBlank(message = "O tipo de exercício é obrigatório")
        String type_exercise,
        @NotNull(message = "A frequência recomendada é obrigatória")
        @Positive(message = "A frequência deve ser maior que zero")
        Integer freq_recommended,
        String video_url
) {}
