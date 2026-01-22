package com.example.simodapp.data.repository;

import com.example.simodapp.data.dto.RegisterResponse;

public interface RegisterCallback {
    void registerSucess(RegisterResponse response);
    void registerError(String message);
}
