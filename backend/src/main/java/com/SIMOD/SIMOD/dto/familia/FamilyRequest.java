package com.SIMOD.SIMOD.dto.familia;

import com.SIMOD.SIMOD.domain.enums.Kinship;
import jakarta.validation.constraints.NotNull;

public record FamilyRequest(String name, String telephone, @NotNull Kinship kinship) {
}
