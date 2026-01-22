package com.example.simodapp.data.repository;

import com.example.simodapp.data.dto.LoginResponse;
import com.example.simodapp.data.dto.RegisterResponse;

public interface LoginCallback {
    void onSucess(LoginResponse response);
    void onError(String message);
}
