package com.SIMOD.SIMOD.dto.diario;

import java.util.UUID;

public record DiarySessionRequest(
        UUID sessionId,
        boolean attended,
        String note
) {}

