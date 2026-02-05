package com.example.simodapp.data.dto;

public class RegisterResponse {
    private String userId;
    private String token;

    public String getToken(){
        return token;
    }
    public String getUserId() {
        return userId;
    }
}
