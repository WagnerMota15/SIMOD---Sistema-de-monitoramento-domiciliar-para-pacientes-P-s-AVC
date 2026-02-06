package com.SIMOD.SIMOD.dto.auth;

import com.SIMOD.SIMOD.domain.enums.Role;
import com.SIMOD.SIMOD.domain.enums.StrokeTypes;
import com.SIMOD.SIMOD.validation.ValidRegistration;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidRegistration
public record RegisterRequest(
        @NotBlank String nameComplete,
        String cpf, @Email @NotBlank String email,
        @Size(min = 8) String password, String telephone,
        @NotNull Role role,
        StrokeTypes strokeTypes,
        String numCouncil
) {}
