package com.SIMOD.SIMOD.dto.auth;

public record UpdatePasswordByEmailRequest(
       String email,
       String novaSenha
) {}
