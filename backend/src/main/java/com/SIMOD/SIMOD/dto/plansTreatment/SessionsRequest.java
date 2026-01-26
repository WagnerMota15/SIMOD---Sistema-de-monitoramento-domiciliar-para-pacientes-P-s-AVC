package com.SIMOD.SIMOD.dto.plansTreatment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SessionsRequest(
        @NotNull(message = "A data e hora da sessão são obrigatórias")
        @Future(message = "A sessão deve ser marcada para uma data futura")
        LocalDateTime dateTime,
        @NotNull(message = "Informe se a sessão é remota ou presencial")
        Boolean remote,
        @NotBlank(message = "O local da sessão é obrigatório")
        String place
) {}
