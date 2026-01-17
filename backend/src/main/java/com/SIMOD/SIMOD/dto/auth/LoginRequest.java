package com.SIMOD.SIMOD.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record LoginRequest(@NotBlank String login,@NotBlank @Size(min = 8) String password) {
}
