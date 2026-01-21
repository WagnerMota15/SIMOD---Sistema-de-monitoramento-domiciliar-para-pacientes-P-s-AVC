package com.SIMOD.SIMOD.dto.familia;

import com.SIMOD.SIMOD.domain.enums.Kinship;

import java.util.UUID;

public record FamilyResponse(UUID id,String name, String telephone, Kinship kniship) {
}
