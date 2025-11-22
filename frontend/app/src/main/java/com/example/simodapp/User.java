package com.example.simodapp;

public class User {

    private String id;
    private String CPF;
    private String nomeComplete;
    private String email;
    private String password;
    private String telephone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
