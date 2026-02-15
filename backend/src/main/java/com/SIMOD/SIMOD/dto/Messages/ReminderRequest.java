package com.SIMOD.SIMOD.dto.Messages;

import com.SIMOD.SIMOD.domain.enums.IntervaloRecorrencia;
import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
import com.SIMOD.SIMOD.domain.enums.TipoLembrete;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReminderRequest(
    @NotNull
    TipoLembrete tipo,
    @NotNull
    String descricao,
    @NotNull
    LocalDateTime dataHora,
    Integer intervaloHora,
    @NotNull
    RemetenteVinculo usuarioCadastrante,
    String titulo,
    boolean recorrente,
    IntervaloRecorrencia intervalo,
    UUID medicineId,
    UUID dietId,
    UUID activityId,
    UUID sessionId
){
    // Método auxiliar para facilitar validação no service/controller
    public boolean hasValidReference() {
        return switch (tipo) {
            case MEDICAMENTO -> medicineId != null;
            case DIETA       -> dietId != null;
            case EXERCICIO   -> activityId != null;
            case SESSOES     -> sessionId != null;
            default          -> false;
        };
    }
    public UUID getReferencedId() {
        return switch (tipo) {
            case MEDICAMENTO -> medicineId;
            case DIETA       -> dietId;
            case EXERCICIO   -> activityId;
            case SESSOES     -> sessionId;
            default          -> null;
        };
    }
}
