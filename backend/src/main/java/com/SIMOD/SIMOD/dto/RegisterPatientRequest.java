package com.SIMOD.SIMOD.dto;

public record RegisterPatientRequest(String nomeComplete, String CPF, String email, String password, String telephone, String tipoAVC) {
}
