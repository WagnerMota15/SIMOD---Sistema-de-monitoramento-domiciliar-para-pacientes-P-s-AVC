package com.SIMOD.SIMOD.dto.auth;

import com.SIMOD.SIMOD.domain.enums.Platform;
import jakarta.validation.constraints.NotBlank;

public record UpdateFcmTokenRequest(
        @NotBlank String fcmToken,
        @NotBlank Platform platform
) {}
