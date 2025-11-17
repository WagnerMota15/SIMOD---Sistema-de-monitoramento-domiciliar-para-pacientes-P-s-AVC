package com.example.simodapp;

public class UserRequest {

    private String CPF;
    private String nomeComplete;
    private String email;
    private String password;
    private String telephone;

    public UserRequest(String nomeComplete, String CPF, String email, String password, String telephone) {
        this.nomeComplete = nomeComplete;
        this.CPF = CPF;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
    }


    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getNomeComplete() {
        return nomeComplete;
    }

    public void setNomeComplete(String nomeComplete) {
        this.nomeComplete = nomeComplete;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
