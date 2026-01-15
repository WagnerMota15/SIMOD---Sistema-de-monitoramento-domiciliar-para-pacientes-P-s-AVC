package com.SIMOD.SIMOD.dto.auth;

public record RegisterCaregiverRequest(String nomeComplete, String CPF, String email, String password, String telephone) {
}
