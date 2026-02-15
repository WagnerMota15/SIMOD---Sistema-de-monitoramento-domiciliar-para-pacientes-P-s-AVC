package com.SIMOD.SIMOD.dto.diario;

import java.util.UUID;

public record DiaryActivityRequest(
        UUID activityId,
        boolean completed,
        String note
) {}

