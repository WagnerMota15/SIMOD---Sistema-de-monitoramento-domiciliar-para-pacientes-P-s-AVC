package com.SIMOD.SIMOD.dto.Messages;

import com.SIMOD.SIMOD.domain.enums.IntervaloRecorrencia;
import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
import com.SIMOD.SIMOD.domain.enums.TipoLembrete;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderRequest(
    @NotNull
    TipoLembrete tipo,
    @NotNull
    String descricao,
    @NotNull
    LocalDateTime dataHora,
    @NotNull
    RemetenteVinculo usuarioCadastrante,
    String titulo,
    boolean recorrente,
    IntervaloRecorrencia intervalo
){}
