package com.SIMOD.SIMOD.dto.cuidador;

public record CaregiverRequest(
        String nomeComplete,
        String CPF,
        String email,
        String password,
        String telephone
) {}
