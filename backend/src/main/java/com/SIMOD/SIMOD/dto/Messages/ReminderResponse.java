package com.SIMOD.SIMOD.dto.Messages;

import com.SIMOD.SIMOD.domain.enums.IntervaloRecorrencia;
import com.SIMOD.SIMOD.domain.enums.TipoLembrete;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReminderResponse(
    UUID id,
    TipoLembrete tipo,
    String titulo,
    String descricao,
    LocalDateTime dataHora,
    boolean recorrente,
    IntervaloRecorrencia intervalo,
    boolean ativo
){}
