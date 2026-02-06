package com.example.simodapp.data.dto;

import com.example.simodapp.domain.enums.Kinship;

import java.util.UUID;

public class FamilyRequest {
    private String name;
    private String telephone;
    private Kinship kinship;

    public FamilyRequest(String name, String telephone, Kinship kinship) {
        this.name = name;
        this.telephone = telephone;
        this.kinship = kinship;
    }

    public String getName() {
        return name;
    }

    public String getTelephone() {
        return telephone;
    }

    public Kinship getKinship() {
        return kinship;
    }
}
