package com.SIMOD.SIMOD.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record UpdateFcmTokenRequest(
        @NotBlank String fcmToken
) {}
