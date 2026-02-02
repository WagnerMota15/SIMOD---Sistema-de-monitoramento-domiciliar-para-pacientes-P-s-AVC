package com.SIMOD.SIMOD.dto.samu;

import java.util.List;
import java.util.UUID;

public record SamuResponse(
        List<String> cuidadoresNotificados,
        List<String> profissionaisNotificados
) {}

