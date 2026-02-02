package com.SIMOD.SIMOD.dto.Messages;

import com.SIMOD.SIMOD.domain.enums.TipoNotificacao;

public record AlertRequest(
        TipoNotificacao tipo,
        String descricao
) {}
