package com.SIMOD.SIMOD.domain.usuario;

import java.util.UUID;

public record UserRequest(String nomeComplete,String CPF,String email,String password,String telephone) {
}
