package com.SIMOD.SIMOD.dto.auth;

import com.SIMOD.SIMOD.domain.enums.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank String login,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String fcmToken,
        Platform platform
) {}
