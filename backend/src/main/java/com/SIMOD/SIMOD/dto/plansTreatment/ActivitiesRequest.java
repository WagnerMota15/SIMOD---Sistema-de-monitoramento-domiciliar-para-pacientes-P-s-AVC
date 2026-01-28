package com.SIMOD.SIMOD.dto.plansTreatment;

import com.SIMOD.SIMOD.domain.enums.ActivitiesTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ActivitiesRequest(
        @NotBlank(message = "O nome da atividade é obrigatório")
        String name,
        @NotBlank(message = "A descrição é obrigatória")
        String description,
        @NotBlank(message = "O tipo de exercício é obrigatório")
        ActivitiesTypes type_exercise,
        @NotNull(message = "A frequência recomendada é obrigatória")
        @Positive(message = "A frequência deve ser maior que zero")
        Integer freq_recommended,
        @NotBlank(message = "E necessario ter uma URL de um video")
        String video_url
) {}
