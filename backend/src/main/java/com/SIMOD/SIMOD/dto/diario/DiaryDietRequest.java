package com.SIMOD.SIMOD.dto.diario;

import java.util.UUID;

public record DiaryDietRequest(
        UUID dietId,
        boolean followed,
        String note
) {}
