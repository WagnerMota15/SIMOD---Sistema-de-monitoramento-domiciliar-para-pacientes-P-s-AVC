package com.example.simodapp.data.dto;

import com.example.simodapp.domain.enums.Kinship;

import java.util.UUID;

public class FamilyResponse {
    private UUID id;
    private String name;
    private String telephone;
    private Kinship kinship;

    public FamilyResponse(UUID id, String name, String telephone, Kinship kinship) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.kinship = kinship;
    }

}
