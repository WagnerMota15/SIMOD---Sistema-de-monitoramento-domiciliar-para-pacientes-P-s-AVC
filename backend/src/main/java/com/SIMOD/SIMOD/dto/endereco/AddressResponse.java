package com.SIMOD.SIMOD.dto.endereco;

import java.util.UUID;

public record AddressResponse(
        UUID id,
        String cep,
        String number,
        String description,
        boolean principal
) {}
