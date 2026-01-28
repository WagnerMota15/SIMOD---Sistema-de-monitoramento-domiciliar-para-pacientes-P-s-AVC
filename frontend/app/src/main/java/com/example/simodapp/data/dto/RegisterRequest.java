package com.example.simodapp.data.dto;

import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;

public class RegisterRequest {

    private String nameComplete;
    private String cpf;
    private String email;
    private String password;
    private String telephone;
    private Role role;
    private StrokeTypes strokeTypes;
    private String numCouncil;

    public RegisterRequest(String nameComplete, String cpf, String email, String password, String telephone, Role role, StrokeTypes strokeTypes, String numCouncil) {
        this.nameComplete = nameComplete;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.role = role;
        this.strokeTypes = strokeTypes;
        this.numCouncil = numCouncil;
    }
}
