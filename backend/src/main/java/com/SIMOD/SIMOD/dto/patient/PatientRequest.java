package com.SIMOD.SIMOD.dto.patient;

public record PatientRequest(String nomeComplete, String CPF, String email, String password, String telephone, String tipoAVC) {
}
