package com.SIMOD.SIMOD.dto.endereco;

import jakarta.validation.constraints.NotNull;

public record AddressRequest(@NotNull String cep, @NotNull String street,@NotNull String neighborhood,@NotNull String city,@NotNull String state, @NotNull String number) {
}
