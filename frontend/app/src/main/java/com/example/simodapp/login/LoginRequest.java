package com.example.simodapp.login;

public class LoginRequest {
    private String CPF;
    private String password;

    public LoginRequest(String CPF, String password) {
        this.CPF = CPF;
        this.password = password;
    }
}
