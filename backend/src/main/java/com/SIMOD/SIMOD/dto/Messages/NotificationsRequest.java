package com.SIMOD.SIMOD.dto.Messages;

import com.SIMOD.SIMOD.domain.enums.TipoNotificacao;

import java.util.UUID;

public record NotificationsRequest(
        String titulo,
        String mensagem,
        String tipo
) {}
