package com.SIMOD.SIMOD.dto.endereco;

import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotNull String cep,
        @NotNull String number,
        String description,
        boolean principal) {
}
