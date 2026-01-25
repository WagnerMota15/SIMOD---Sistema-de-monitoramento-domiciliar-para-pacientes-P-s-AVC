package com.SIMOD.SIMOD.dto.Messages;

import com.SIMOD.SIMOD.domain.enums.TipoNotificacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationsResponse(
        UUID id,
        String title,
        String message,
        TipoNotificacao type,
        boolean read,
        LocalDateTime createdAt
) {}
