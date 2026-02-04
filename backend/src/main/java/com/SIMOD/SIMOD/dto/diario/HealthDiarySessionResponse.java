package com.SIMOD.SIMOD.dto.diario;

import java.time.LocalDateTime;
import java.util.UUID;

public record HealthDiarySessionResponse(
        UUID sessionId,
        LocalDateTime dateTime,
        boolean attended,
        String note
) {}
